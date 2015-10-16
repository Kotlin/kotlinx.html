package kotlinx.html.generate.humanize

import kotlinx.html.generate.*
import java.util.*
import java.util.regex.*


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

private fun Matcher.findAll() : List<MatchResult> = ArrayList<MatchResult>().let { results ->
    while (find()) {
        results.add(toMatchResult())
    }

    results
}

private fun <T> List<T>.safeSubList(from : Int) : List<T> = if (from >= size) emptyList() else subList(from, size)

private fun String.makeCamelCaseByDictionary() : String {
    var current = StringBuilder(this)

    val allRanges = wellKnownWords.flatMap { word ->
        word.matcher(current).findAll()
    }.sortedBy { it.start() }

    fun applyMatchResult(mr : MatchResult, cutTail : Boolean) {
        if (mr.start() > 0) {
            current.capitalizeAt(mr.start())
        }
        if (!cutTail && mr.end() < length) {
            current.capitalizeAt(mr.end())
        }
    }

    var unprocessedStart = 0
    allRanges.forEachIndexed { i, mr ->
        if (mr.start() >= unprocessedStart) {
            val startClash = allRanges.safeSubList(i + 1).asSequence().takeWhile { it.start() == mr.start() }.maxBy { it.group().length }
            if (startClash == null || startClash.group().length <= mr.group().length) {
                val possibleTail = when {
                    mr.group().endsWith("ing") -> 3
                    mr.group().endsWith("es") -> 2
                    mr.group().endsWith("ed") -> 2
                    mr.group().endsWith("s") -> 1
                    mr.group().endsWith("d") -> 1
                    else -> 0
                }

                val thereAreClashes = possibleTail > 0 &&
                        allRanges.safeSubList(i + 1)
                                .asSequence()
                                .takeWhile { it.start() < mr.end() }
                                .any { it.start() >= mr.end() - possibleTail }

                applyMatchResult(mr, thereAreClashes)
                unprocessedStart = mr.end() - possibleTail
            }
        }
    }

    return current.toString()
}
