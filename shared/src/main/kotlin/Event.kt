package org.w3c.dom.events

@Suppress("HEADER_WITHOUT_IMPLEMENTATION")
header interface Event {
    fun stopPropagation()
    fun preventDefault()

    fun initEvent(eventTypeArg: String,
                           canBubbleArg: Boolean,
                           cancelableArg: Boolean)
}
