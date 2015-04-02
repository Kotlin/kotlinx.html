package html4k.generate

import java.util.HashMap

object Repository {
    val tags = HashMap<String, TagInfo>()
    val attributes = HashMap<String, AttributeInfo>()
}

data class AttributeInfo(
        val name : String,
        val type : String = "String",
        val safeAlias : String = ""
)

val AttributeInfo.fieldName : String
    get() = if (safeAlias.isNotEmpty()) safeAlias else name

data class TagInfo(
        val name : String,
        val possibleChildren : List<String> = listOf(),
        val attributes : List<String> = listOf(),
        val suggestedAttributes : List<String> = listOf()
)

val TagInfo.nameUpper : String
    get() = name.toUpperCase()