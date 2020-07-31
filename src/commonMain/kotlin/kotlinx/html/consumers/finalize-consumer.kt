package kotlinx.html.consumers

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.Unsafe

expect class FinalizeConsumer<F, T, E>(downstream: TagConsumer<F, E>, block: (F, Boolean) -> T) : TagConsumer<T, E> {
  override fun onTagStart(tag: Tag<E>)
  override fun onTagEnd(tag: Tag<E>)
  override fun onTagAttributeChange(tag: Tag<E>, attribute: String, value: String?)
  override fun onTagContent(content: CharSequence)
  override fun onTagContentEntity(entity: Entities)
  override fun onTagContentUnsafe(block: Unsafe.() -> Unit)
  override fun onTagError(tag: Tag<E>, exception: Throwable)
  override fun onTagComment(content: CharSequence)
  override fun finalize(): T
}

fun <T, E> TagConsumer<T, E>.onFinalize(block: (from: T, partial: Boolean) -> Unit): TagConsumer<T, E> =
  FinalizeConsumer(this) { to, partial -> block(to, partial); to }

fun <F, T, E> TagConsumer<F, E>.onFinalizeMap(block: (from: F, partial: Boolean) -> T): TagConsumer<T, E> =
  FinalizeConsumer(this, block)
