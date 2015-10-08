package kotlinx.html.generate

import java.io.*
import java.util.*
import kotlin.support.*

fun generateParentTraits(todir: String, packg: String) {
    val allParentTraits = Repository.tags.values().map { tag ->
        val parentAttributeTraits = tag.attributeGroups.map { it.name.capitalize() + "Facade" }
        val parentElementTraits = tag.tagGroupNames.map { it.escapeUnsafeValues().capitalize() }

        parentAttributeTraits + parentElementTraits
    }.filter { it.isNotEmpty() }.toSet()

    val allIntroduced = HashSet<Set<String>>(allParentTraits.size)
    do {
        val introduced = HashSet<Set<String>>()
        PairsSequence(allParentTraits.toList(), true).forEach { pair ->
            val intersection = pair.first.intersect(pair.second)
            if (intersection.size > 1 && intersection !in allIntroduced && intersection !in allParentTraits) {
                introduced.add(intersection)
            }
        }

        if (introduced.isEmpty) {
            break
        }

        allIntroduced.addAll(introduced)
    } while (true)

    FileOutputStream("$todir/gen-parent-traits.kt").writer("UTF-8").use {
        it.with {
            packg(packg)
            emptyLine()
//            import("kotlinx.html.*")
            emptyLine()

            warning()
            emptyLine()
            emptyLine()

            (allIntroduced.map { it.sorted() } + allParentTraits.filter { it.size > 1 }.map { it.sorted() }).distinct().forEach { iface ->
                val ifaceName = iface.sorted().joinToString("")
                val subs =
                    allIntroduced.map { it.sorted() }.filter { other -> other != iface && other.all { it in iface } } +
                    allParentTraits.map { it.sorted() }.filter { other -> other != iface && other.all { it in iface } }

                println(subs)

                val parents = iface - subs.flatMap { it } + subs.map { it.sorted().joinToString("") } - ifaceName

                clazz(Clazz(name = ifaceName, parents = parents.distinct().sorted(), isTrait = true)) {
                }
                emptyLine()
            }
        }
    }
}

private class PairsIterator<T>(val source: List<T>, val skipSamePairs: Boolean) : AbstractIterator<Pair<T, T>>() {
    private var index = 0

    override fun computeNext() {
        if (source.isEmpty) {
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

private class PairsSequence<T>(val source: List<T>, val skipSamePairs: Boolean) : Sequence<Pair<T, T>> {
    override fun iterator(): Iterator<Pair<T, T>> = PairsIterator(source, skipSamePairs)
}