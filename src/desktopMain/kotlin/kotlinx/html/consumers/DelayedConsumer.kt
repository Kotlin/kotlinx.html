package kotlinx.html.consumers

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.Unsafe

actual class DelayedConsumer<T, E> actual constructor(val downstream: TagConsumer<T, E>) : TagConsumer<T, E> {
    private var delayed: Tag<E>? = null

    actual override fun onTagStart(tag: Tag<E>) {
        processDelayedTag()
        delayed = tag
    }

    actual override fun onTagAttributeChange(tag: Tag<E>, attribute: String, value: String?) {
        if (delayed == null || delayed != tag) {
            throw IllegalStateException("You can't change tag attribute because it was already passed to the downstream")
        }
    }

    actual override fun onTagEnd(tag: Tag<E>) {
        processDelayedTag()
        downstream.onTagEnd(tag)
    }

    actual override fun onTagContent(content: CharSequence) {
        processDelayedTag()
        downstream.onTagContent(content)
    }

    actual override fun onTagContentEntity(entity: Entities) {
        processDelayedTag()
        downstream.onTagContentEntity(entity)
    }

    actual override fun onTagError(tag: Tag<E>, exception: Throwable) {
        processDelayedTag()
        downstream.onTagError(tag, exception)
    }

    actual override fun onTagComment(content: CharSequence) {
        processDelayedTag()
        downstream.onTagComment(content)
    }

    actual override fun finalize(): T {
        processDelayedTag()
        return downstream.finalize()
    }

    actual override fun onTagContentUnsafe(block: Unsafe.() -> Unit) {
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
