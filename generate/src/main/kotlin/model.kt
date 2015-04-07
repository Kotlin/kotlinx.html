package html4k.generate

import java.util.*

object Repository {
    val tags = TreeMap<String, TagInfo>()
    val attributes = TreeMap<String, AttributeInfo>()
    val attributeEnums = TreeMap<String, List<AttributeEnumValue>>()
    val strictEnums = TreeSet<String>()

    [suppress("UNCHECKED_CAST")]
    val attributeDelegateRequests = TreeSet<AttributeRequest> ({a, b ->
        a.type.compareTo(b.type).let { typeComparison ->
            if (typeComparison != 0) typeComparison
            else a.options.size().compareTo(b.options.size()).let { sizeComparison ->
                if (sizeComparison != 0) sizeComparison
                else if (a.options.isEmpty()) 0
                else a.options.indices.map { a.options[it].asValue.compareTo(b.options[it].asValue) }.firstOrNull() { it != 0 } ?: 0
            }
        }
    }) as MutableSet<AttributeRequest>

    val attributeFacades = ArrayList<AttributeFacade>()
    val attributesToFacadesMap = HashMap<String, List<AttributeFacade>>(4096)

    val tagGroups = TreeMap<String, TagGroup>()
}

data class AttributeFacade(val name : String, val attributes : List<String> = emptyList())

data class AttributeEnumValue (
        val realName : String,
        val fieldName : String
)

enum class AttributeType(val classPrefix : String, val typeName : String) {
    STRING : AttributeType("String", "String")
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
    get() = "attribute${typeName}${options.map {it.asFieldPart.capitalize()}.join("")}${toNameHash()}"

data class AttributeInfo(
        val name : String,
        override val type : AttributeType,
        val safeAlias : String,
        val trueFalse : List<String> = listOf(),
        override val enumTypeName : String = ""
) : HasType

val HasType.typeName : String
    get() = if (type == AttributeType.ENUM) enumTypeName else type.typeName

val AttributeInfo.fieldName : String
    get() = if (safeAlias.isNotEmpty()) safeAlias else name

data class TagInfo(
        val name : String,
        val possibleChildren : List<String> = listOf(),
        val attributes : List<String> = listOf(),
        val suggestedAttributes : List<String> = listOf()
)

val TagInfo.safeName : String
    get() = name.escapeUnsafeValues()

val TagInfo.nameUpper : String
    get() = safeName.toUpperCase()

data class TagGroup(
        val name : String,
        val tags : MutableList<String> = ArrayList()
)

private fun Any.toNameHash() = Integer.toHexString(hashCode())