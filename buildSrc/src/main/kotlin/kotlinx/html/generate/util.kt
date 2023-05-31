package kotlinx.html.generate

import java.util.*

private val locale = Locale.getDefault()

fun String.capitalize() = replaceFirstChar { if (it.isLowerCase()) it.titlecase(locale) else it.toString() }

fun String.decapitalize() = replaceFirstChar { it.lowercase(locale) }
