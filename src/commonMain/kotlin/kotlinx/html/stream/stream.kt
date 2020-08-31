@file:Suppress("PropertyName", "FunctionName")

package kotlinx.html.stream

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.Unsafe
import kotlinx.html.consumers.delayed
import kotlinx.html.consumers.onFinalizeMap

internal expect class HTMLStreamBuilder<out O : Appendable>(out: O, prettyPrint: Boolean, xhtmlCompatible: Boolean) :
    TagConsumer<O, Nothing> {
    override fun onTagStart(tag: Tag<Nothing>)

    override fun onTagAttributeChange(tag: Tag<Nothing>, attribute: String, value: String?)

    override fun onTagEnd(tag: Tag<Nothing>)

    override fun onTagContent(content: CharSequence)

    override fun onTagContentEntity(entity: Entities)

    override fun finalize(): O

    override fun onTagContentUnsafe(block: Unsafe.() -> Unit)

    override fun onTagComment(content: CharSequence)

    val UnsafeImpl: Unsafe

    override fun onTagError(tag: Tag<Nothing>, exception: Throwable)
}

private const val AVERAGE_PAGE_SIZE = 32768

fun createHTML(prettyPrint: Boolean = true, xhtmlCompatible: Boolean = false): TagConsumer<String, Nothing> =
    HTMLStreamBuilder(
        StringBuilder(AVERAGE_PAGE_SIZE),
        prettyPrint,
        xhtmlCompatible
    ).onFinalizeMap { sb, _ -> sb.toString() }.delayed()

fun <O : Appendable> O.appendHTML(
    prettyPrint: Boolean = true,
    xhtmlCompatible: Boolean = false
): TagConsumer<O, Nothing> =
    HTMLStreamBuilder(this, prettyPrint, xhtmlCompatible).delayed()

@Deprecated("Should be resolved to the previous implementation", level = DeprecationLevel.HIDDEN)
fun <O : Appendable> O.appendHTML(prettyPrint: Boolean = true): TagConsumer<O, Nothing> =
    appendHTML(prettyPrint, false)

private val escapeMap = mapOf(
    '<' to "&lt;",
    '>' to "&gt;",
    '&' to "&amp;",
    '\"' to "&quot;"
).let { mappings ->
    val maxCode = mappings.keys.map { it.toInt() }.maxOrNull() ?: -1

    Array(maxCode + 1) { mappings[it.toChar()] }
}

private val letterRangeLowerCase = 'a'..'z'
private val letterRangeUpperCase = 'A'..'Z'
private val digitRange = '0'..'9'

private fun Char._isLetter() = this in letterRangeLowerCase || this in letterRangeUpperCase
private fun Char._isDigit() = this in digitRange

internal fun String.isValidXmlAttributeName() =
    !startsWithXml()
            && this.isNotEmpty()
            && (this[0]._isLetter() || this[0] == '_')
            && this.all { it._isLetter() || it._isDigit() || it in "._:-" }

private fun String.startsWithXml() = length >= 3
        && (this[0].let { it == 'x' || it == 'X' })
        && (this[1].let { it == 'm' || it == 'M' })
        && (this[2].let { it == 'l' || it == 'L' })

internal fun Appendable.escapeAppend(s: CharSequence) {
    var lastIndex = 0
    val mappings = escapeMap
    val size = mappings.size

    for (idx in s.indices) {
        val ch = s[idx].toInt()
        if (ch < 0 || ch >= size) continue
        val escape = mappings[ch]
        if (escape != null) {
            append(s.substring(lastIndex, idx))
            append(escape)
            lastIndex = idx + 1
        }
    }

    if (lastIndex < s.length) {
        append(s.substring(lastIndex, s.length))
    }
}

internal fun Appendable.escapeComment(s: CharSequence) {
    var start = 0
    while (start < s.length) {
        val index = s.indexOf("--")
        if (index == -1) {
            if (start == 0) {
                append(s)
            } else {
                append(s, start, s.length)
            }
            break
        }

        append(s, start, index)
        start += 2
    }
}
