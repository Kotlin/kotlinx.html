package kotlinx.html.stream

import kotlinx.html.*
import kotlinx.html.consumers.*
import org.w3c.dom.events.Event

private val emptyTags = """area
base
basefont
bgsound
br
col
command
device
embed
frame
hr
img
input
keygen
link
menuitem
meta
param
source
track
wbr""".lines().toSet()

private val inlineTags = """a
abbr
acronym
area
b
base
basefont
bdi
bdo
bgsound
big
br
button
cite
code
command
data
datalist
device
dfn
em
embed
font
i
iframe
img
input
kbd
keygen
label
legend
map
mark
menuitem
meter
object
optgroup
option
output
param
progress
q
rp
rt
ruby
samp
select
small
source
span
strong
sub
summary
sup
textarea
time
track
tt
u
var
wbr""".lines().toSet()

class HTMLStreamBuilder<O : Appendable>(val out : O, val prettyPrint : Boolean) : TagConsumer<O> {
    private var level = 0
    private var ln = true

    override fun onTagStart(tag: Tag) {
        if (tag.tagName !in inlineTags) {
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
            tag.attributesEntries.forEachIndexed { idx, e ->
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

        if (tag.tagName !in emptyTags) {
            out.append("</")
            out.append(tag.tagName)
            out.append(">")
        }

        if (prettyPrint && tag.tagName !in inlineTags) {
            appenln()
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

    val UnsafeImpl = object : Unsafe {
        override operator fun String.unaryPlus() {
            out.append(this)
        }
    }

    private fun appenln() {
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
            for (l in 0..level - 1) {
                out.append("  ")
            }
            ln = false
        }
    }
}

private val AVERAGE_PAGE_SIZE = 32768
public fun createHTML(prettyPrint: Boolean = true): TagConsumer<String> = HTMLStreamBuilder(StringBuilder(AVERAGE_PAGE_SIZE), prettyPrint).onFinalizeMap { sb, last -> sb.toString() }.delayed()
public fun <O : Appendable> O.appendHTML(prettyPrint : Boolean = true) : TagConsumer<O> = HTMLStreamBuilder(this, prettyPrint).delayed()

private val escapeMap = mapOf(
        '<' to "&lt;",
        '>' to "&gt;",
        '&' to "&amp;",
        '\'' to "&apos;",
        '\"' to "&quot;"
)

private fun Char._isLetter() = this in 'a' .. 'z' || this in 'A' .. 'Z'
private fun Char._isDigit() = this in '0' .. '9'
//private fun String.contains(ch : Char) = this.indexOf(ch) != -1

private fun String.isValidXmlAttributeName() =
        !this.toLowerCase().startsWith("xml")
                && this.isNotEmpty()
                && (this[0]._isLetter() || this[0] == '_')
                && this.all { it._isLetter() || it._isDigit() || it in "._:-" }

private fun Appendable.escapeAppend(s : CharSequence) {
    var lastIndex = 0
    for (idx in 0 .. s.length - 1) {
        val ch = s[idx]
        val escape = escapeMap[ch]
        if (escape != null) {
            append(s, lastIndex, idx)
            append(escape)
            lastIndex = idx + 1
        }
    }

    if (lastIndex < s.length) {
        append(s, lastIndex, s.length)
    }
}