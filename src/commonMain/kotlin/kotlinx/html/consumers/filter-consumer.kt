package kotlinx.html.consumers

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.Unsafe

object PredicateResults {
    val PASS = PredicateResult.PASS
    val SKIP = PredicateResult.SKIP
    val DROP = PredicateResult.DROP
}

enum class PredicateResult {
    PASS,
    SKIP,
    DROP
}

expect class FilterTagConsumer<T, E>(downstream: TagConsumer<T, E>, predicate: (Tag<E>) -> PredicateResult) :
    TagConsumer<T, E> {
    override fun onTagStart(tag: Tag<E>)
    
    override fun onTagAttributeChange(tag: Tag<E>, attribute: String, value: String?)
    
    override fun onTagEnd(tag: Tag<E>)
    
    override fun onTagContent(content: CharSequence)
    
    override fun onTagContentEntity(entity: Entities)
    
    override fun onTagContentUnsafe(block: Unsafe.() -> Unit)
    
    override fun onTagError(tag: Tag<E>, exception: Throwable)
    
    override fun onTagComment(content: CharSequence)
    
    override fun finalize(): T
}

fun <T, E> TagConsumer<T, E>.filter(predicate: PredicateResults.(Tag<E>) -> PredicateResult): TagConsumer<T, E> =
    FilterTagConsumer(this) { PredicateResults.predicate(it) }.delayed()
