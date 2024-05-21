package kotlinx.html.generate

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LambdaTypeName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeVariableName
import com.squareup.kotlinpoet.asTypeName
import java.util.*

private const val BLOCK_LAMBDA = "block"
private const val CONTENT_LAMBDA = "{+content}"

fun Appendable.tagClass(repository: Repository, tag: TagInfo, excludeAttributes: Set<String>) {
    val parentAttributeIfaces = tag.attributeGroups.map { it.className }
    val parentElementIfaces = tag.tagGroupNames.map { it.humanize().capitalize() }
    val allParentIfaces = parentAttributeIfaces + parentElementIfaces
    val betterParentIfaces = humanizeJoin(allParentIfaces)

    val namespace = tagNamespaces[tag.name.lowercase()]
    val customizableNamespace = tag.name.lowercase() in tagsWithCustomizableNamespace

    val parameters = buildList {
        add(
            Var(
                name = "initialAttributes",
                type = "Map<String, String>",
                varType = VarType.IMMUTABLE,
                override = false,
                forceOmitValVar = true
            )
        )
        add(Var(name = "consumer", type = "TagConsumer<*>", varType = VarType.IMMUTABLE, override = true))
        if (customizableNamespace) {
            add(
                Var(
                    name = "namespace",
                    type = "String?",
                    varType = VarType.IMMUTABLE,
                    override = false,
                    forceOmitValVar = true,
                    defaultValue = namespace?.quote() ?: "null"
                )
            )
        }
    }
    val superConstructorArguments = listOf(
        tag.name.quote(),
        "consumer",
        "initialAttributes",
        if (customizableNamespace) "namespace" else namespace?.quote().toString(),
        (tag.name in inlineTags).toString(),
        (tag.name in emptyTags).toString()
    )

    appendLine("@Suppress(\"unused\")")
    clazz(Clazz(
        name = tag.className,
        variables = parameters,
        parents = listOf(
            buildString {
                functionCall("HTMLTag", superConstructorArguments)
            }
        ) + when {
            allParentIfaces.isNotEmpty() -> listOf(betterParentIfaces).map { renames[it] ?: it }
            else -> emptyList()
        },
        isOpen = true
    )) {
        val lowerCasedNames = tag.attributes.map { it.name.lowercase() }.toSet()
        val attributes = tag.attributes.filter { it.name !in excludeAttributes }

        attributes.filter { !isAttributeExcluded(it.name) }.forEach { attribute ->
            if (attribute.name[0].isLowerCase() || attribute.name.lowercase() !in lowerCasedNames) {
                attributeProperty(repository, attribute)
            }
        }

        fun contentlessTagDeprecation() {
            indent()
            appendLine("@Suppress(\"DeprecatedCallableAddReplaceWith\")")
            indent()
            appendLine("@Deprecated(\"This tag most likely doesn't support text content or requires unsafe content (try unsafe {})\")")
        }

        if (tag.name.lowercase() in contentlessTags) {
            contentlessTagDeprecation()

            indent()
            function("unaryPlus", modifiers = listOf("override", "operator"), receiver = "Entities")
            block {
                indent(2)
                append("@Suppress(\"DEPRECATION\") ")
                functionCall("entity", listOf("this"))
                appendLine()
                indent()
            }

            appendLine()
            contentlessTagDeprecation()

            indent()
            function("unaryPlus", modifiers = listOf("override", "operator"), receiver = "String")
            block {
                indent(2)
                append("@Suppress(\"DEPRECATION\") ")
                functionCall("text", listOf("this"))
                appendLine()
                indent()
            }

            appendLine()
            contentlessTagDeprecation()

            indent()
            function("text", arguments = listOf(Var("s", "String")), modifiers = listOf("override"))
            block {
                indent(2)
                receiverDot("super<HTMLTag>")
                functionCall("text", listOf("s"))
                appendLine()
                indent()
            }

            appendLine()
            contentlessTagDeprecation()

            indent()
            function("text", arguments = listOf(Var("n", "Number")), modifiers = listOf("override"))
            block {
                indent(2)
                receiverDot("super<HTMLTag>")
                functionCall("text", listOf("n"))
                appendLine()
                indent()
            }

            appendLine()
            contentlessTagDeprecation()

            indent()
            function("entity", arguments = listOf(Var("e", "Entities")), modifiers = listOf("override"))
            block {
                indent(2)
                receiverDot("super<HTMLTag>")
                functionCall("entity", listOf("e"))
                appendLine()
                indent()
            }
        }

        emptyLine()
    }

    tag.directChildren.mapNotNull { repository.tags[it] }.filterIgnored().forEach { children ->
        htmlTagBuilders(tag.className, children)
    }

    if (parentElementIfaces.size > 1) {
        val commons = tag.tagGroupNames.mapNotNull { repository.tagGroups[it]?.tags?.toSet() }
            .reduce { a, b -> a.intersect(b) }
        if (commons.isNotEmpty()) {
            parentElementIfaces.forEach { group ->
                variable(Var(name = "as$group", type = group), receiver = tag.className)
                appendLine()
                getter()
                defineIs("this")
                emptyLine()
            }
        }
    }

    emptyLine()
}

internal fun Appendable.tagAttributeVar(
    repository: Repository, attribute: AttributeInfo,
    receiver: String?,
    indent: Int = 1,
): AttributeRequest {
    val options = LinkedList<Const<*>>()

    if (attribute.type == AttributeType.ENUM) {
        options.add(ReferenceConst(attribute.enumTypeName.decapitalize() + "Values"))
    } else if (attribute.type == AttributeType.BOOLEAN && attribute.trueFalse.isNotEmpty()) {
        options.addAll(attribute.trueFalse.map(::StringConst))
    }

    val attributeRequest = AttributeRequest(
        attribute.type,
        if (attribute.type == AttributeType.ENUM) attribute.enumTypeName else "",
        attribute.poetEnumTypeName,
        options
    )
    repository.attributeDelegateRequests.add(attributeRequest)

    indent(indent)
    variable(
        Var(name = attribute.fieldName, type = attribute.typeName, varType = VarType.MUTABLE),
        receiver = receiver ?: "",
    )
    return attributeRequest
}

private fun tagCandidates(tag: TagInfo) = (listOf(tag.memberName) + tagReplacements.map {
    tag.memberName.replace(
        it.first.toRegex(),
        it.second
    )
}).flatMap { listOf(it.capitalize(), it.uppercase()) }.distinct()

fun getTagResultClass(tag: TagInfo): String? =
    tagCandidates(tag)
        .map { "HTML${it}Element" }
        .firstOrNull { it in knownTagClasses }

fun tagConsumerJs(parameter: String): TypeName =
    ClassName("kotlinx.html", "TagConsumer")
        .parameterizedBy(ClassName("org.w3c.dom", parameter))

fun tagConsumer(parameter: TypeName): TypeName =
    ClassName("kotlinx.html", "TagConsumer")
        .parameterizedBy(parameter)

fun FunSpec.Builder.addSuppressAnnotation(vararg suppress: String) =
    addAnnotation(
        AnnotationSpec
            .builder(Suppress::class).apply { suppress.forEach { addMember("%S", it) } }
            .build()
    )

fun FunSpec.Builder.addDeprecatedAnnotation(reason: String) =
    addAnnotation(
        AnnotationSpec
            .builder(Deprecated::class)
            .addMember("%S", reason)
            .build()
    )


fun consumerBuilderJsPoet(
    tag: TagInfo,
    blockOrContent: Boolean,
    defaultTagConsumer: String,
): FunSpec {
    val constructorArgs = listOfNotNull(
        buildSuggestedAttributesArgument(tag),
        "this",
        "namespace".takeIf { tag.name.lowercase() in tagsWithCustomizableNamespace }
    )

    val tagResultClass = getTagResultClass(tag)
    val cast = if (tagResultClass != null) " as $tagResultClass" else ""
    val tagClass = ClassName("kotlinx.html", tag.className)
    val tagBuilderCouldBeInline = tagBuilderCouldBeInline(tag, blockOrContent)
    return FunSpec
        .builder(tag.memberName)
        .returns(ClassName("org.w3c.dom", tagResultClass ?: defaultTagConsumer))
        .addModifiers(KModifier.PUBLIC)
        .receiver(tagConsumerJs(defaultTagConsumer))
        .addKdoc(tag)
        .addAnnotation(ClassName("kotlinx.html", "HtmlTagMarker"))
        .apply {
            if (tagBuilderCouldBeInline) {
                addAnnotation(
                    AnnotationSpec.builder(ClassName("", "OptIn"))
                        .addMember("ExperimentalContracts::class")
                        .build()
                )
            }
        }
        .addTagBuilderFunctionArguments(tag, tagClass, blockOrContent)
        .apply {
            if (tagBuilderCouldBeInline) {
                addModifiers(KModifier.INLINE)
                addCode(
                    """
                        |contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
                        |return %T(${constructorArgs.joinToString(", ")})
                        |    .visitAndFinalize(this, $BLOCK_LAMBDA) $cast
                    """.trimMargin(),
                    tagClass,
                )
            } else {
                addCode(
                    """
                        |return %T(${constructorArgs.joinToString(", ")})
                        |    .visitAndFinalize(this, $CONTENT_LAMBDA) $cast
                    """.trimMargin(),
                    tagClass,
                )
            }
        }
        .build()
}

fun consumerBuilderSharedPoet(
    tag: TagInfo,
    blockOrContent: Boolean,
): FunSpec {
    val constructorArgs = listOfNotNull(
        buildSuggestedAttributesArgument(tag),
        "this",
        "namespace".takeIf { tag.name.lowercase() in tagsWithCustomizableNamespace }
    )

    val tagClass = ClassName("kotlinx.html", tag.className)
    val typeVariableT = TypeVariableName("T")
    val typeVariableC = TypeVariableName("C", tagConsumer(typeVariableT))
    val tagBuilderCouldBeInline = tagBuilderCouldBeInline(tag, blockOrContent)
    return FunSpec
        .builder(tag.memberName)
        .returns(typeVariableT)
        .addTypeVariable(typeVariableT)
        .addTypeVariable(typeVariableC)
        .receiver(typeVariableC)
        .addKdoc(tag)
        .addAnnotation(ClassName("kotlinx.html", "HtmlTagMarker"))
        .apply {
            if (tagBuilderCouldBeInline) {
                addAnnotation(
                    AnnotationSpec.builder(ClassName("", "OptIn"))
                        .addMember("ExperimentalContracts::class")
                        .build()
                )
            }
        }
        .addTagBuilderFunctionArguments(tag, tagClass, blockOrContent)
        .apply {
            if (tagBuilderCouldBeInline) {
                addModifiers(KModifier.INLINE)
                addCode(
                    """
                        |contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
                        |return %T(${constructorArgs.joinToString(", ")})
                        |    .visitAndFinalize(this, $BLOCK_LAMBDA)
                    """.trimMargin(),
                    tagClass,
                )
            } else {
                addCode(
                    """
                    |return %T(${constructorArgs.joinToString(", ")})
                    |    .visitAndFinalize(this, $CONTENT_LAMBDA)
                    """.trimMargin(),
                    tagClass,
                )
            }
        }
        .build()
}

private fun FunSpec.Builder.addKdoc(tag: TagInfo): FunSpec.Builder {
    val kdoc = tag.kdoc?.description ?: return this
    addKdoc("%L", kdoc)
    return this
}

fun Appendable.htmlTagBuilderMethod(receiver: String, tag: TagInfo, blockOrContent: Boolean) {
    val arguments = tagBuilderFunctionArguments(tag, blockOrContent)

    val constructorArgs = buildList {
        add(buildSuggestedAttributesArgument(tag))
        add("consumer")
        if (tag.name.lowercase() in tagsWithCustomizableNamespace) add("namespace")
    }

    tagKdoc(tag)
    htmlDslMarker()
    val tagBuilderCouldBeInline = tagBuilderCouldBeInline(tag, blockOrContent)
    if (tagBuilderCouldBeInline) {
        experimentalContractsMarker()
        append("inline ")
    }
    function(tag.memberName, arguments, "Unit", receiver = receiver)
    tagBody(tagBuilderCouldBeInline, tag, constructorArgs)
}

fun Appendable.htmlTagEnumBuilderMethod(
    receiver: String,
    tag: TagInfo,
    blockOrContent: Boolean,
    enumAttribute: AttributeInfo,
    indent: Int,
) {
    require(enumAttribute.enumValues.isNotEmpty())

    val arguments = tagBuilderFunctionArguments(tag, blockOrContent).filter { it.name != enumAttribute.fieldName }

    enumAttribute.enumValues.forEach { enumValue ->
        val deprecation = findEnumDeprecation(enumAttribute, enumValue)

        val constructorArgs = buildList {
            add(
                buildSuggestedAttributesArgument(
                    tag,
                    mapOf(enumAttribute.fieldName to enumAttribute.typeName + "." + enumValue.fieldName + ".realValue")
                )
            )
            add("consumer")

            if (tag.name.lowercase() in tagsWithCustomizableNamespace) add("namespace")
        }

        if (deprecation != null) {
            indent(indent)
            suppress("DEPRECATION")
        }

        indent(indent)
        htmlDslMarker()
        val tagBuilderCouldBeInline = tagBuilderCouldBeInline(tag, blockOrContent)
        if (tagBuilderCouldBeInline) {
            experimentalContractsMarker()
            append("inline ")
        }
        function(enumValue.fieldName + tag.memberName.capitalize(), arguments, "Unit", receiver = receiver)
        tagBody(tagBuilderCouldBeInline, tag, constructorArgs)
    }
}

fun Appendable.indent(stops: Int = 1) {
    for (i in 0 until stops) {
        append("    ")
    }
}

private fun buildSuggestedAttributesArgument(tag: TagInfo, predefinedValues: Map<String, String> = emptyMap()): String =
    tag.mergeAttributes().filter { it.name in tag.suggestedAttributes }.map { attribute ->
        val name = attribute.fieldName

        val encoded = if (name in predefinedValues) predefinedValues[name] else when (attribute.type) {
            AttributeType.STRING -> name
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

private fun tagBuilderCouldBeInline(tag: TagInfo, blockOrContent: Boolean) =
    blockOrContent || tag.name.lowercase() in emptyTags

private fun FunSpec.Builder.addTagBuilderFunctionArguments(
    tag: TagInfo,
    tagClass: ClassName,
    blockOrContent: Boolean,
): FunSpec.Builder {
    val customizableNamespace = tag.name.lowercase() in tagsWithCustomizableNamespace
    val defaultNamespace: String = tagNamespaces[tag.name.lowercase()]?.quote().toString()

    val attributeParameters =
        tag.mergeAttributes().filter { it.name in tag.suggestedAttributes }.map { attribute ->
            val type = when (attribute.type) {
                AttributeType.STRING_SET -> String::class.asTypeName()
                else -> attribute.poetTypeName
            }

            ParameterSpec.builder(attribute.fieldName, type.copy(nullable = true))
                .defaultValue("null")
                .build()
        }

    val namespaceParameter =
        if (customizableNamespace) {
            ParameterSpec.builder("namespace", String::class.asTypeName().copy(nullable = true))
                .defaultValue(defaultNamespace)
                .build()
        } else {
            null
        }

    val blockParameter =
        ParameterSpec.builder(
            "block",
            LambdaTypeName.get(receiver = tagClass, returnType = Unit::class.asTypeName()),
            KModifier.CROSSINLINE
        )
            .defaultValue("{}")
            .build()

    val contentParameter =
        ParameterSpec.builder("content", String::class.asTypeName())
            .defaultValue("\"\"")
            .build()

    val isEmptyTag = tag.name.lowercase() in emptyTags
    val additionalParameters =
        when {
            isEmptyTag || blockOrContent -> listOfNotNull(namespaceParameter, blockParameter)

            else -> listOfNotNull(contentParameter, namespaceParameter)
        }

    addParameters(attributeParameters)
    addParameters(additionalParameters)

    return this
}

private fun tagBuilderFunctionArguments(tag: TagInfo, blockOrContent: Boolean): ArrayList<Var> {
    val arguments = ArrayList<Var>()
    val customizableNamespace = tag.name.lowercase() in tagsWithCustomizableNamespace
    val defaultNamespace: String = tagNamespaces[tag.name.lowercase()]?.quote().toString()

    tag.mergeAttributes().filter { it.name in tag.suggestedAttributes }.forEach { attribute ->
        val type = when (attribute.type) {
            AttributeType.STRING_SET -> "String"
            else -> attribute.typeName
        }
        arguments.add(Var(attribute.fieldName, "$type?", defaultValue = "null"))
    }

    fun addNamespaceParameter() {
        if (customizableNamespace) {
            arguments.add(
                Var(
                    name = "namespace",
                    type = "String?",
                    varType = VarType.IMMUTABLE,
                    override = false,
                    forceOmitValVar = true,
                    defaultValue = defaultNamespace
                )
            )
        }
    }

    fun addBlockParameter() {
        arguments.add(Var("crossinline block", "${tag.className}.() -> Unit", defaultValue = "{}"))
    }

    fun addContentParameter() {
        arguments.add(Var("content", "String", defaultValue = "\"\""))
    }

    when {
        tag.name.lowercase() in emptyTags || blockOrContent -> {
            addNamespaceParameter()
            addBlockParameter()
        }

        else -> {
            addContentParameter()
            addNamespaceParameter()
        }
    }

    return arguments
}

private fun Appendable.htmlDslMarker() {
    appendLine("@HtmlTagMarker")
}

private fun Appendable.experimentalContractsMarker() {
    appendLine("@OptIn(ExperimentalContracts::class)")
}

private fun Appendable.tagKdoc(tag: TagInfo) {
    val kdoc = tag.kdoc ?: return
    appendLine("/**")
    appendLine(" * ${kdoc.description}")
    appendLine(" */")
}

private fun Appendable.tagBody(tagBuilderCouldBeInline: Boolean, tag: TagInfo, constructorArgs: List<String>) {
    if (tagBuilderCouldBeInline) {
        block {
            indent()
            appendLine("contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }")
            indent()
            functionCall(tag.className, constructorArgs)
            append(".")
            functionCall("visit", listOf(BLOCK_LAMBDA))
            appendLine()
        }
    } else {
        defineIs(
            buildString {
                functionCall(tag.className, constructorArgs)
                append(".")
                functionCall("visit", listOf(CONTENT_LAMBDA))
            }
        )
    }
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
