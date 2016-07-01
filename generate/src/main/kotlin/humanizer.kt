package kotlinx.html.generate.humanize

import kotlinx.html.generate.*


fun String.humanize() : String {
    if (this.isEmpty()) {
        return "empty"
    }

    val fixedAllUpper = if (all { it.isUpperCase() }) toLowerCase() else this
    val fixedFirstUpper = fixedAllUpper.decapitalize()

    return fixedFirstUpper.replaceHyphensToCamelCase().makeCamelCaseByDictionary().replaceMistakesAndUglyWords()
}

private fun String.replaceMistakesAndUglyWords() : String =
        replace("SuspEnd", "Suspend")
        .replace("suspEnd", "suspend")
        .replace("dbl", "double")
        .replace("Dbl", "Double")


private fun String.replaceHyphensToCamelCase() : String =
        this.split("[.:_\\-<>]".toRegex())
                .mapIndexed { i, s ->
                    if (i == 0) s
                    else s.capitalize()
                }
                .joinToString("")

private fun StringBuilder.capitalizeAt(index : Int) {
    val ch = this[index]
    this.setCharAt(index, Character.toUpperCase(ch))
}

private fun <T> List<T>.safeSubList(from : Int) : List<T> = if (from >= size) emptyList() else subList(from, size)

private fun String.makeCamelCaseByDictionary() : String {
    val current = StringBuilder(this)

    val allRanges = wellKnownWords.flatMap { word ->
        word.findAll(current).toList()
    }.sortedBy { it.range.start }

    fun applyMatchResult(mr : MatchResult, cutTail : Boolean) {
        if (mr.range.start > 0) {
            current.capitalizeAt(mr.range.start)
        }
        if (!cutTail && mr.range.endInclusive < length - 1) {
            current.capitalizeAt(mr.range.last + 1)
        }
    }

    var unprocessedStart = 0
    allRanges.forEachIndexed { i, mr ->
        if (mr.range.start >= unprocessedStart) {
            val startClash = allRanges.safeSubList(i + 1).asSequence().takeWhile { it.range.start == mr.range.start }.maxBy { it.value.length }
            if (startClash == null || startClash.value.length <= mr.value.length) {
                val possibleTail = when {
                    mr.value.endsWith("ing") -> 3
                    mr.value.endsWith("es") -> 2
                    mr.value.endsWith("ed") -> 2
                    mr.value.endsWith("s") -> 1
                    mr.value.endsWith("d") -> 1
                    else -> 0
                }

                val thereAreClashes = possibleTail > 0 &&
                        allRanges.safeSubList(i + 1)
                                .asSequence()
                                .takeWhile { it.range.start <= mr.range.endInclusive }
                                .any { it.range.start > mr.range.endInclusive - possibleTail }

                applyMatchResult(mr, thereAreClashes)
                unprocessedStart = mr.range.last - possibleTail + 1
            }
        }
    }

    return current.toString()
}
