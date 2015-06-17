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

val shouldHaveNoContent = setOf("script")

fun <O : Appendable> O.htmlTagBuilders(receiver : String, tag : TagInfo) {
    val probablyContentOnly = tag.possibleChildren.isEmpty() && tag.name.toLowerCase() !in emptyTags && tag.name.toLowerCase() !in shouldHaveNoContent
    htmlTagBuilderMethod(receiver, tag, true)
    if (probablyContentOnly) {
        htmlTagBuilderMethod(receiver, tag, false)
    }

    val someEnumAttribute = tag.attributes.filter { it.type == AttributeType.ENUM }.maxBy { it.enumValues.size() } // ??
    if (someEnumAttribute != null && someEnumAttribute.enumValues.size() < 25) {
        htmlTagEnumBuilderMethod(receiver, tag, true, someEnumAttribute, 0)
        if (probablyContentOnly) {
            htmlTagEnumBuilderMethod(receiver, tag, false, someEnumAttribute, 0)
        }
    }

    emptyLine()
}