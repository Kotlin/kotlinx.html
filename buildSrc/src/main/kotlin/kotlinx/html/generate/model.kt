package kotlinx.html.generate

import java.util.*
import kotlin.collections.HashMap

object Repository {
  val tags = TreeMap<String, TagInfo>()
  
  val attributeDelegateRequests = TreeSet<AttributeRequest>(
    Comparator<AttributeRequest> { a, b -> a.type.compareTo(b.type) }
      .thenComparator { a, b -> a.enumTypeName.compareTo(b.enumTypeName) }
      .thenComparator { a, b -> a.options.size.compareTo(b.options.size) }
      .thenComparator { a, b ->
        a.options.zip(b.options).map { it.first.asValue.compareTo(it.second.asValue) }.firstOrNull { it != 0 } ?: 0
      }
  )
  
  val attributeFacades = TreeMap<String, AttributeFacade>()
  val tagGroups = TreeMap<String, TagGroup>()
  val groupsByTags = HashMap<String, MutableList<TagGroup>>()
  
  val groupUnions = HashMap<String, GroupUnion>()
  var unionsByGroups: Map<String, List<GroupUnion>> = emptyMap()
}

data class AttributeFacade(val name: String, val attributes: List<AttributeInfo>, val required: Set<String>) {
  val attributeNames = attributes.map { it.name }.toSet()
}

data class AttributeEnumValue(
  val realName: String,
  val fieldName: String
)

enum class AttributeType(val classPrefix: String, val typeName: String) {
  STRING("String", "String"),
  STRING_SET("StringSet", "Set<String>"),
  BOOLEAN("Boolean", "Boolean"),
  TICKER("Ticker", "Boolean"),
  ENUM("Enum", "???")
}

data class GroupUnion(
  val members: List<String>,
  val intersectionTags: Set<String>,
  val additionalTags: List<String>,
  val ambiguityTags: List<String>,
  val superGroups: List<String>
) {
  val name = unionName(members)
}

interface HasType {
  val type: AttributeType
  val enumTypeName: String
}

data class AttributeRequest(
  override val type: AttributeType,
  override val enumTypeName: String,
  val options: List<Const<*>> = emptyList()
) : HasType

val AttributeRequest.delegatePropertyName: String
  get() = "attribute${typeName.humanize().capitalize()}${type.classPrefix}${
    options.map {
      it.asFieldPart.humanize().capitalize()
    }.joinToString("")
  }"

data class AttributeInfo(
  val name: String,
  override val type: AttributeType,
  val trueFalse: List<String> = listOf(),
  override val enumTypeName: String = "",
  val required: Boolean = false,
  val enumValues: List<AttributeEnumValue> = emptyList()
) : HasType {
  val fieldName: String = attributeReplacements.firstOrNull { it.first.matches(name) }?.second
    ?: name.humanize().replaceIfReserved()
}

val HasType.typeName: String
  get() = if (type == AttributeType.ENUM) enumTypeName else type.typeName

fun String.isLowerCase() = this.toLowerCase() == this

data class TagInfo(
  val name: String,
  val possibleChildren: List<String>,
  val directChildren: List<String>,
  val attributeGroups: List<AttributeFacade>,
  val attributes: List<AttributeInfo>,
  val suggestedAttributes: Set<String>,
  val tagGroupNames: List<String>
) {
  val className: String = name.humanize().toUpperCase()
  val memberName: String = name.humanize().replaceIfReserved()
}

fun TagInfo.mergeAttributes() = attributes + attributeGroups.flatMap { it.attributes }

data class TagGroup(
  val name: String,
  val tags: List<String>
) {
  
  val memberName: String = name.humanize()
  val typeName: String = memberName.capitalize()
}

