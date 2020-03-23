package org.w3c.dom.events

@Suppress("HEADER_WITHOUT_IMPLEMENTATION", "NO_ACTUAL_FOR_EXPECT")
expect interface Event {
    fun stopPropagation()
    fun preventDefault()

    fun initEvent(
        eventTypeArg: String,
        canBubbleArg: Boolean,
        cancelableArg: Boolean
    )
}
