package kotlinx.html.jsoup

import org.jsoup.nodes.Attributes

//region Read-Only Properties
val Attributes.size: Int
    get() = size()
//endregion

//region Methods
operator fun Attributes.set(key: String, value: String)
    = put(key, value)

operator fun Attributes.set(key: String, value: Boolean)
    = put(key, value)

operator fun Attributes.contains(key: String): Boolean
    = hasKey(key)

operator fun Attributes.contains(attribute: Pair<String, String>): Boolean
    = this.hasKey(attribute.first) && this[attribute.first] == attribute.second

fun Attributes.toMap(): Map<String, String>
    = associate { it.key to it.value }
//endregion
