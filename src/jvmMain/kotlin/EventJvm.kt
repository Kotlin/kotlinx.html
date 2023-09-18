package kotlinx.html.org.w3c.dom.events // ktlint-disable filename

actual open class Event {
    actual open fun stopPropagation() {}
    actual open fun preventDefault() {}

    actual open fun initEvent(type: String, bubbles: Boolean, cancelable: Boolean) {}
}
