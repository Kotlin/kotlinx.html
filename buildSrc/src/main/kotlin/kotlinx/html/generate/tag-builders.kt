package kotlinx.html.generate

val emptyTags = """area
base
basefont
bgsound
br
col
command
device
embed
frame
hr
img
input
keygen
link
menuitem
meta
param
source
track
wbr""".lines().toSet()

fun Appendable.htmlTagBuilders(receiver : String, tag : TagInfo) {
    val contentlessTag = tag.name.lowercase() in contentlessTags
    val probablyContentOnly = tag.possibleChildren.isEmpty() && tag.name.lowercase() !in emptyTags && !contentlessTag
    htmlTagBuilderMethod(receiver, tag, true)
    if (probablyContentOnly) {
        htmlTagBuilderMethod(receiver, tag, false)
    } else if (contentlessTag) {
        suppress("DEPRECATION", "DeprecatedCallableAddReplaceWith")
        deprecated("This tag doesn't support content or requires unsafe (try unsafe {})")
        htmlTagBuilderMethod(receiver, tag, false)
    }

    val someEnumAttribute =
        tag.attributes.filter { it.type == AttributeType.ENUM }.maxByOrNull { it.enumValues.size } // ??
    if (someEnumAttribute != null && someEnumAttribute.enumValues.size < 25) {
        htmlTagEnumBuilderMethod(receiver, tag, true, someEnumAttribute, 0)
        if (probablyContentOnly) {
            htmlTagEnumBuilderMethod(receiver, tag, false, someEnumAttribute, 0)
        } else if (contentlessTag) {
            suppress("DEPRECATION", "DeprecatedCallableAddReplaceWith")
            deprecated("This tag doesn't support content or requires unsafe (try unsafe {})")
            htmlTagEnumBuilderMethod(receiver, tag, false, someEnumAttribute, 0)
        }
    }

    emptyLine()
}
