package kotlinx.html

public fun HEAD.styleLink(url : String) : Unit = link {
    rel = LinkRel.stylesheet
    type = LinkType.textCss

    href = url
}

public val Tag.br : Unit
    get() {
        val tag = BR(emptyMap(), consumer)
        consumer.onTagStart(tag)
        consumer.onTagEnd(tag)
    }