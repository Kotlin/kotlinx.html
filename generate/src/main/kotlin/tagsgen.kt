package html4k.generate

import java.util.ArrayList
import java.util.HashSet
import java.util.LinkedList

fun <O : Appendable> O.tagClass(tag : TagInfo, excludeAttributes : Set<String>) : O = with {
    val parentTraits = tag.attributeGroups.map {it.name.capitalize() + "Facade"}

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
        val lowerCasedNames = tag.attributes.map {it.name.toLowerCase()}.toSet()
        val attributes = tag.attributes.filter {it.name !in excludeAttributes}

        attributes.filter {!isAtrributeExcluded(it.name) }.forEach { attribute ->
            if (attribute.name[0].isLowerCase() || attribute.name.toLowerCase() !in lowerCasedNames) {
                attributeProperty(attribute)
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
    tag.mergeAttributes().filter {it.name in tag.suggestedAttributes}.map { attribute ->
        val name = attribute.fieldName

        val encoded = when (attribute.type) {
            AttributeType.STRING -> "$name"
            AttributeType.BOOLEAN -> "$name?.booleanEncode()"
            AttributeType.ENUM -> "$name?.enumEncode()"
            AttributeType.TICKER -> "$name?.tickerEncode(${attribute.name.quote()})"
            else -> throw UnsupportedOperationException()
        }

        "${attribute.name.quote()} to $encoded"
    }.let { attributeArgs ->
        if (attributeArgs.isEmpty()) "emptyMap()" else attributeArgs.join(",", "listOf(", ").toAttributesMap()")
    }

private fun tagBuilderFunctionArguments(tag: TagInfo, blockOrContent : Boolean = tag.possibleChildren.isNotEmpty()) : ArrayList<Var> {
    val arguments = ArrayList<Var>()

    tag.mergeAttributes().filter {it.name in tag.suggestedAttributes}.forEach { attribute ->
        arguments.add(Var(attribute.fieldName, attribute.typeName + "?", defaultValue = "null"))
    }

    if (blockOrContent) {
        arguments.add(Var("block", "${tag.nameUpper}.() -> Unit"))
    } else {
        arguments.add(Var("content", "String", defaultValue = "\"\""))
    }
    return arguments
}