package kotlinx.html

import java.util.LinkedHashSet

public operator fun <T> Set<T>.plus(value : T) : Set<T> = with(LinkedHashSet(this)) { add(value); this }
public operator fun <T> Set<T>.minus(value : T) : Set<T> = with(LinkedHashSet(this)) { remove(value); this }

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