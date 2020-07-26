package org.w3c.dom.events

actual external interface Event {
  actual fun stopPropagation()
  actual fun preventDefault()
  actual fun initEvent(eventTypeArg: String, canBubbleArg: Boolean, cancelableArg: Boolean)
}
