package kotlinx.html.consumers

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.Unsafe

actual class FilterTagConsumer<T, E> actual constructor(
    val downstream: TagConsumer<T, E>,
    val predicate: (Tag<E>) -> PredicateResult
) :
    TagConsumer<T, E> {
    private var currentLevel = 0
    private var skippedLevels = HashSet<Int>()
    private var dropLevel: Int? = null

    actual override fun onTagStart(tag: Tag<E>) {
        currentLevel++

        if (dropLevel == null) {
            when (predicate(tag)) {
                PredicateResult.PASS -> downstream.onTagStart(tag)
                PredicateResult.SKIP -> skippedLevels.add(currentLevel)
                PredicateResult.DROP -> dropLevel = currentLevel
            }
        }
    }

    actual override fun onTagAttributeChange(tag: Tag<E>, attribute: String, value: String?) {
        throw UnsupportedOperationException("this filter doesn't support attribute change")
    }

    actual override fun onTagEnd(tag: Tag<E>) {
        if (canPassCurrentLevel()) {
            downstream.onTagEnd(tag)
        }

        skippedLevels.remove(currentLevel)
        if (dropLevel == currentLevel) {
            dropLevel = null
        }

        currentLevel--
    }

    actual override fun onTagContent(content: CharSequence) {
        if (canPassCurrentLevel()) {
            downstream.onTagContent(content)
        }
    }

    actual override fun onTagContentEntity(entity: Entities) {
        if (canPassCurrentLevel()) {
            downstream.onTagContentEntity(entity)
        }
    }

    actual override fun onTagContentUnsafe(block: Unsafe.() -> Unit) {
        if (canPassCurrentLevel()) {
            downstream.onTagContentUnsafe(block)
        }
    }

    private fun canPassCurrentLevel() = dropLevel == null && currentLevel !in skippedLevels

    actual override fun onTagError(tag: Tag<E>, exception: Throwable) {
        if (canPassCurrentLevel()) {
            downstream.onTagError(tag, exception)
        }
    }

    actual override fun onTagComment(content: CharSequence) {
        if (canPassCurrentLevel()) {
            downstream.onTagComment(content)
        }
    }

    actual override fun finalize(): T = downstream.finalize()
}
