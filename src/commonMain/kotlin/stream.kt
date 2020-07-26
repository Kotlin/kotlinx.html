package kotlinx.html.stream

import kotlinx.html.*
import kotlinx.html.consumers.*
import org.w3c.dom.events.*

class HTMLStreamBuilder<out O : Appendable>(val out : O, val prettyPrint : Boolean, val xhtmlCompatible: Boolean) : TagConsumer<O> {
    private var level = 0
    private var ln = true

    override fun onTagStart(tag: Tag) {
        if (prettyPrint && !tag.inlineTag) {
            indent()
        }
        level++

        out.append("<")
        out.append(tag.tagName)

        if (tag.namespace != null) {
            out.append(" xmlns=\"")
            out.append(tag.namespace)
            out.append("\"")
        }

        if (tag.attributes.isNotEmpty()) {
            tag.attributesEntries.forEachIndexed { _, e ->
                if (!e.key.isValidXmlAttributeName()) {
                    throw IllegalArgumentException("Tag ${tag.tagName} has invalid attribute name ${e.key}")
                }

                out.append(' ')
                out.append(e.key)
                out.append("=\"")
                out.escapeAppend(e.value)
                out.append('\"')
            }
        }

        if (xhtmlCompatible && tag.emptyTag) {
            out.append("/")
        }

        out.append(">")
        ln = false
    }

    override fun onTagAttributeChange(tag : Tag, attribute: String, value: String?) {
        throw UnsupportedOperationException("tag attribute can't be changed as it was already written to the stream. Use with DelayedConsumer to be able to modify attributes")
    }

    override fun onTagEvent(tag: Tag, event: String, value: (Event) -> Unit) {
        throw UnsupportedOperationException("you can't assign lambda event handler when building text")
    }

    override fun onTagEnd(tag: Tag) {
        level--
        if (ln) {
            indent()
        }

        if (!tag.emptyTag) {
            out.append("</")
            out.append(tag.tagName)
            out.append(">")
        }

        if (prettyPrint && !tag.inlineTag) {
            appendln()
        }
    }

    override fun onTagContent(content: CharSequence) {
        out.escapeAppend(content)
        ln = false
    }

    override fun onTagContentEntity(entity: Entities) {
        out.append(entity.text)
        ln = false
    }

    override fun finalize(): O = out

    override fun onTagContentUnsafe(block: Unsafe.() -> Unit) {
        UnsafeImpl.block()
    }

    override fun onTagComment(content: CharSequence) {
        if (prettyPrint) {
            indent()
        }

        out.append("<!--")
        out.escapeComment(content)
        out.append("-->")

        ln = false
    }

    val UnsafeImpl = object : Unsafe {
        override operator fun String.unaryPlus() {
            out.append(this)
        }
    }

    private fun appendln() {
        if (prettyPrint && !ln) {
            out.append("\n")
            ln = true
        }
    }

    private fun indent() {
        if (prettyPrint) {
            if (!ln) {
                out.append("\n")
            }
            var remaining = level
            while (remaining >= 4) {
                out.append("        ")
                remaining -= 4
            }
            while (remaining >= 2) {
                out.append("    ")
                remaining -= 2
            }
            if (remaining > 0) {
                out.append("  ")
            }
            ln = false
        }
    }
}

private val AVERAGE_PAGE_SIZE = 32768

fun createHTML(prettyPrint: Boolean = true, xhtmlCompatible: Boolean = false): TagConsumer<String> =
        HTMLStreamBuilder(StringBuilder(AVERAGE_PAGE_SIZE), prettyPrint, xhtmlCompatible).onFinalizeMap { sb, _ -> sb.toString() }.delayed()

fun <O : Appendable> O.appendHTML(prettyPrint: Boolean = true, xhtmlCompatible: Boolean = false): TagConsumer<O> =
        HTMLStreamBuilder(this, prettyPrint, xhtmlCompatible).delayed()

@Deprecated("Should be resolved to the previous implementation", level = DeprecationLevel.HIDDEN)
fun <O : Appendable> O.appendHTML(prettyPrint: Boolean = true): TagConsumer<O> =
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

private val letterRangeLowerCase = 'a' .. 'z'
private val letterRangeUpperCase = 'A' .. 'Z'
private val digitRange = '0' .. '9'

private fun Char._isLetter() = this in letterRangeLowerCase || this in letterRangeUpperCase
private fun Char._isDigit() = this in digitRange

private fun String.isValidXmlAttributeName() =
        !startsWithXml()
                && this.isNotEmpty()
                && (this[0]._isLetter() || this[0] == '_')
                && this.all { it._isLetter() || it._isDigit() || it in "._:-" }

private fun String.startsWithXml() = length >= 3
        && (this[0].let { it == 'x' || it == 'X' })
        && (this[1].let { it == 'm' || it == 'M' })
        && (this[2].let { it == 'l' || it == 'L' })

private fun Appendable.escapeAppend(s : CharSequence) {
    var lastIndex = 0
    val mappings = escapeMap
    val size = mappings.size

    for (idx in 0 .. s.length - 1) {
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

private fun Appendable.escapeComment(s: CharSequence) {
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
