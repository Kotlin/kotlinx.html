package html4k.generate

import java.util.ArrayList

fun <O : Appendable> O.tagClass(tag : TagInfo) : O = with {
    clazz(Clazz(
            name = tag.safeName.toUpperCase(),
            variables = listOf(
                    Var("initialAttributes", "Map<String, String>", false, false, true),
                    Var("consumer", "TagConsumer<*>", false, true)
            ),
            parents = listOf(
                    "HTMLTag(\"${tag.name}\", consumer, initialAttributes)"
            )
    )) {
        val lowerCasedNames = (tag.attributes + tag.suggestedAttributes).map {it.toLowerCase()}.distinct()

        (tag.attributes + tag.suggestedAttributes).distinct().forEach {
            if (it[0].isLowerCase() || it.toLowerCase() !in lowerCasedNames) {
                tagAttributeVar(Repository.attributes[it])
            }
        }

        emptyLine()

        tag.possibleChildren.forEach {
            tagChildrenMethod(it)
            emptyLine()
        }
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

fun <O : Appendable> O.tagChildrenMethod(children : String) {
    val tag = Repository.tags[children]!!

    val arguments = ArrayList<Var>()

    arguments addAll tag.suggestedAttributes.map { Var(Repository.attributes[it].fieldName, Repository.attributes[it].type + "?") }

    if (tag.possibleChildren.isNotEmpty()) {
        arguments.add(Var("block", "${tag.nameUpper}.() -> Unit"))
    } else {
        arguments.add(Var("content", "String"))
    }

    append("    override\n")
    append("    ")
    function(tag.safeName, arguments, "Unit")

    defineIs("super.${tag.safeName}(" + arguments.map {it.name}.join(", ") + ")")
}

fun <O : Appendable> O.tagAttributeVar(attribute : AttributeInfo) {
    val attributeClass = when (attribute.type) {
        "String" -> "StringAttribute(\"${attribute.name}\")"
        "Boolean" -> "BooleanAttribute(\"${attribute.name}\")"
//        "Ticker" -> "TickerAttribute" // TODO
        else -> "EnumAttribute(\"${attribute.name}\", ${attribute.type.decapitalize()}Values)"
    }

    append("    ")
    variable(Var(attribute.fieldName, attribute.type, true))
    delegateBy(attributeClass)
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
    append("    deprecated(\"you shouldn't use tag ${tag.name} here\")\n")
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
            val type = Repository.attributes[it].type

            val encoded = when (type) {
                "String" -> "$name"
                "Boolean" -> "$name?.booleanEncode()"
                else -> "$name?.enumEncode()"
            }

            "\"$it\" to $encoded"
        }.join(",", "listOf(", ").toAttributesMap()")
    }

private fun tagBuilderFunctionArguments(tag: TagInfo, blockOrContent : Boolean = tag.possibleChildren.isNotEmpty()) : ArrayList<Var> {
    val arguments = ArrayList<Var>()

    tag.suggestedAttributes.forEach {
        val attributeInfo = Repository.attributes[it]
        arguments.add(Var(attributeInfo.fieldName, attributeInfo.type + "?", defaultValue = "null"))
    }

    if (blockOrContent) {
        arguments.add(Var("block", "${tag.nameUpper}.() -> Unit"))
    } else {
        arguments.add(Var("content", "String", defaultValue = "\"\""))
    }
    return arguments
}