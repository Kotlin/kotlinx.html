package html4k.generate.humanize

import html4k.generate.wellKnownWords
import java.util.regex.Pattern


fun String.humanize() : String {
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
        this.split("[.:_\\-]")
                .mapIndexed { i, s ->
                    if (i == 0) s
                    else s.capitalize()
                }
                .join("")

private fun StringBuilder.capitalizeAt(index : Int) {
    val ch = this[index]
    this.setCharAt(index, Character.toUpperCase(ch))
}

private fun String.makeCamelCaseByDictionary() : String {
    var current = StringBuilder(this)

    wellKnownWords.sortDescendingBy { it.pattern().length() }.forEach { word ->
        val matcher = word.matcher(current)

        while (matcher.find()) {
            if (matcher.start() > 0) {
                current.capitalizeAt(matcher.start())
            }
            if (matcher.end() < length()) {
                current.capitalizeAt(matcher.end())
            }
        }
    }

    return current.toString()
}

fun main(args: Array<String>) {
    println("contenteditable".humanize())
}