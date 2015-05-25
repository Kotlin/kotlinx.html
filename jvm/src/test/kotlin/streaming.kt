package html4k.tests

import html4k.*
import html4k.consumers.filter
import html4k.consumers.trace
import html4k.stream.appendHTML
import java.io.StringWriter
import kotlin.test.assertEquals
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

        val result = sw.appendHTML(false).filter { when (it.tagName) {
            "div" -> DROP
            "pre" -> SKIP
            else -> PASS
        } }.html {
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

        assertEquals("<html><body><p>[pre.p]<a>[pre.p.pre.a]</a></p></body></html>", sw.toString())
    }
}