package kotlinx.html.org.w3c.dom.events

expect interface Event {
    fun stopPropagation()
    fun preventDefault()

    fun initEvent(eventTypeArg: String, canBubbleArg: Boolean, cancelableArg: Boolean)
}
