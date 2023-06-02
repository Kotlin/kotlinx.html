package kotlinx.html.generate

import com.sun.xml.xsom.*
import com.sun.xml.xsom.parser.*
import java.util.*
import javax.xml.parsers.*

val SCHEME_URL = "html_5.xsd".asResourceUrl()
const val HTML_NAMESPACE = "html-5"

private fun flattenTerm(term: XSTerm, result: MutableCollection<String>, visitedModelNames: MutableSet<String>) {
    if (term.isElementDecl) {
        result.add(term.asElementDecl().name)
    } else if (term.isModelGroupDecl) {
        visitedModelNames.add(term.asModelGroupDecl().name)
        term.asModelGroupDecl().modelGroup.toList().map { it.term }.forEach {
            flattenTerm(it, result, visitedModelNames)
        }
    } else if (term.isModelGroup) {
        term.asModelGroup().toList().map { it.term }.forEach {
            flattenTerm(it, result, visitedModelNames)
        }
    }
}

fun handleAttributeDeclaration(prefix: String, attributeDeclaration: XSAttributeDecl): AttributeInfo {
    val name = attributeDeclaration.name
    val type = attributeDeclaration.type

    if (type.isUnion) {
        val enumEntries = type.asUnion()
                .filter { it.isRestriction }
                .map { it.asRestriction() }
                .flatMap { it.declaredFacets ?: emptyList() }
                .filter { it.name == "enumeration" }
                .map { it.value.value }

        return AttributeInfo(name, AttributeType.STRING, enumValues = enumEntries.toAttributeValues(), enumTypeName = prefix.capitalize() + name.humanize().capitalize())
    } else if (type.isPrimitive || type.name in setOf<String?>("integer", "string", "boolean", "decimal")) {
        return AttributeInfo(name, xsdToType[type.primitiveType.name] ?: AttributeType.STRING)
    } else if (type.isRestriction) {
        val restriction = type.asRestriction()
        val enumEntries = restriction.declaredFacets
                .filter { it.name == "enumeration" }
                .map { it.value.value }

        return if (enumEntries.size == 1 && enumEntries.single() == name) {
            // probably ticker
            AttributeInfo(name, AttributeType.TICKER)
        } else if (enumEntries.size == 2 && enumEntries.sorted() == listOf("off", "on")) {
            AttributeInfo(name, AttributeType.BOOLEAN, trueFalse = listOf("on", "off"))
        } else if (enumEntries.isEmpty()) {
            AttributeInfo(name, AttributeType.STRING)
        } else {
            AttributeInfo(name, AttributeType.ENUM, enumValues = enumEntries.toAttributeValues(), enumTypeName = prefix.capitalize() + name.humanize().capitalize())
        }
    } else {
        return AttributeInfo(name, AttributeType.STRING)
    }
}

fun flattenGroups(root: XSAttGroupDecl, result: MutableList<XSAttGroupDecl> = ArrayList()): List<XSAttGroupDecl> {
    result.add(root)
    root.attGroups?.forEach {
        flattenGroups(it, result)
    }

    return result
}

fun AttributeInfo.handleSpecialType(tagName: String = ""): AttributeInfo = specialTypeFor(tagName, this.name)?.let { type ->
    this.copy(type = type)
} ?: this

fun fillRepository(repository: Repository) {
    val parser = XSOMParser(SAXParserFactory.newInstance())
    parser.parse(SCHEME_URL)
    val schema = parser.result.getSchema(HTML_NAMESPACE)

    val alreadyIncluded = TreeSet<String> { a, b -> a.compareTo(b, true) }
    schema.attGroupDecls.values.sortedByDescending { it.attributeUses.size }.forEach { attributeGroup ->
        val requiredNames = HashSet<String>()
        val facadeAttributes = attributeGroup.attributeUses.map { attributeUse ->
            val attributeDeclaration = attributeUse.decl
            if (attributeUse.isRequired) {
                requiredNames.add(attributeDeclaration.name)
            }

            handleAttributeDeclaration("", attributeDeclaration).handleSpecialType()
        }.filter { it.name !in alreadyIncluded }
                .filter { !it.name.startsWith("On") }
                .sortedBy { it.name }

        val name = attributeGroup.name

        if (facadeAttributes.isNotEmpty()) {
            repository.attributeFacades[name] = AttributeFacade(name, facadeAttributes, requiredNames)
            alreadyIncluded.addAll(facadeAttributes.map { it.name })
        }
    }

    schema.modelGroupDecls.values.forEach { modelGroupDeclaration ->
        val name = modelGroupDeclaration.name
        val children = modelGroupDeclaration.modelGroup
                .children
                .map { it.term }
                .filter { it.isElementDecl }
                .map { it.asElementDecl().name }

        val group = TagGroup(name, children)
        repository.tagGroups[name] = group
        children.forEach {
            repository.groupsByTags.getOrPut(it) { ArrayList<TagGroup>() }.add(group)
        }
    }

    schema.elementDecls.values.forEach { elementDeclaration ->
        val name = elementDeclaration.name
        val type = elementDeclaration.type
        val suggestedNames = HashSet<String>()
        globalSuggestedAttributes.get(name)?.let {
            suggestedNames.addAll(it.filter { !it.startsWith("-") })
        }
        val excluded = globalSuggestedAttributes.get(name)?.filter { it.startsWith("-") }?.map { it.removePrefix("-") } ?: emptyList()

        val tagInfo: TagInfo
        if (type.isComplexType) {
            val complex = type.asComplexType()
            val groupDeclarations = complex.attGroups.flatMap { flattenGroups(it) }.distinct().toList()
            val attributeGroups = groupDeclarations.map { repository.attributeFacades[it.name] }.filterNotNull()

            val attributes = complex.declaredAttributeUses.map {
                if (it.isRequired) {
                    suggestedNames.add(it.decl.name)
                }

                handleAttributeDeclaration(name.humanize(), it.decl).handleSpecialType(name)
            }

            val children = HashSet<String>()
            val modelGroupNames = HashSet<String>()
            val contentTerm = complex.contentType?.asParticle()?.term
            val directChildren = ArrayList<String>()
            if (contentTerm != null) {
                flattenTerm(contentTerm, children, modelGroupNames)
                if (contentTerm.isModelGroup) {
                    directChildren.addAll(contentTerm.asModelGroup().children.map { it.term }.filter { it.isElementDecl }.map { it.asElementDecl().name })
                }
            }

            modelGroupNames.addAll(repository.groupsByTags[name]?.map { it.name }.orEmpty())

            suggestedNames.addAll(attributes.filter { it.type == AttributeType.ENUM }.map { it.name })
            suggestedNames.addAll(attributes.filter { it.name in globalSuggestedAttributeNames }.map { it.name })
            suggestedNames.addAll(attributeGroups.flatMap { it.attributes }.filter { it.name in globalSuggestedAttributeNames }.map { it.name })
            suggestedNames.removeAll(excluded)

            tagInfo = TagInfo(name, children.toList().sorted(), directChildren, attributeGroups, attributes, suggestedNames, modelGroupNames.sorted().toList())
        } else {
            throw UnsupportedOperationException()
        }

        repository.tags[name] = tagInfo
    }

    tagUnions(repository)
}

private val xsdToType = mapOf(
        "boolean" to AttributeType.BOOLEAN,
        "string" to AttributeType.STRING,
        "anyURI" to AttributeType.STRING // TODO links
)