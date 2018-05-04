package kotlinx.html.tests

import kotlinx.html.*
import kotlinx.html.consumers.*
import kotlinx.html.stream.*
import java.io.*
import kotlin.test.*
import org.junit.Test as test

class TestStreaming {
    @test fun `we should be able to construct at least simple things`() {
        assertEquals(
                "<html>\n  <body>\n    <h1>Test me</h1>\n  </body>\n</html>\n",
                StringBuilder().apply {
                    appendHTML().html {
                        body {
                            h1 {
                                +"Test me"
                            }
                        }
                    }
                }.toString())
    }

    @test fun `we should be able append multiple htmls`() {
        assertEquals(
                "<html>\n  <body>\n    <h1>Test me</h1>\n  </body>\n</html>\n<html></html>\n",
                StringBuilder().apply {
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

    @test(expected = IllegalStateException::class)
    fun `we shouldn't be able to change attributes of other tag`() {
        StringBuilder().apply {
            appendHTML().html {
                body {
                    div {
                        this@body.id = "kotlin"
                    }
                }
            }
        }
    }

    @test fun `we should be able to write to different appendable`() {
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

    @test fun `we should be able filter tags`() {
        val sw = StringWriter()

        val result = sw.appendHTML(false).filter {
            when (it.tagName) {
                "div" -> DROP
                "span", "footer" -> SKIP
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
                footer {
                    p {
                        +"[footer.p]"
                        span {
                            a {
                                +"[footer.p.span.a]"
                            }
                        }
                    }
                    div {
                        p {
                            +"[footer.div.p]"
                        }
                    }
                }
            }
        }

        val sw2: StringWriter = result // note: this is to ensure result type is valid at compile time

        assertEquals("<html><body><p>[footer.p]<a>[footer.p.span.a]</a></p></body></html>", sw2.toString())
    }

    @test fun `we should be able to handle many requests`() {
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

    @test fun `we should be able to make extension functions with DSL`() {
        assertEquals("<html><body><div class=\"block deprecated\"><a href=\"http://kotlinlang.org\" target=\"_blank\" custom=\"custom\">test me</a></div></body></html>", StringBuilder().appendHTML(false).buildMe().toString())
    }

    @test fun `empty tag should have attributes`() {
        assertEquals("<div id=\"d\"></div>", StringBuilder().appendHTML(false).div { id = "d" }.toString())
        assertEquals("<div id=\"d\"><div id=\"d2\"></div></div>", StringBuilder().appendHTML(false).div { id = "d"; div { id = "d2" } }.toString())
    }

    @test fun `test attributes order`() {
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

    @test fun `multiple attributes and custom attribute present`() {
        assertEquals("<div id=\"d1\" custom=\"c1\" class=\"c1 c2\"></div>", StringBuilder().appendHTML(false).div {
            id = "d1"
            attributes["custom"] = "c1"
            classes = linkedSetOf("c1", "c2")
        }.toString())
    }

    @test fun `test tags order`() {
        assertEquals("<div><p><span></span></p></div>", StringBuilder().appendHTML(false).div { p { span {} } }.toString())
    }

    @test fun `test generated enum could be used`() {
        assertEquals("<link rel=\"Stylesheet\" href=\"/path\">", StringBuilder().appendHTML(false).link {
            rel = LinkRel.stylesheet
            href = "/path"
        }.toString())
    }

    @test fun `anchor with href syntax`() {
        assertEquals("<a href=\"a.html\">text</a>", StringBuilder().appendHTML(false).a("a.html") {
            +"text"
        }.toString())
    }

    @test fun `multiple content`() {
        assertEquals("<span>AAAbbb...</span>", StringBuilder().appendHTML(false).span {
            +"AAA"
            +"bbb."
            +".."
        }.toString())
    }

    @test fun `content with entity`() {
        assertEquals("<span>before&amp;after</span>", StringBuilder().appendHTML(false).span {
            +"before"
            +Entities.amp
            +"after"
        }.toString())
    }

    @test fun `test form with button`() {
        assertEquals("<form action=\"/someurl\">" +
                "<input type=\"checkbox\" name=\"cb1\">var1" +
                "<input type=\"checkbox\" name=\"cb2\" disabled=\"disabled\">var2" +
                "<input type=\"submit\" value=\"Go!\">" +
                "</form>",
                StringBuilder().appendHTML(false).form("/someurl") {
                    checkBoxInput(name = "cb1") {
                        +"var1"
                    }
                    input(type = InputType.checkBox, name = "cb2") {
                        disabled = true
                        +"var2"
                    }

                    submitInput {
                        value = "Go!"
                    }
                }.toString())
    }

    @test fun `test measure consumer with loop inside`() {
        val count = 1000
        val rs = StringBuilder(26 * (count + 1)).appendHTML(false).measureTime().div {
            for (i in 1..count) {
                div {
                    p { +"node$i" }
                }
            }
        }

        assertTrue(rs.time > 0)
        assertTrue(rs.time < count.toLong())

        val expected = StringBuilder().apply {
            append("<div>")
            for (i in 1..count) {
                append("<div><p>")
                append("node")
                append(i)
                append("</p></div>")
            }
            append("</div>")
        }

        assertEquals(expected.toString(), rs.result.toString())
    }

    @test fun `escape bad chars`() {
        assertEquals("<div id=\"bad&quot;\" custom=\"bad&amp;&quot;\" onevent=\"fire('evt')\">" +
                "content&lt;script&gt;" +
                "</div>",
                StringBuilder().appendHTML(false).div {
                    id = "bad\""
                    attributes["custom"] = "bad&\""
                    attributes["onevent"] = "fire('evt')"
                    +"content<script>"
                }.toString())
    }

    @test(expected = IllegalArgumentException::class)
    fun `bad chars in attribute name`() {
        StringBuilder().appendHTML().div {
            attributes["bad'char"] = "test"
        }
    }

    @test(expected = IllegalArgumentException::class)
    fun `bad chars 'equals' in attribute name`() {
        StringBuilder().appendHTML().div {
            attributes["bad=char"] = "test"
        }
    }

    @test fun `we should print empty tags with no close tag`() {
        assertEquals("<img src=\"my.jpg\">", StringBuilder().appendHTML(
                prettyPrint = false
        ).img(src = "my.jpg").toString())
    }

    @test fun `we should print empty tags with close tag if xhtmlCompatible flag is set to true`() {
        assertEquals("<img src=\"my.jpg\"/>", StringBuilder().appendHTML(
                prettyPrint = false,
                xhtmlCompatible = true
        ).img(src = "my.jpg").toString())
    }

    @test fun `pretty print should take into account inline tags`() {
        val text = StringBuilder().apply {
            appendHTML().div {
                +"content"
                span {
                    +"y"
                }
            }
        }.toString()

        assertEquals("<div>content<span>y</span></div>", text.trim())
    }

    @test fun `pretty print should work`() {
        assertEquals("<div>\n" +
                "  <div>content</div>\n" +
                "  <div>\n" +
                "    <div></div>\n" +
                "  </div>\n" +
                "</div>",
                StringBuilder().appendHTML(true).div {
                    div {
                        +"content"
                    }
                    div {
                        div {
                        }
                    }
                }.toString().trim())
    }

    @test fun `ticker attribute modification should work properly`() {
        assertEquals("<input type=\"checkbox\" checked=\"checked\">", createHTML(false).input {
            type = InputType.checkBox
            checked = true
        })

        assertEquals("<input type=\"checkbox\">", createHTML(false).input {
            type = InputType.checkBox
            checked = true
            checked = false
        })
    }

    @test fun `meta tag should have name and content suggested attributes`() {
        assertEquals("<meta name=\"name\" content=\"content\">", createHTML(false).meta("name", "content"))
        assertEquals("<head><meta name=\"name\" content=\"content\"></head>", createHTML(false).head {
            meta("name", "content")
        })
    }

    @test fun `we should be able to create div with no body`() {
        assertEquals("<div></div>", createHTML(false).div())
        assertEquals("<div><div></div></div>", createHTML(false).div {
            div()
        })
    }

    @test fun `meta tag example`() {
        createHTML(true).html {
            head {
                meta {
                    charset = "utf-8"
                }
                meta("viewport", "device-width, initial-scale=1.0")
                link(LinkRel.stylesheet, "/main.css")
            }
            body {
            }
        }
    }

    @test fun `svg should have namespace`() {
        val t = createHTML(false).html {
            body {
                svg {
                }
            }
        }

        assertEquals("<html><body><svg xmlns=\"http://www.w3.org/2000/svg\"></svg></body></html>", t)
    }

    @test fun `pretty print`() {
        val x = StringBuilder().appendHTML().html {
            body {
                article {
                    p {
                    }
                    a {
                        +"aaa"
                    }
                    span { }
                }
            }
        }.toString()

        assertEquals("""
            <html>
              <body>
                <article>
                  <p></p>
            <a>aaa</a><span></span></article>
              </body>
            </html>
            """.trimIndent(), x.trimEnd())
    }

    @test fun testHtmlWithNamespace() {
        val x = createHTML().html(namespace = "test") {
            body {
            }
        }

        assertEquals("""
            <html xmlns="test">
              <body></body>
            </html>
        """.trimIndent(), x.trimEnd())
    }

    @test fun testComment() {
        val x = createHTML().html {
            comment("commented")
            body {  }
        }

        assertEquals("""
            <html>
              <!--commented-->
              <body></body>
            </html>
        """.trimIndent(), x.trimEnd())
    }
}

fun <T> TagConsumer<T>.buildMe() = html { body { buildMe2() } }
fun FlowContent.buildMe2() =
        div("block deprecated") {
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