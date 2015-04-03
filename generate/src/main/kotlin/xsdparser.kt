package html4k.generate

import com.sun.xml.xsom.XSAttributeDecl
import com.sun.xml.xsom.XSDeclaration
import com.sun.xml.xsom.XSParticle
import com.sun.xml.xsom.XSTerm
import com.sun.xml.xsom.parser.XSOMParser
import java.util.ArrayList
import java.util.HashSet

val SCHEME_URL = "generate/src/main/resources/html_5.xsd"
val HTML_NAMESPACE = "html-5"

private val attributeNamesMap = mapOf("class" to "classes")

private fun flattenTerm(term : XSTerm, result : MutableCollection<String>) {
    if (term.isElementDecl()) {
        result.add(term.asElementDecl().getName())
    } else if (term.isModelGroupDecl()) {
        term.asModelGroupDecl().getModelGroup().toList().map {it.getTerm()}.forEach {
            flattenTerm(it, result)
        }
    } else if (term.isModelGroup()) {
        term.asModelGroup().toList().map {it.getTerm()}.forEach {
            flattenTerm(it, result)
        }
    }
}

fun handleAttributeDeclaration(attributeDeclaration : XSAttributeDecl) {
    val name = attributeDeclaration.getName()
    val type = attributeDeclaration.getType()

    val safeName = attributeNamesMap[name] ?: name.escapeUnsafeValues()
    val attributeInfo: AttributeInfo
    if (type.isUnion()) {
        val enumEntries = type.asUnion()
                .filter {it.isRestriction()}
                .map {it.asRestriction()}
                .flatMap { it.getDeclaredFacets() ?: emptyList() }
                .filter { it.getName() == "enumeration" }
                .map {it.getValue().value}

        if (enumEntries.isNotEmpty()) {
            Repository.attributeEnums[safeName.capitalize()] = enumEntries.toAttributeValues()
        }

        attributeInfo = AttributeInfo(name, "String", safeName)
    } else if (type.isPrimitive()) {
        attributeInfo = AttributeInfo(name, xsdToType[type.getPrimitiveType().getName()] ?: "String", safeName)
    } else if (type.isRestriction()) {
        val restriction = type.asRestriction()
        val enumEntries = restriction.getDeclaredFacets().filter { it.getName() == "enumeration" }.map { it.getValue().value }

        if (enumEntries.size() == 1 && enumEntries.single() == name) {
            // probably ticker
            attributeInfo = AttributeInfo(name, "Boolean", safeName, trueFalse = listOf(name, ""))
        } else if (enumEntries.size() == 2 && enumEntries.sort() == listOf("off", "on")) {
            attributeInfo = AttributeInfo(name, "Boolean", safeName, trueFalse = listOf("on", "off"))
        } else {
            val enumTypeName = safeName.capitalize()
            Repository.attributeEnums[enumTypeName] = enumEntries.toAttributeValues()
            Repository.strictEnums.add(enumTypeName)

            attributeInfo = AttributeInfo(name, enumTypeName, safeName)
        }
    } else {
        attributeInfo = AttributeInfo(name, "String", safeName)
    }

    Repository.attributes[name] = attributeInfo
}

fun fillRepository() {
    val parser = XSOMParser()
    parser.parse(SCHEME_URL)
    val schema = parser.getResult().getSchema(HTML_NAMESPACE)
    val suggestedNames = HashSet<String>(1024)
    schema.getAttGroupDecls().values().forEach { attributeGroup ->
        attributeGroup.getAttributeUses().forEach { attributeUse ->
            val attributeDeclaration = attributeUse.getDecl()
            if (attributeUse.isRequired()) {
                suggestedNames add attributeDeclaration.getName()
            }

            handleAttributeDeclaration(attributeDeclaration)
        }
    }

    schema.getElementDecls().values().forEach { elementDeclaration ->
        val name = elementDeclaration.getName()
        val type = elementDeclaration.getType()

        val tagInfo : TagInfo
        if (type.isComplexType()) {
            val complex = type.asComplexType()
            val attributeUses = complex.getAttributeUses()
            val attributes = attributeUses.map {it.getDecl().getName()}

            attributeUses.filter { it.getDecl().getName() !in Repository.attributes }.forEach {
                if (it.isRequired()) {
                    suggestedNames add it.getDecl().getName()
                }

                handleAttributeDeclaration(it.getDecl())
            }

            val children = HashSet<String>()
            complex.getContentType().asParticle()?.let { particle ->
                flattenTerm(particle.getTerm(), children)
            }

            tagInfo = TagInfo(name, children.toList().sort(), attributes, attributes.filter {it in suggestedNames} + (globalSuggestedAttributes.get(name) ?: emptyList()))
        } else {
            throw UnsupportedOperationException()
        }

        Repository.tags[name] = tagInfo
    }
}

private val xsdToType = mapOf(
        "boolean" to "Boolean",
        "string" to "String",
        "anyURI" to "String" // TODO links
)