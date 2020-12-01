package kotlinx.html

actual interface TagConsumer<out R, E> {
    actual fun onTagStart(tag: Tag<E>)
    actual fun onTagAttributeChange(tag: Tag<E>, attribute: String, value: String?)
    actual fun onTagEnd(tag: Tag<E>)
    actual fun onTagContent(content: CharSequence)
    actual fun onTagContentEntity(entity: Entities)
    actual fun onTagContentUnsafe(block: Unsafe.() -> Unit)
    actual fun onTagComment(content: CharSequence)
    actual fun onTagError(tag: Tag<E>, exception: Throwable)

    actual fun finalize(): R
}
