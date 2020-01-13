package kotlinx.html

fun HEAD.styleLink(url : String) : Unit = link {
    rel = LinkRel.stylesheet
    type = LinkType.textCss

    href = url
}

val Tag.br : Unit
    get() {
        val tag = BR(emptyMap(), consumer)
        consumer.onTagStart(tag)
        consumer.onTagEnd(tag)
    }

expect fun currentTimeMillis(): Long
