package kotlinx.html.consumers

import kotlinx.html.Tag
import kotlinx.html.TagConsumer

private class DelegatingExceptionConsumer<R>(
        val underlying: TagConsumer<R>,
        val handler: TagConsumer<R>.(Throwable) -> Unit) : TagConsumer<R> by underlying {

    override fun onTagError(tag: Tag, exception: Throwable) = handler(underlying, exception)
}

/**
 * Allows simple exception handling. Any exceptions will forwarded to `handler` function.
 * For more control of error handling, implement `onTagError` in your subclass of `TagConsumer`
 */
fun <R> TagConsumer<R>.catch(handler: TagConsumer<R>.(Throwable) -> Unit): TagConsumer<R>
        = DelegatingExceptionConsumer(this, handler)
