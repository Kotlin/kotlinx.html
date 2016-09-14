import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.div

private class DelegatingExceptionConsumer<R>(
        val underlying: TagConsumer<R>,
        val handler: TagConsumer<R>.(Exception) -> Unit) : TagConsumer<R> by underlying {

    override fun onError(tag: Tag, exception: Exception) = handler(underlying, exception)
}

/**
 * Allows simple exception handling. Any exceptions will forwarded to `handler` function.
 * For more control of error handling, implement `onError` in your subclass of `TagConsumer`
 */
fun <R> TagConsumer<R>.catch(handler: TagConsumer<R>.(Exception) -> Unit): TagConsumer<R>
        = DelegatingExceptionConsumer(this, handler)
