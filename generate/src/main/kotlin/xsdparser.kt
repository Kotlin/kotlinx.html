package html4k.generate

import com.sun.xml.xsom.XSAttGroupDecl
import com.sun.xml.xsom.XSAttributeDecl
import com.sun.xml.xsom.XSComplexType
import com.sun.xml.xsom.XSTerm
import com.sun.xml.xsom.parser.XSOMParser
import html4k.generate.humanize.humanize
import java.util.ArrayList
import java.util.HashSet
import java.util.TreeSet

val SCHEME_URL = "generate/src/main/resources/html_5.xsd"
val HTML_NAMESPACE = "html-5"

private val attributeNamesMap = mapOf("class" to "classes")

private fun flattenTerm(term : XSTerm, result : MutableCollection<String>, visitedModelNames : MutableSet<String>) {
    if (term.isElementDecl()) {
        result.add(term.asElementDecl().getName())
    } else if (term.isModelGroupDecl()) {
        visitedModelNames.add(term.asModelGroupDecl().getName())
        term.asModelGroupDecl().getModelGroup().toList().map {it.getTerm()}.forEach {
            flattenTerm(it, result, visitedModelNames)
        }
    } else if (term.isModelGroup()) {
        term.asModelGroup().toList().map {it.getTerm()}.forEach {
            flattenTerm(it, result, visitedModelNames)
        }
    }
}

fun handleAttributeDeclaration(prefix : String, attributeDeclaration : XSAttributeDecl) : AttributeInfo {
    val name = attributeDeclaration.getName()
    val type = attributeDeclaration.getType()

    val safeName = attributeNamesMap[name] ?: name.escapeUnsafeValues()
    if (type.isUnion()) {
        val enumEntries = type.asUnion()
                .filter {it.isRestriction()}
                .map {it.asRestriction()}
                .flatMap { it.getDeclaredFacets() ?: emptyList() }
                .filter { it.getName() == "enumeration" }
                .map {it.getValue().value}

        return AttributeInfo(name, AttributeType.STRING, safeName, enumValues = enumEntries.toAttributeValues(), enumTypeName = prefix.capitalize() + name.humanize().capitalize())
    } else if (type.isPrimitive() || type.getName() in setOf("integer", "string", "boolean", "decimal")) {
        return AttributeInfo(name, xsdToType[type.getPrimitiveType().getName()] ?: AttributeType.STRING, safeName)
    } else if (type.isRestriction()) {
        val restriction = type.asRestriction()
        val enumEntries = restriction.getDeclaredFacets()
                .filter { it.getName() == "enumeration" }
                .map { it.getValue().value }

        if (enumEntries.size() == 1 && enumEntries.single() == name) {
            // probably ticker
            return AttributeInfo(name, AttributeType.TICKER, safeName)
        } else if (enumEntries.size() == 2 && enumEntries.sort() == listOf("off", "on")) {
            return AttributeInfo(name, AttributeType.BOOLEAN, safeName, trueFalse = listOf("on", "off"))
        } else if (enumEntries.isEmpty()) {
            return AttributeInfo(name, AttributeType.STRING, safeName)
        } else {
            return AttributeInfo(name, AttributeType.ENUM, safeName, enumValues = enumEntries.toAttributeValues(), enumTypeName = prefix.capitalize() + name.humanize().capitalize())
        }
    } else {
        return AttributeInfo(name, AttributeType.STRING, safeName)
    }
}

fun flattenGroups(root : XSAttGroupDecl, result : MutableList<XSAttGroupDecl> = ArrayList()) : List<XSAttGroupDecl> {
    result.add(root)
    root.getAttGroups()?.forEach {
        flattenGroups(it, result)
    }

    return result
}

fun AttributeInfo.handleSpecialType(tagName : String = "") : AttributeInfo = specialTypeFor(tagName, this.name)?.let { type ->
    this.copy(type = type)
} ?: this

fun fillRepository() {
    val parser = XSOMParser()
    parser.parse(SCHEME_URL)
    val schema = parser.getResult().getSchema(HTML_NAMESPACE)

    [suppress("UNCHECKED_CAST")]
    val alreadyIncluded = TreeSet<String>() {a, b -> a.compareToIgnoreCase(b)} as MutableSet<String>
    schema.getAttGroupDecls().values().forEach { attributeGroup ->
        val requiredNames = HashSet<String>()
        val facadeAttributes = attributeGroup.getAttributeUses().map { attributeUse ->
            val attributeDeclaration = attributeUse.getDecl()
            if (attributeUse.isRequired()) {
                requiredNames add attributeDeclaration.getName()
            }

            handleAttributeDeclaration("", attributeDeclaration).handleSpecialType()
        }.filter { it.name !in alreadyIncluded }.filter { !it.name.startsWith("On") }

        val name = attributeGroup.getName()

        if (facadeAttributes.isNotEmpty()) {
            Repository.attributeFacades[name] = AttributeFacade(name, facadeAttributes, requiredNames)
            alreadyIncluded.addAll(facadeAttributes.map { it.name })
        }
    }

    schema.getModelGroupDecls().values().forEach { modelGroupDeclaration ->
        val name = modelGroupDeclaration.getName()
        val children = modelGroupDeclaration.getModelGroup()
                .getChildren()
                .map {it.getTerm()}
                .filter {it.isElementDecl()}
                .map {it.asElementDecl().getName()}

        Repository.tagGroups[name] = TagGroup(name, children)
    }

    schema.getElementDecls().values().forEach { elementDeclaration ->
        val name = elementDeclaration.getName()
        val type = elementDeclaration.getType()
        val suggestedNames = HashSet<String>()
        globalSuggestedAttributes.get(name)?.let {
            suggestedNames.addAll(it)
        }

        val tagInfo : TagInfo
        if (type.isComplexType()) {
            val complex = type.asComplexType()
            val groupDeclarations = complex.getAttGroups().flatMap { flattenGroups(it) }.distinct().toList()
            val attributeGroups = groupDeclarations.map {Repository.attributeFacades[it.getName()]}.filterNotNull()

            val attributes = complex.getDeclaredAttributeUses().map {
                if (it.isRequired()) {
                    suggestedNames add it.getDecl().getName()
                }

                handleAttributeDeclaration(name.humanize(), it.getDecl()).handleSpecialType(name)
            }

            val children = HashSet<String>()
            val modelGroupNames = HashSet<String>()
            val contentTerm = complex.getContentType()?.asParticle()?.getTerm()
            val directChildren = ArrayList<String>()
            if (contentTerm != null) {
                flattenTerm(contentTerm, children, modelGroupNames)
                if (contentTerm.isModelGroup()) {
                    directChildren.addAll(contentTerm.asModelGroup().getChildren().map {it.getTerm()}.filter {it.isElementDecl()}.map {it.asElementDecl().getName()})
                }
            }

            tagInfo = TagInfo(name, children.toList().sort(), directChildren, attributeGroups, attributes, suggestedNames, modelGroupNames.sort().toList())
        } else {
            throw UnsupportedOperationException()
        }

        Repository.tags[name] = tagInfo
    }
}

private val xsdToType = mapOf(
        "boolean" to AttributeType.BOOLEAN,
        "string" to AttributeType.STRING,
        "anyURI" to AttributeType.STRING // TODO links
)