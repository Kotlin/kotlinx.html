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
    val contentlessTag = tag.name.toLowerCase() in contentlessTags
    val probablyContentOnly = tag.possibleChildren.isEmpty() && tag.name.toLowerCase() !in emptyTags && !contentlessTag
    htmlTagBuilderMethod(receiver, tag, true)
    if (probablyContentOnly) {
        htmlTagBuilderMethod(receiver, tag, false)
    } else if (contentlessTag) {
        deprecated("This tag doesn't support content or requires unsafe (try unsafe {})")
        suppress("DEPRECATION")
        htmlTagBuilderMethod(receiver, tag, false)
    }

    val someEnumAttribute = tag.attributes.filter { it.type == AttributeType.ENUM }.maxBy { it.enumValues.size } // ??
    if (someEnumAttribute != null && someEnumAttribute.enumValues.size < 25) {
        htmlTagEnumBuilderMethod(receiver, tag, true, someEnumAttribute, 0)
        if (probablyContentOnly) {
            htmlTagEnumBuilderMethod(receiver, tag, false, someEnumAttribute, 0)
        } else if (contentlessTag) {
            deprecated("This tag doesn't support content or requires unsafe (try unsafe {})")
            suppress("DEPRECATION")
            htmlTagEnumBuilderMethod(receiver, tag, false, someEnumAttribute, 0)
        }
    }

    emptyLine()
}