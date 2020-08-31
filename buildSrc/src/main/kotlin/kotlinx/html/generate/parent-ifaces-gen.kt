package kotlinx.html.generate

import java.io.FileOutputStream
import java.util.*

fun generateParentInterfaces(todir: String, packg: String) {
    val allParentIfaces = Repository.tags.values.filterIgnored().map { tag ->
        val parentAttributeIfaces = tag.attributeGroups.map { it.name.humanize().capitalize() + "Facade" }
        val parentElementIfaces = tag.tagGroupNames.map { it.humanize().capitalize() }
        val sum = parentAttributeIfaces + parentElementIfaces
        
        sum.toSet()
    }.filter { it.isNotEmpty() }.toSet()
    
    val allIntroduced = HashSet<Set<String>>(allParentIfaces.size)
    do {
        val introduced = HashSet<Set<String>>()
        allParentIfaces.toList().allPairs().forEach { pair ->
            val intersection = pair.first.intersect(pair.second)
            if (intersection.size > 1 && intersection !in allIntroduced && intersection !in allParentIfaces) {
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
            
            doNotEditWarning()
            emptyLine()
            emptyLine()
            
            (allIntroduced.map { it.sorted() } + allParentIfaces.filter { it.size > 1 }.map { it.sorted() }).distinct()
                .sortedBy { it.sorted().joinToString("").let { renames[it] ?: it } }.forEach { iface ->
                    val ifaceName = humanizeJoin(iface)
                    val subs =
                        allIntroduced.map { it.sorted() }
                            .filter { other -> other != iface && other.all { it in iface } } +
                                allParentIfaces.map { it.sorted() }
                                    .filter { other -> other != iface && other.all { it in iface } }
                    
                    val computedParents =
                        (iface - subs.flatMap { it } + subs.map(::humanizeJoin) - ifaceName)
                            .distinct()
                            .map { renames[it] ?: it }
                            .sorted().map { p -> "$p<E>" }
                    
                    clazz(
                        Clazz(
                            name = "${renames[ifaceName] ?: ifaceName}<E>",
                            parents = computedParents,
                            isInterface = true
                        )
                    ) {
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
