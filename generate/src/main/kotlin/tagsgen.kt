package kotlinx.html.generate

import kotlinx.html.generate.humanize.*
import java.util.*

fun <O : Appendable> O.tagClass(tag : TagInfo, excludeAttributes : Set<String>) : O = with {
    val parentAttributeIfaces = tag.attributeGroups.map {it.name.capitalize() + "Facade"}
    val parentElementIfaces = tag.tagGroupNames.map {it.escapeUnsafeValues().capitalize()}
    val allParentIfaces = parentAttributeIfaces + parentElementIfaces
    val betterParentIfaces = humanizeJoin(allParentIfaces)

    val namespace = tagNamespaces[tag.name.toLowerCase()]

    appendln("@Suppress(\"unused\")")
    clazz(Clazz(
            name = tag.safeName.toUpperCase(),
            variables = listOf(
                    Var("initialAttributes", "Map<String, String>", false, false, true),
                    Var("consumer", "TagConsumer<*>", false, true)
            ),
            parents = listOf(
                    buildString {
                        functionCall("HTMLTag", listOf(
                                tag.name.quote(),
                                "consumer",
                                "initialAttributes",
                                namespace?.quote().toString(),
                                (tag.name in inlineTags).toString(),
                                (tag.name in emptyTags).toString()
                        ))
                    }
            ) + when {
                allParentIfaces.isNotEmpty() -> listOf(betterParentIfaces).map { renames[it] ?: it }
                else -> emptyList<String>()
            },
            isOpen = true
    )) {
        val lowerCasedNames = tag.attributes.map {it.name.toLowerCase()}.toSet()
        val attributes = tag.attributes.filter {it.name !in excludeAttributes}

        attributes.filter {!isAttributeExcluded(it.name) }.forEach { attribute ->
            if (attribute.name[0].isLowerCase() || attribute.name.toLowerCase() !in lowerCasedNames) {
                attributeProperty(attribute)
            }
        }

        fun contentlessTagDeprecation() {
            indent()
            appendln("@Deprecated(\"This tag most likely doesn't support text content or requires unsafe content (try unsafe {}\")")
        }

        if (tag.name.toLowerCase() in contentlessTags) {
            contentlessTagDeprecation()

            indent()
            function("unaryPlus", modifiers = listOf("override", "operator"), receiver = "Entities")
            block {
                indent(2)
                append("@Suppress(\"DEPRECATION\") ")
                functionCall("entity", listOf("this"))
                appendln()
                indent()
            }

            appendln()
            contentlessTagDeprecation()

            indent()
            function("unaryPlus", modifiers = listOf("override", "operator"), receiver = "String")
            block {
                indent(2)
                append("@Suppress(\"DEPRECATION\") ")
                functionCall("text", listOf("this"))
                appendln()
                indent()
            }

            appendln()
            contentlessTagDeprecation()

            indent()
            function("text", arguments = listOf(Var("s", "String")), modifiers = listOf("override"))
            block {
                indent(2)
                receiverDot("super<HTMLTag>")
                functionCall("text", listOf("s"))
                appendln()
                indent()
            }

            appendln()
            contentlessTagDeprecation()

            indent()
            function("text", arguments = listOf(Var("n", "Number")), modifiers = listOf("override"))
            block {
                indent(2)
                receiverDot("super<HTMLTag>")
                functionCall("text", listOf("n"))
                appendln()
                indent()
            }

            appendln()
            contentlessTagDeprecation()

            indent()
            function("entity", arguments = listOf(Var("e", "Entities")), modifiers = listOf("override"))
            block {
                indent(2)
                receiverDot("super<HTMLTag>")
                functionCall("entity", listOf("e"))
                appendln()
                indent()
            }
        }

        emptyLine()
    }

    tag.directChildren.map {Repository.tags[it]}.filterNotNull().filterIgnored().forEach { children ->
        htmlTagBuilders(tag.safeName.toUpperCase(), children)
    }

    if (parentElementIfaces.size > 1) {
        val commons = tag.tagGroupNames.map {Repository.tagGroups[it]?.tags?.toSet()}.filterNotNull().reduce { a, b -> a.intersect(b) }
        if (commons.isNotEmpty()) {
            parentElementIfaces.forEach { group ->
                variable(Var(name = "as" + group.escapeUnsafeValues().capitalize(), type = group.escapeUnsafeValues().capitalize()), receiver = tag.safeName.toUpperCase())
                appendln()
                getter()
                defineIs("this")
                emptyLine()
            }
        }
    }

    emptyLine()
}

internal fun <O : Appendable> O.tagAttributeVar(attribute: AttributeInfo, receiver: String?, indent: Int = 1): AttributeRequest {
    val options = LinkedList<Const<*>>()

    if (attribute.type == AttributeType.ENUM) {
        options.add(ReferenceConst(attribute.enumTypeName.decapitalize() + "Values"))
    } else if (attribute.type == AttributeType.BOOLEAN && attribute.trueFalse.isNotEmpty()) {
        options.addAll(attribute.trueFalse.map(::StringConst))
    }

    val attributeRequest = AttributeRequest(attribute.type, if (attribute.type == AttributeType.ENUM) attribute.enumTypeName else "", options)
    Repository.attributeDelegateRequests.add(attributeRequest)

    indent(indent)
    variable(Var(attribute.fieldName, attribute.typeName, true), receiver = receiver ?: "")
    return attributeRequest
}

fun probeType(htmlClassName : String) : Boolean = htmlClassName in knownTagClasses

private fun tagCandidates(tag : TagInfo) = (listOf(tag.safeName) + replacements.map { tag.safeName.replace(it.first.toRegex(), it.second) }).flatMap { listOf(it.capitalize(), it.toUpperCase()) }.distinct()

fun getTagResultClass(tag: TagInfo) =
        tagCandidates(tag)
                .map { "HTML${it}Element" }
                .firstOrNull { probeType(it) } ?: "HTMLElement"

fun contentArgumentValue(tag : TagInfo, blockOrContent : Boolean) = when {
    tag.name.toLowerCase() in emptyTags -> "block"
    blockOrContent -> "block"
    else -> "{+content}"
}

fun <O : Appendable> O.consumerBuilderJS(tag : TagInfo, blockOrContent : Boolean) {
    val resultType = getTagResultClass(tag)

    append("public ")
    function(tag.safeName, tagBuilderFunctionArguments(tag, blockOrContent), resultType, receiver = "TagConsumer<HTMLElement>")
    defineIs(buildString {
        functionCall(tag.nameUpper, listOf(
                buildSuggestedAttributesArgument(tag),
                "this"
        ))
        append(".")
        functionCall("visitAndFinalize", listOf(
                "this",
                contentArgumentValue(tag, blockOrContent)
        ))

        if (resultType != "HTMLElement") {
            append(" as ")
            append(resultType)
        }
    })
}

fun <O : Appendable> O.consumerBuilderShared(tag : TagInfo, blockOrContent : Boolean) {
    function(tag.safeName, tagBuilderFunctionArguments(tag, blockOrContent), "T", listOf("T", "C : TagConsumer<T>"), "C")
    defineIs(buildString {
        functionCall(tag.nameUpper, listOf(
                buildSuggestedAttributesArgument(tag),
                "this"
        ))
        append(".")
        functionCall("visitAndFinalize", listOf(
                "this",
                contentArgumentValue(tag, blockOrContent)
        ))
    })
}

fun <O : Appendable> O.htmlTagBuilderMethod(receiver : String, tag : TagInfo, blockOrContent : Boolean) {
    val arguments = tagBuilderFunctionArguments(tag, blockOrContent)

    function(tag.safeName, arguments, "Unit", receiver = receiver)
    defineIs(buildString {
        functionCall(tag.nameUpper, listOf(
                buildSuggestedAttributesArgument(tag),
                "consumer"
        ))
        append(".")
        functionCall("visit", listOf(contentArgumentValue(tag, blockOrContent)))
    })
}

fun <O : Appendable> O.htmlTagEnumBuilderMethod(receiver : String, tag : TagInfo, blockOrContent : Boolean, enumAttribute : AttributeInfo, indent : Int) {
    require(enumAttribute.enumValues.isNotEmpty())

    val arguments = tagBuilderFunctionArguments(tag, blockOrContent).filter {it.name != enumAttribute.fieldName}

    enumAttribute.enumValues.forEach { enumValue ->
        indent(indent)
        function(enumValue.fieldName + tag.safeName.capitalize(), arguments, "Unit", receiver = receiver)
        defineIs(buildString {
            functionCall(tag.nameUpper, listOf(
                    buildSuggestedAttributesArgument(tag, mapOf(enumAttribute.fieldName to enumAttribute.typeName + "." + enumValue.fieldName + ".realValue")),
                    "consumer"
            ))
            append(".")
            functionCall("visit", listOf(contentArgumentValue(tag, blockOrContent)))
        })
    }
}

fun <O : Appendable> O.indent(stops : Int = 1) {
    for (i in 0..stops - 1) {
        append("    ")
    }
}

private fun buildSuggestedAttributesArgument(tag: TagInfo, predefinedValues : Map<String, String> = emptyMap()) : String =
    tag.mergeAttributes().filter {it.name in tag.suggestedAttributes}.map { attribute ->
        val name = attribute.fieldName

        val encoded = if (name in predefinedValues) predefinedValues[name] else when (attribute.type) {
            AttributeType.STRING -> "$name"
            AttributeType.BOOLEAN -> "$name?.booleanEncode()"
            AttributeType.ENUM -> "$name?.enumEncode()"
            AttributeType.TICKER -> "$name?.tickerEncode(${attribute.name.quote()})"
            AttributeType.STRING_SET -> name
        }

        "${attribute.name.quote()}, $encoded"
    }.let { attributeArgs ->
        when (attributeArgs.size) {
            0 -> "emptyMap"
            else -> attributeArgs.joinToString(",", "attributesMapOf(", ")")
        }
    }

private fun tagBuilderFunctionArguments(tag: TagInfo, blockOrContent : Boolean) : ArrayList<Var> {
    val arguments = ArrayList<Var>()

    tag.mergeAttributes().filter {it.name in tag.suggestedAttributes}.forEach { attribute ->
        val type = when (attribute.type) {
            AttributeType.STRING_SET -> "String"
            else -> attribute.typeName
        }
        arguments.add(Var(attribute.fieldName, type + "?", defaultValue = "null"))
    }

    when {
        tag.name.toLowerCase() in emptyTags -> arguments.add(Var("block", "${tag.nameUpper}.() -> Unit", defaultValue = "{}"))
        blockOrContent -> arguments.add(Var("block", "${tag.nameUpper}.() -> Unit", defaultValue = "{}"))
        else -> arguments.add(Var("content", "String", defaultValue = "\"\""))
    }

    return arguments
}

private val inlineTags = """a
abbr
acronym
area
b
base
basefont
bdi
bdo
bgsound
big
br
button
cite
code
command
data
datalist
device
dfn
em
embed
font
i
iframe
img
input
kbd
keygen
label
legend
map
mark
menuitem
meter
object
optgroup
option
output
param
progress
q
rp
rt
ruby
samp
select
small
source
span
strong
sub
summary
sup
textarea
time
track
tt
u
var
wbr""".lines().toSet()
