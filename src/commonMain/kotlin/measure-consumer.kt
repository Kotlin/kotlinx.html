package kotlinx.html.consumers

import kotlinx.html.*
import kotlinx.html.org.w3c.dom.events.Event

data class TimedResult<T>(val result: T, val time: Long)

val <O : Appendable> TimedResult<O>.out: O
    get() = result

private class TimeMeasureConsumer<R>(val downstream: TagConsumer<R>) : TagConsumer<TimedResult<R>> {
    private val start = currentTimeMillis()

    override fun onTagStart(tag: Tag) {
        downstream.onTagStart(tag)
    }

    override fun onTagAttributeChange(tag: Tag, attribute: String, value: String?) {
        downstream.onTagAttributeChange(tag, attribute, value)
    }

    override fun onTagEvent(tag: Tag, event: String, value: (Event) -> Unit) {
        downstream.onTagEvent(tag, event, value)
    }

    override fun onTagEnd(tag: Tag) {
        downstream.onTagEnd(tag)
    }

    override fun onTagContent(content: CharSequence) {
        downstream.onTagContent(content)
    }

    override fun onTagContentEntity(entity: Entities) {
        downstream.onTagContentEntity(entity)
    }

    override fun onTagContentUnsafe(block: Unsafe.() -> Unit) {
        downstream.onTagContentUnsafe(block)
    }

    override fun onTagError(tag: Tag, exception: Throwable) {
        downstream.onTagError(tag, exception)
    }

    override fun onTagComment(content: CharSequence) {
        downstream.onTagComment(content)
    }

    override fun finalize(): TimedResult<R> = TimedResult(downstream.finalize(), currentTimeMillis() - start)
}

fun <R> TagConsumer<R>.measureTime(): TagConsumer<TimedResult<R>> = TimeMeasureConsumer(this)