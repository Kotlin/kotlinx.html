package kotlinx.html.consumers

import kotlinx.html.Tag
import kotlinx.html.TagConsumer

private class DelegatingExceptionConsumer<R, E>(
    val underlying: TagConsumer<R, E>,
    val handler: TagConsumer<R, E>.(Throwable) -> Unit,
) : TagConsumer<R, E> by underlying {
    
    override fun onTagError(tag: Tag<E>, exception: Throwable) = handler(underlying, exception)
}

/**
 * Allows simple exception handling. Any exceptions will forwarded to `handler` function.
 * For more control of error handling, implement `onTagError` in your subclass of `TagConsumer`
 */
fun <R, E> TagConsumer<R, E>.catch(handler: TagConsumer<R, E>.(Throwable) -> Unit): TagConsumer<R, E> =
    DelegatingExceptionConsumer(this, handler)
