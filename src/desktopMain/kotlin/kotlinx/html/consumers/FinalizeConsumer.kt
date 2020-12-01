package kotlinx.html.consumers

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.Unsafe

actual class FinalizeConsumer<F, T, E> actual constructor(
    val downstream: TagConsumer<F, E>,
    val block: (F, Boolean) -> T
) : TagConsumer<T, E> {
    private var level = 0

    actual override fun onTagStart(tag: Tag<E>) {
        downstream.onTagStart(tag)
        level++
    }

    actual override fun onTagEnd(tag: Tag<E>) {
        downstream.onTagEnd(tag)
        level--
    }

    actual override fun onTagAttributeChange(tag: Tag<E>, attribute: String, value: String?) =
        downstream.onTagAttributeChange(tag, attribute, value)

    actual override fun onTagContent(content: CharSequence) = downstream.onTagContent(content)
    actual override fun onTagContentEntity(entity: Entities) = downstream.onTagContentEntity(entity)
    actual override fun onTagContentUnsafe(block: Unsafe.() -> Unit) = downstream.onTagContentUnsafe(block)
    actual override fun onTagError(tag: Tag<E>, exception: Throwable) = downstream.onTagError(tag, exception)
    actual override fun onTagComment(content: CharSequence) = downstream.onTagComment(content)

    actual override fun finalize() = block(downstream.finalize(), level > 0)
}
