package kotlinx.html.consumers

import kotlinx.html.*
import org.w3c.dom.events.*

class DelayedConsumer<T>(val downstream : TagConsumer<T>) : TagConsumer<T> {
    private var delayed : Tag? = null

    override fun onTagStart(tag: Tag) {
        processDelayedTag()
        delayed = tag
    }

    override fun onTagAttributeChange(tag : Tag, attribute: String, value: String?) {
        if (delayed == null || delayed != tag) {
            throw IllegalStateException("You can't change tag attribute because it was already passed to the downstream")
        }
    }

    override fun onTagEvent(tag: Tag, event: String, value: (Event) -> Unit) {
        if (delayed == null || delayed != tag) {
            throw IllegalStateException("You can't change tag attribute because it was already passed to the downstream")
        }
    }

    override fun onTagEnd(tag: Tag) {
        processDelayedTag()
        downstream.onTagEnd(tag)
    }

    override fun onTagContent(content: CharSequence) {
        processDelayedTag()
        downstream.onTagContent(content)
    }

    override fun onTagContentEntity(entity: Entities) {
        processDelayedTag()
        downstream.onTagContentEntity(entity)
    }

    override fun onTagError(tag: Tag, exception: Throwable) {
        processDelayedTag()
        downstream.onTagError(tag, exception)
    }

    override fun onTagComment(content: CharSequence) {
        processDelayedTag()
        downstream.onTagComment(content)
    }

    override fun finalize(): T {
        processDelayedTag()
        return downstream.finalize()
    }

    override fun onTagContentUnsafe(block: Unsafe.() -> Unit) {
        processDelayedTag()
        return downstream.onTagContentUnsafe(block)
    }

    private fun processDelayedTag() {
        delayed?.let { tag ->
            delayed = null
            downstream.onTagStart(tag)
        }
    }
}

fun <T> TagConsumer<T>.delayed() : TagConsumer<T> = if (this is DelayedConsumer<T>) this else DelayedConsumer(this)