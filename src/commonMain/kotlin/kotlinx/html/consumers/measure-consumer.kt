package kotlinx.html.consumers

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.Unsafe

data class TimedResult<T>(val result: T, val time: Long)

val <O : Appendable> TimedResult<O>.out: O
    get() = result

expect class TimeMeasureConsumer<R, E>(downstream: TagConsumer<R, E>) : TagConsumer<TimedResult<R>, E> {
    override fun onTagStart(tag: Tag<E>)
    
    override fun onTagAttributeChange(tag: Tag<E>, attribute: String, value: String?)
    
    override fun onTagEnd(tag: Tag<E>)
    
    override fun onTagContent(content: CharSequence)
    
    override fun onTagContentEntity(entity: Entities)
    
    override fun onTagContentUnsafe(block: Unsafe.() -> Unit)
    
    override fun onTagError(tag: Tag<E>, exception: Throwable)
    
    override fun onTagComment(content: CharSequence)
    
    override fun finalize(): TimedResult<R>
}

fun <R, E> TagConsumer<R, E>.measureTime(): TagConsumer<TimedResult<R>, E> = TimeMeasureConsumer(this)
