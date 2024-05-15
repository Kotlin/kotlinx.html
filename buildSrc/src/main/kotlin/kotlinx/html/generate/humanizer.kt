package kotlinx.html.generate

fun String.humanize() : String {
    if (this.isEmpty()) {
        return "empty"
    }

    val fixedAllUpper = if (all { it.isUpperCase() }) lowercase() else this
    val fixedFirstUpper = fixedAllUpper.decapitalize()

    return fixedFirstUpper.replaceHyphensToCamelCase().makeCamelCaseByDictionary().replaceMistakesAndUglyWords().decapitalize()
}

fun humanizeJoin(parts: Iterable<String>) = humanizeJoin(parts, separator = "")

fun humanizeJoin(parts: Iterable<String>, separator: String): String {
    val humanizedParts = parts.map(String::humanize)
    val dictionary = HashMap<String, Int>()

    humanizedParts.forEach { part ->
        var start = 0

        while (start < part.length) {
            var end = part.drop(start + 1).indexOfFirst(Char::isUpperCase)
            if (end == -1) {
                end = part.length
            } else {
                end += start + 1
            }

            val word = part.substring(start, end).capitalize()

            val newCount = dictionary.getOrElse(word) { 0 } + 1
            dictionary[word] = newCount

            start = end
        }
    }

    val repeated = dictionary.filterValues { it > 1 }.keys.toList().sortedByDescending { it.length }
    val filteredParts = ArrayList<String>(humanizedParts.size)
    val trailingParts = HashSet<String>(repeated.size)

    humanizedParts.forEach { part ->
        var cutPart = part
        repeated.forEach { word ->
            if (cutPart.contains(word, ignoreCase = true)) {
                cutPart = cutPart.replace(word, "", ignoreCase = true)
                trailingParts.add(word)
            }
        }

        filteredParts.add(cutPart)
    }

    return filteredParts.joinToString(separator = separator) { it.capitalize() } + trailingParts.joinToString("") { it.capitalize() }
}

private fun String.replaceMistakesAndUglyWords() : String =
    replace("dbl", "double")
        .replace("Dbl", "Double")
        .replace("EnDO", "enDo")


private fun String.replaceHyphensToCamelCase() : String =
        this.split("[.:_\\-<>/]".toRegex())
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
            val startClash = allRanges.safeSubList(i + 1).asSequence().takeWhile { it.range.start == mr.range.start }
                .maxByOrNull { it.value.length }
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
