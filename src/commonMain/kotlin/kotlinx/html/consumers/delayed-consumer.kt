package kotlinx.html.consumers

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.Unsafe

expect class DelayedConsumer<T, E>(downstream: TagConsumer<T, E>) : TagConsumer<T, E> {
  override fun onTagStart(tag: Tag<E>)
  
  override fun onTagAttributeChange(tag: Tag<E>, attribute: String, value: String?)
  
  override fun onTagEnd(tag: Tag<E>)
  
  override fun onTagContent(content: CharSequence)
  
  override fun onTagContentEntity(entity: Entities)
  
  override fun onTagError(tag: Tag<E>, exception: Throwable)
  
  override fun onTagComment(content: CharSequence)
  
  override fun finalize(): T
  
  override fun onTagContentUnsafe(block: Unsafe.() -> Unit)
}

fun <T, E> TagConsumer<T, E>.delayed(): TagConsumer<T, E> =
  if (this is DelayedConsumer<T, E>) this else DelayedConsumer(this)
