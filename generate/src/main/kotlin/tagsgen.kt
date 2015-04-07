package html4k.generate

import java.util.ArrayList
import java.util.HashSet
import java.util.LinkedList

fun <O : Appendable> O.tagClass(tag : TagInfo, excludeAttributes : Set<String>) : O = with {
    val parentTraits = tag.attributes.fold(HashSet<AttributeFacade>()) { traits, attribute -> traits.addAll(Repository.attributesToFacadesMap[attribute] ?: emptyList()); traits }.map {it.name.capitalize() + "Facade"}

    clazz(Clazz(
            name = tag.safeName.toUpperCase(),
            variables = listOf(
                    Var("initialAttributes", "Map<String, String>", false, false, true),
                    Var("consumer", "TagConsumer<*>", false, true)
            ),
            parents = listOf(
                    "HTMLTag(\"${tag.name}\", consumer, initialAttributes)"
            ) + parentTraits
    )) {
        val lowerCasedNames = (tag.attributes + tag.suggestedAttributes).map {it.toLowerCase()}.distinct()
        val attributes = (tag.attributes + tag.suggestedAttributes).distinct().filter {it !in excludeAttributes}

        attributes.forEach {
            if (it[0].isLowerCase() || it.toLowerCase() !in lowerCasedNames) {
                if (it !in Repository.attributesToFacadesMap) {
                    attributeProperty(it)
                }
            }
        }

        emptyLine()
    }

    emptyLine()
}

fun <O : Appendable> O.builderFunction(tag : TagInfo) : O = with {
    function("build${tag.nameUpper}", listOf(
            Var("attributes", "Map<String, String>"),
            Var("consumer", "TagConsumer<*>"),
            Var("block", "${tag.nameUpper}.() -> Unit")
    ), "Unit")

    defineIs("${tag.nameUpper}(attributes, consumer).visit(block)")
}

private fun <O : Appendable> O.tagAttributeVar(attribute: AttributeInfo): AttributeRequest {
    val options = LinkedList<Const<*>>()

    if (attribute.type == AttributeType.ENUM) {
        options.add(ReferenceConst(attribute.enumTypeName.decapitalize() + "Values"))
    } else if (attribute.type == AttributeType.BOOLEAN && attribute.trueFalse.isNotEmpty()) {
        options.addAll(attribute.trueFalse.map { StringConst(it) })
    }

    val attributeRequest = AttributeRequest(attribute.type, attribute.enumTypeName, options)
    Repository.attributeDelegateRequests.add(attributeRequest)

    append("    ")
    variable(Var(attribute.fieldName, attribute.typeName, true))
    return attributeRequest
}

fun <O : Appendable> O.consumerBuilder(tag : TagInfo, blockOrContent : Boolean) {
    append("public ")
    function(tag.safeName, tagBuilderFunctionArguments(tag, blockOrContent), "T", listOf("T", "C : TagConsumer<T>"), "C")
    defineIs(StringBuilder {
        functionCall("build", listOf(
                buildSuggestedAttributesArgument(tag),
                "::build${tag.nameUpper}",
                if (blockOrContent) "block" else "{+content}"
        ))
        append(".finalize()")
    })
}

fun <O : Appendable> O.htmlTagBuilderMethod(tag : TagInfo, blockOrContent : Boolean) {
    append("    open ")

    val arguments = tagBuilderFunctionArguments(tag, blockOrContent)

    val delegateArguments = ArrayList<String>()

    delegateArguments.add(buildSuggestedAttributesArgument(tag))

    delegateArguments.add("consumer")
    if (blockOrContent) {
        delegateArguments.add("block")
    } else {
        delegateArguments.add("{+content}")
    }

    function(tag.safeName, arguments, "Unit")
    defineIs("build${tag.nameUpper}" + delegateArguments.join(", ", "(", ")"))
}

private fun buildSuggestedAttributesArgument(tag: TagInfo) : String =
    if (tag.suggestedAttributes.isEmpty()) {
        "emptyMap()"
    } else {
        tag.suggestedAttributes.map {
            val name = Repository.attributes[it].fieldName

            val encoded = when (Repository.attributes[it].type) {
                AttributeType.STRING -> "$name"
                AttributeType.BOOLEAN -> "$name?.booleanEncode()"
                AttributeType.ENUM -> "$name?.enumEncode()"
                AttributeType.TICKER -> "$name?.tickerEncode(${it.quote()})"
                else -> throw UnsupportedOperationException()
            }

            "\"$it\" to $encoded"
        }.join(",", "listOf(", ").toAttributesMap()")
    }

private fun tagBuilderFunctionArguments(tag: TagInfo, blockOrContent : Boolean = tag.possibleChildren.isNotEmpty()) : ArrayList<Var> {
    val arguments = ArrayList<Var>()

    tag.suggestedAttributes.forEach {
        val attributeInfo = Repository.attributes[it]
        arguments.add(Var(attributeInfo.fieldName, attributeInfo.typeName + "?", defaultValue = "null"))
    }

    if (blockOrContent) {
        arguments.add(Var("block", "${tag.nameUpper}.() -> Unit"))
    } else {
        arguments.add(Var("content", "String", defaultValue = "\"\""))
    }
    return arguments
}