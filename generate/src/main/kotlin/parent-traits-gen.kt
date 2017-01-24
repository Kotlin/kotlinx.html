package kotlinx.html.generate

import kotlinx.html.generate.humanize.*
import java.io.*
import java.util.*

fun generateParentTraits(todir: String, packg: String) {
    val allParentTraits = Repository.tags.values.filterIgnored().map { tag ->
        val parentAttributeTraits = tag.attributeGroups.map { it.name.capitalize() + "Facade" }
        val parentElementTraits = tag.tagGroupNames.map { it.escapeUnsafeValues().capitalize() }
        val sum = parentAttributeTraits + parentElementTraits

        sum.toSet()
    }.filter { it.isNotEmpty() }.toSet()

    val allIntroduced = HashSet<Set<String>>(allParentTraits.size)
    do {
        val introduced = HashSet<Set<String>>()
        allParentTraits.toList().allPairs().forEach { pair ->
            val intersection = pair.first.intersect(pair.second)
            if (intersection.size > 1 && intersection !in allIntroduced && intersection !in allParentTraits) {
                introduced.add(intersection)
            }
        }

        if (introduced.isEmpty()) {
            break
        }

        allIntroduced.addAll(introduced)
    } while (true)

    FileOutputStream("$todir/gen-parent-traits.kt").writer(Charsets.UTF_8).use {
        it.with {
            packg(packg)
            emptyLine()
//            import("kotlinx.html.*")
            emptyLine()

            warning()
            emptyLine()
            emptyLine()

            (allIntroduced.map { it.sorted() } + allParentTraits.filter { it.size > 1 }.map { it.sorted() }).distinct().sortedBy { it.sorted().joinToString("").let { renames[it] ?: it } }.forEach { iface ->
                val ifaceName = humanizeJoin(iface)
                val subs =
                    allIntroduced.map { it.sorted() }.filter { other -> other != iface && other.all { it in iface } } +
                    allParentTraits.map { it.sorted() }.filter { other -> other != iface && other.all { it in iface } }

                val computedParents =
                        (iface - subs.flatMap { it } + subs.map(::humanizeJoin) - ifaceName)
                            .distinct()
                            .map { renames[it] ?: it }
                            .sorted()

                clazz(Clazz(name = renames[ifaceName] ?: ifaceName, parents = computedParents, isTrait = true)) {
                }
                emptyLine()
            }
        }
    }
}

/**
 * Returns a sequence that consists of all possible pair of original list elements, does nothing with potential duplicates
 * @param skipSamePairs indicates whether it should produce pairs from the same element at both first and second positions
 */
fun <T> List<T>.allPairs(skipSamePairs: Boolean = true): Sequence<Pair<T, T>> = PairsSequence(this, skipSamePairs)

private class PairsSequence<T>(val source: List<T>, val skipSamePairs: Boolean) : Sequence<Pair<T, T>> {
    override fun iterator(): Iterator<Pair<T, T>> = PairsIterator(source, skipSamePairs)
}

private class PairsIterator<T>(val source: List<T>, val skipSamePairs: Boolean) : AbstractIterator<Pair<T, T>>() {
    private var index = 0

    override fun computeNext() {
        if (source.isEmpty()) {
            done()
            return
        }

        index++

        val i1 = index / source.size
        val i2 = index % source.size

        if (i1 >= source.lastIndex) {
            done()
            return
        }

        if (skipSamePairs && i1 == i2) {
            return computeNext()
        }

        setNext(Pair(source[i1], source[i2]))
    }
}
