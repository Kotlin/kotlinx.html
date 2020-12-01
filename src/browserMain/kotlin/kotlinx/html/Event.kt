package kotlinx.html

expect open class Event {
    fun preventDefault()
    fun initEvent(type: String, bubbles: Boolean, cancelable: Boolean)
    fun stopPropagation()
}
