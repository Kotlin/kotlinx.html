package kotlinx.html.consumers

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.Unsafe
import kotlinx.html.currentTimeMillis

actual class TimeMeasureConsumer<R, E> actual constructor(val downstream: TagConsumer<R, E>) :
    TagConsumer<TimedResult<R>, E> {
    private val start = currentTimeMillis()
    
    actual override fun onTagStart(tag: Tag<E>) {
        downstream.onTagStart(tag)
    }
    
    actual override fun onTagAttributeChange(tag: Tag<E>, attribute: String, value: String?) {
        downstream.onTagAttributeChange(tag, attribute, value)
    }
    
    actual override fun onTagEnd(tag: Tag<E>) {
        downstream.onTagEnd(tag)
    }
    
    actual override fun onTagContent(content: CharSequence) {
        downstream.onTagContent(content)
    }
    
    actual override fun onTagContentEntity(entity: Entities) {
        downstream.onTagContentEntity(entity)
    }
    
    actual override fun onTagContentUnsafe(block: Unsafe.() -> Unit) {
        downstream.onTagContentUnsafe(block)
    }
    
    actual override fun onTagError(tag: Tag<E>, exception: Throwable) {
        downstream.onTagError(tag, exception)
    }
    
    actual override fun onTagComment(content: CharSequence) {
        downstream.onTagComment(content)
    }
    
    actual override fun finalize(): TimedResult<R> = TimedResult(downstream.finalize(), currentTimeMillis() - start)
}
