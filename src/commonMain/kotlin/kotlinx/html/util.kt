package kotlinx.html

fun <E> HEAD<E>.styleLink(url: String): Unit = link {
    rel = LinkRel.stylesheet
    type = LinkType.textCss

    href = url
}

val <E> Tag<E>.br: Unit
    get() {
        val tag = BR(emptyMap(), consumer)
        consumer.onTagStart(tag)
        consumer.onTagEnd(tag)
    }

expect fun currentTimeMillis(): Long
