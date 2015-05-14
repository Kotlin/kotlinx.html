package html4k.generate

import html4k.generate.humanize.humanize
import java.util.ArrayList
import java.util.TreeMap
import java.util.TreeSet

object Repository {
    val tags = TreeMap<String, TagInfo>()

    @suppress("UNCHECKED_CAST")
    val attributeDelegateRequests = TreeSet<AttributeRequest> ({a, b ->
        a.type.compareTo(b.type).let { typeComparison ->
            if (typeComparison != 0) typeComparison
            else a.enumTypeName.compareTo(b.enumTypeName).let { enumTypeComparison ->
                if (enumTypeComparison != 0) enumTypeComparison
                else a.options.size().compareTo(b.options.size()).let { sizeComparison ->
                    if (sizeComparison != 0) sizeComparison
                    else if (a.options.isEmpty()) 0
                    else a.options.indices.map { a.options[it].asValue.compareTo(b.options[it].asValue) }.firstOrNull() { it != 0 } ?: 0
                }
            }
        }
    }) as MutableSet<AttributeRequest>

    val attributeFacades = TreeMap<String, AttributeFacade>()
    val tagGroups = TreeMap<String, TagGroup>()
}

data class AttributeFacade(val name : String, val attributes : List<AttributeInfo>, val required : Set<String>) {
    val attributeNames = attributes.map {it.name}.toSet()
}

data class AttributeEnumValue (
        val realName : String,
        val fieldName : String
)

enum class AttributeType(val classPrefix : String, val typeName : String) {
    STRING : AttributeType("String", "String")
    STRING_SET : AttributeType("StringSet", "Set<String>")
    BOOLEAN : AttributeType("Boolean", "Boolean")
    TICKER : AttributeType("Ticker", "Boolean")
    ENUM : AttributeType("Enum", "???")
}

trait HasType {
    val type : AttributeType
    val enumTypeName : String
}

data class AttributeRequest(
    override val type : AttributeType,
    override val enumTypeName : String,
    val options : List<Const<*>> = emptyList()
) : HasType

val AttributeRequest.delegatePropertyName : String
    get() = "attribute${typeName.humanize().capitalize()}${options.map {it.asFieldPart.humanize().capitalize()}.join("")}_${toNameHash()}"

data class AttributeInfo(
        val name : String,
        override val type : AttributeType,
        val safeAlias : String,
        val trueFalse : List<String> = listOf(),
        override val enumTypeName : String = "",
        val required : Boolean = false,
        val enumValues : List<AttributeEnumValue> = emptyList()
) : HasType

val HasType.typeName : String
    get() = if (type == AttributeType.ENUM) enumTypeName else type.typeName

val AttributeInfo.fieldName : String
    get() = if (safeAlias.isEmpty()) name else safeAlias

fun String.isLowerCase() = this.toLowerCase() == this

data class TagInfo(
        val name : String,
        val possibleChildren : List<String>,
        val directChildren : List<String>,
        val attributeGroups : List<AttributeFacade>,
        val attributes : List<AttributeInfo>,
        val suggestedAttributes : Set<String>,
        val tagGroupNames: List<String>
)

fun TagInfo.mergeAttributes() = attributes + attributeGroups.flatMap { it.attributes }

val TagInfo.safeName : String
    get() = name.escapeUnsafeValues()

val TagInfo.nameUpper : String
    get() = safeName.toUpperCase()

data class TagGroup(
        val name : String,
        val tags : List<String>
)

private fun Any.toNameHash() = Integer.toHexString(hashCode())