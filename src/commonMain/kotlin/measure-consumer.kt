package kotlinx.html.consumers

import kotlinx.html.*
import kotlinx.html.org.w3c.dom.events.Event
import kotlin.time.*

val <O : Appendable> TimedValue<O>.out: O
    get() = value

private class TimeMeasureConsumer<R>(val downstream: TagConsumer<R>, val timeSource: TimeSource) : TagConsumer<TimedValue<R>> {
    private val start = timeSource.markNow()

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

    override fun onTagComment(content: CharSequence) {
        downstream.onTagComment(content)
    }

    override fun finalize(): TimedValue<R> = TimedValue(downstream.finalize(), start.elapsedNow())
}

fun <R> TagConsumer<R>.measureTime(timeSource: TimeSource = TimeSource.Monotonic): TagConsumer<TimedValue<R>> = TimeMeasureConsumer(this, timeSource)
