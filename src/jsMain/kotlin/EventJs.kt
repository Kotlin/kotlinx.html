package kotlinx.html.org.w3c.dom.events // ktlint-disable filename

import org.w3c.dom.events.EventTarget

public actual external interface Event {
    val type: String
    val target: EventTarget?
    val currentTarget: EventTarget?
    val eventPhase: Short
    val bubbles: Boolean
    val cancelable: Boolean
    val defaultPrevented: Boolean
    val composed: Boolean
    val isTrusted: Boolean
    val timeStamp: Number

    fun stopImmediatePropagation()
    fun composedPath(): Array<EventTarget>

    actual fun stopPropagation()
    actual fun preventDefault()
    actual fun initEvent(eventTypeArg: String, canBubbleArg: Boolean, cancelableArg: Boolean)
}
