package kotlinx.html.consumers

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.Unsafe
import org.w3c.dom.events.Event

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

private class FilterTagConsumer<T>(val downstream : TagConsumer<T>, val predicate : (Tag) -> PredicateResult) : TagConsumer<T> {
    private var currentLevel = 0
    private var skippedLevels = HashSet<Int>()
    private var dropLevel : Int? = null

    override fun onTagStart(tag: Tag) {
        currentLevel++

        if (dropLevel == null) {
            when (predicate(tag)) {
                PredicateResult.PASS -> downstream.onTagStart(tag)
                PredicateResult.SKIP -> skippedLevels.add(currentLevel)
                PredicateResult.DROP -> dropLevel = currentLevel
            }
        }
    }

    override fun onTagAttributeChange(tag: Tag, attribute: String, value: String?) {
        throw UnsupportedOperationException("this filter doesn't support attribute change")
    }

    override fun onTagEvent(tag: Tag, event: String, value: (Event) -> Unit) {
        throw UnsupportedOperationException("this filter doesn't support attribute change")
    }

    override fun onTagEnd(tag: Tag) {
        if (canPassCurrentLevel()) {
            downstream.onTagEnd(tag)
        }

        skippedLevels.remove(currentLevel)
        if (dropLevel == currentLevel) {
            dropLevel = null
        }

        currentLevel--
    }

    override fun onTagContent(content: CharSequence) {
        if (canPassCurrentLevel()) {
            downstream.onTagContent(content)
        }
    }

    override fun onTagContentEntity(entity: Entities) {
        if (canPassCurrentLevel()) {
            downstream.onTagContentEntity(entity)
        }
    }

    override fun onTagContentUnsafe(block: Unsafe.() -> Unit) {
        if (canPassCurrentLevel()) {
            downstream.onTagContentUnsafe(block)
        }
    }

    private fun canPassCurrentLevel() = dropLevel == null && currentLevel !in skippedLevels

    override fun onTagError(tag: Tag, exception: Throwable) {
        if (canPassCurrentLevel()) {
            downstream.onTagError(tag, exception)
        }
    }

    override fun onTagComment(content: CharSequence) {
        if (canPassCurrentLevel()) {
            downstream.onTagComment(content)
        }
    }

    override fun finalize(): T = downstream.finalize()
}

fun <T> TagConsumer<T>.filter(predicate : PredicateResults.(Tag) -> PredicateResult) : TagConsumer<T> = FilterTagConsumer(this) { PredicateResults.predicate(it) }.delayed()