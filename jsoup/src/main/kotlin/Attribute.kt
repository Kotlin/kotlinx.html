package kotlinx.html.jsoup

import org.jsoup.nodes.Attribute

val Attribute.html: String
    get() = html()

fun Pair<String, String>.toAttribute(): Attribute
    = Attribute(first, second)
