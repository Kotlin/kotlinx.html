package kotlinx.html.tests

import kotlinx.html.*
import kotlinx.html.consumers.filter
import kotlinx.html.dom.*
import kotlinx.html.dom.createHTMLDocument
import kotlin.test.assertEquals
import org.junit.Test as test

class TestDOMTrees {
    @test fun `able to create simple tree`() {
        val tree = createHTMLDocument().div {
            id = "test-node"
            +"content"
        }

        assertEquals("div", tree.getElementById("test-node")?.tagName?.toLowerCase())
    }

    @test fun `able to create complex tree and render it with pretty print`() {
        val tree = createHTMLDocument().html {
            body {
                h1 {
                    +"header"
                }
                div {
                    +"content"
                    span {
                        +"yo"
                    }
                }
            }
        }

        assertEquals("<!DOCTYPE html>\n<html><body><h1>header</h1><div>content<span>yo</span></div></body></html>", tree.serialize(false))
        assertEquals("""
                <!DOCTYPE html>
                <html>
                  <body>
                    <h1>header</h1>
                    <div>content<span>yo</span>
                    </div>
                  </body>
                </html>""".trimIndent(), tree.serialize(true).trim().replace("\r\n", "\n"))
    }

    @test fun `vals create and append support`() {
        val document = createHTMLDocument().html {
            body {
                div {
                    id = "content"
                }
            }
        }

        val contentNode = document.getElementById("content")!!
        contentNode.append.p {
            +"p1"
        }

        val p2 = document.create.p {
            +"p2"
        }
        contentNode.appendChild(p2)

        assertEquals("""<!DOCTYPE html>
<html>
  <body>
    <div id="content">
      <p>p1</p>
      <p>p2</p>
    </div>
  </body>
</html>
        """.trim().replace("\r\n", "\n"), document.serialize(true).trim().replace("\r\n", "\n"))
    }

    @test fun `append function support`() {
        val document = createHTMLDocument().html {
            body {
                div {
                    id = "content"
                }
            }
        }

        val contentNode = document.getElementById("content")!!

        val nodes = contentNode.append {
            p {
                +"p1"
            }
            p {
                +"p2"
                p {
                    +"p3"
                }
            }
        }

        assertEquals(2, nodes.size)

        assertEquals("""<!DOCTYPE html>
<html>
  <body>
    <div id="content">
      <p>p1</p>
      <p>p2<p>p3</p>
      </p>
    </div>
  </body>
</html>
        """.trim().replace("\r\n", "\n"), document.serialize(true).trim().replace("\r\n", "\n"))
    }

    @test fun `should compile wiki example`() {
        println(document {
            append.filter { if (it.tagName == "div") SKIP else PASS }.html {
                body {
                    div {
                        a { +"link1" }
                    }
                    a { +"link2" }
                }
            }
        }.serialize())
    }

    @test fun `svg should have namespace`() {
        val d = document {
            append.html {
                body {
                    svg {
                    }
                }
            }
        }

        assertEquals("<!DOCTYPE html>\n<html><body><svg xmlns=\"http://www.w3.org/2000/svg\"></svg></body></html>",
                d.serialize(false).trim().replace("\r\n", "\n"))
    }

    @test fun `generalize tests`() {
        fun <T> T.genericFlow() where T : HtmlBlockTag {
            classes += "aha"
            +"content"
            +Entities.nbsp
            div {
            }
        }

        fun <T> T.genericPhrasing() where T : HtmlInlineTag {
            classes += "aha"
            +"content"
            +Entities.nbsp
            span { }
        }

        fun <T> T.genericMetaData() where T : HtmlHeadTag {
            classes += "aha"
            +"content"
            +Entities.nbsp
            meta("a")
            script(ScriptType.textJavaScript) { }
        }

        document {
            append.html {
                head {
                    genericMetaData()
                }
                body {
                    div {
                        genericFlow()
                        p {
                            genericPhrasing()
                        }
                    }
                }
            }
        }
    }
}