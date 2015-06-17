package kotlinx.html.consumers

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import org.w3c.dom.events.Event

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

    override fun finalize(): T {
        processDelayedTag()
        return downstream.finalize()
    }

    private fun processDelayedTag() {
        delayed?.let { tag ->
            delayed = null
            downstream.onTagStart(tag)
        }
    }
}

fun <T> TagConsumer<T>.delayed() : TagConsumer<T> = if (this is DelayedConsumer<T>) this else DelayedConsumer(this)