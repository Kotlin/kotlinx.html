package html4k.generate

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

fun <O : Appendable> O.htmlTagBuilders(receiver : String, tag : TagInfo) {
    val probablyContentOnly = tag.possibleChildren.isEmpty() && tag.name.toLowerCase() !in emptyTags
    htmlTagBuilderMethod(receiver, tag, true)
    if (probablyContentOnly) {
        htmlTagBuilderMethod(receiver, tag, false)
    }

    val someEnumAttribute = tag.attributes.filter { it.type == AttributeType.ENUM && it.enumValues.isNotEmpty() }.maxBy { it.enumValues.size() } // ??
    if (someEnumAttribute != null) {
        htmlTagEnumBuilderMethod(receiver, tag, true, someEnumAttribute, 0)
        if (probablyContentOnly) {
            htmlTagEnumBuilderMethod(receiver, tag, false, someEnumAttribute, 0)
        }
    }

    emptyLine()
}