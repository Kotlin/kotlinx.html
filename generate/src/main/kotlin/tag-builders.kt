package html4k.generate

fun <O : Appendable> O.htmlTagBuilders(receiver : String, tag : TagInfo) {
    val probablyContentOnly = tag.possibleChildren.isEmpty()
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