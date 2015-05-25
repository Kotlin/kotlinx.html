package html4k.tests

import html4k.*
import html4k.consumers.filter
import html4k.consumers.measureTime
import html4k.consumers.trace
import html4k.stream.appendHTML
import java.io.StringWriter
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue
import org.junit.Test as test

class TestStreaming {
    test fun `we should be able to construct at least simple things`() {
        assertEquals(
                "<html>\n  <body>\n    <h1>Test me</h1>\n  </body>\n</html>\n",
                StringBuilder {
                    appendHTML().html {
                        body {
                            h1 {
                                +"Test me"
                            }
                        }
                    }
                }.toString())
    }

    test fun `we should be able append multiple htmls`() {
        assertEquals(
                "<html>\n  <body>\n    <h1>Test me</h1>\n  </body>\n</html>\n<html></html>\n",
                StringBuilder {
                    appendHTML().html {
                        body {
                            h1 {
                                +"Test me"
                            }
                        }
                    }

                    appendHTML().html {
                    }
                }.toString())
    }

    test(expected = IllegalStateException::class)
    fun `we shouldn't be able to change attributes of other tag`() {
        StringBuilder {
            appendHTML().html {
                body {
                    div {
                        this@body.id = "kotlin"
                    }
                }
            }
        }
    }

    test fun `we should be able to write to different appendable`() {
        val sw = StringWriter()
        val result = sw.appendHTML(false).html {
            body {
                p { +"Kotlin" }
            }
        }

        val sw2: StringWriter = result  // note: this is to ensure result type is valid at compile time
        assertEquals("<html><body><p>Kotlin</p></body></html>", sw.toString())
        assertEquals("<html><body><p>Kotlin</p></body></html>", sw2.toString())
    }

    test fun `we should be able filter tags`() {
        val sw = StringWriter()

        val result = sw.appendHTML(false).filter {
            when (it.tagName) {
                "div" -> DROP
                "pre" -> SKIP
                else -> PASS
            }
        }.html {
            body {
                div {
                    div {
                        p {
                            +"[div.div.p]"
                        }
                    }
                }
                pre {
                    p {
                        +"[pre.p]"
                        pre {
                            a {
                                +"[pre.p.pre.a]"
                            }
                        }
                    }
                    div {
                        p {
                            +"[pre.div.p]"
                        }
                    }
                }
            }
        }

        val sw2: StringWriter = result // note: this is to ensure result type is valid at compile time

        assertEquals("<html><body><p>[pre.p]<a>[pre.p.pre.a]</a></p></body></html>", sw2.toString())
    }

    test fun `we should be able to handle many requests`() {
        for (i in 1..1000000) {
            NullAppendable.appendHTML().html {
                body {
                    h1 {
                        +"kotlin"
                    }
                    p {
                        +"Here we are"
                    }
                    div {
                        classes = setOf("root")

                        div {
                            classes = setOf("menu")

                            ul {
                                li { +"item1" }
                                li { +"item2" }
                                li { +"item3" }
                            }
                        }
                        div {
                            classes = setOf("content")
                        }
                    }
                }
            }
        }
    }

    test fun `we should be able to make extension functions with DSL`() {
        assertEquals("<html><body><div class=\"block deprecated\"><a href=\"http://kotlinlang.org\" target=\"_blank\" custom=\"custom\">test me</a></div></body></html>", StringBuilder().appendHTML(false).buildMe().toString())
    }

    test fun `empty tag should have attributes`() {
        assertEquals("<div id=\"d\"></div>", StringBuilder().appendHTML(false).div { id = "d" }.toString())
        assertEquals("<div id=\"d\"><div id=\"d2\"></div></div>", StringBuilder().appendHTML(false).div { id = "d"; div { id = "d2" } }.toString())
    }

    test fun `test attributes order`() {
        val order1 = StringBuilder().appendHTML(false).html {
            body {
                div {
                    id = "main"
                    classes = setOf("yellow")
                }
            }
        }.toString()

        val order2 = StringBuilder().appendHTML(false).html {
            body {
                div {
                    classes = setOf("yellow")
                    id = "main"
                }
            }
        }.toString()

        assertNotEquals(order1, order2)
    }

    test fun `multiple attributes and custom attribute present`() {
        assertEquals("<div id=\"d1\" custom=\"c1\" class=\"c1 c2\"></div>", StringBuilder().appendHTML(false).div {
            id = "d1"
            attributes["custom"] = "c1"
            classes = linkedSetOf("c1", "c2")
        }.toString())
    }

    test fun `test tags order`() {
        assertEquals("<div><p><span></span></p></div>", StringBuilder().appendHTML(false).div { p { span {} } }.toString())
    }

    test fun `test generated enum could be used`() {
        assertEquals("<link rel=\"Stylesheet\" href=\"/path\"></link>", StringBuilder().appendHTML(false).link {
            rel = LinkRel.stylesheet
            href = "/path"
        }.toString())
    }

    test fun `anchor with href syntax`() {
        assertEquals("<a href=\"a.html\">text</a>", StringBuilder().appendHTML(false).a("a.html") {
            +"text"
        }.toString())
    }

    test fun `multiple content`() {
        assertEquals("<span>AAAbbb...</span>", StringBuilder().appendHTML(false).span {
            +"AAA"
            +"bbb."
            +".."
        }.toString())
    }

    test fun `content with entity`() {
        assertEquals("<span>before&amp;after</span>", StringBuilder().appendHTML(false).span {
            +"before"
            +Entities.amp
            +"after"
        }.toString())
    }

    test fun `test form with button`() {
        assertEquals("<form action=\"/someurl\">" +
                "<input type=\"checkbox\" name=\"cb1\">var1</input>" +
                "<input type=\"checkbox\" name=\"cb2\" disabled=\"disabled\">var2</input>" +
                "<input type=\"submit\">Go!</input>" +
                "</form>",
                StringBuilder().appendHTML(false).form("/someurl") {
                    checkBoxInput(name = "cb1") {
                        +"var1"
                    }
                    input(type = InputType.checkBox, name = "cb2") {
                        disabled = true
                        +"var2"
                    }

                    submitInput(content = "Go!")
                }.toString())
    }

    test fun `test measure consumer with loop inside`() {
        val count = 1000
        val rs = StringBuilder(26 * (count + 1)).appendHTML(false).measureTime().div {
            for (i in 1..count) {
                div {
                    p { +"node$i" }
                }
            }
        }

        assertTrue(rs.second > 0)
        assertTrue(rs.second < count.toLong())

        val expected = StringBuilder {
            append("<div>")
            for (i in 1..count) {
                append("<div><p>")
                append("node")
                append(i)
                append("</p></div>")
            }
            append("</div>")
        }

        assertEquals(expected.toString(), rs.first.toString())
    }

    test fun `escape bad chars`() {
        assertEquals("<div id=\"bad&quot;\" custom=\"bad&amp;&quot;\">" +
                "content&lt;script&gt;" +
                "</div>",
                StringBuilder().appendHTML(false).div {
                    id = "bad\""
                    attributes["custom"] = "bad&\""
                    +"content<script>"
                }.toString())
    }

    test(expected = IllegalArgumentException::class)
    fun `bad chars in attribute name`() {
        StringBuilder().appendHTML().div {
            attributes["bad'char"] = "test"
        }
    }

    test(expected = IllegalArgumentException::class)
    fun `bad chars 'equals' in attribute name`() {
        StringBuilder().appendHTML().div {
            attributes["bad=char"] = "test"
        }
    }
}

fun <T> TagConsumer<T>.buildMe() = html { body { buildMe2() } }
fun FlowContent.buildMe2() =
        div(setOf("block", "deprecated")) {
            a(href = "http://kotlinlang.org") {
                target = ATarget.blank
                attributes["custom"] = "custom"
                +"test me"
            }
        }

object NullAppendable : Appendable {
    override fun append(csq: CharSequence?): Appendable = this

    override fun append(csq: CharSequence?, start: Int, end: Int): Appendable = this

    override fun append(c: Char): Appendable = this
}