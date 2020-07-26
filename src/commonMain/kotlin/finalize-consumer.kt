package kotlinx.html.consumers

import kotlinx.html.*
import org.w3c.dom.events.*

class FinalizeConsumer<F, T>(val downstream: TagConsumer<F>, val block: (F, Boolean) -> T) : TagConsumer<T> {
  private var level = 0
  
  override fun onTagStart(tag: Tag) {
    downstream.onTagStart(tag)
    level++
  }
  
  override fun onTagEnd(tag: Tag) {
    downstream.onTagEnd(tag)
    level--
  }
  
  override fun onTagAttributeChange(tag: Tag, attribute: String, value: String?) =
    downstream.onTagAttributeChange(tag, attribute, value)
  
  override fun onTagEvent(tag: Tag, event: String, value: (Event) -> Unit) = downstream.onTagEvent(tag, event, value)
  override fun onTagContent(content: CharSequence) = downstream.onTagContent(content)
  override fun onTagContentEntity(entity: Entities) = downstream.onTagContentEntity(entity)
  override fun onTagContentUnsafe(block: Unsafe.() -> Unit) = downstream.onTagContentUnsafe(block)
  override fun onTagError(tag: Tag, exception: Throwable) = downstream.onTagError(tag, exception)
  override fun onTagComment(content: CharSequence) = downstream.onTagComment(content)
  
  override fun finalize() = block(downstream.finalize(), level > 0)
}

fun <T> TagConsumer<T>.onFinalize(block: (from: T, partial: Boolean) -> Unit): TagConsumer<T> =
  FinalizeConsumer(this) { to, partial -> block(to, partial); to }

fun <F, T> TagConsumer<F>.onFinalizeMap(block: (from: F, partial: Boolean) -> T): TagConsumer<T> =
  FinalizeConsumer(this, block)
