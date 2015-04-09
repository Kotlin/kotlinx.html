package html4k

import java.util.LinkedHashSet

public fun <T> Set<T>.plus(value : T) : Set<T> = with(LinkedHashSet(this)) { add(value); this }
public fun HEAD.styleLink(url : String) : Unit = link {
    rel = LinkRel.stylesheet
    type = LinkType.textCss

    href = url
}

public var CoreAttributeGroupFacade.c : String
    get() = classes.join(" ")
    set(newValue) {
        classes = newValue.split("\\s+").filter {it.isNotEmpty()}.toSet()
    }

public val Tag.br : Unit
    get() {
        val tag = BR(emptyMap(), consumer)
        consumer.onTagStart(tag)
        consumer.onTagEnd(tag)
    }