package kotlinx.html.tests

import kotlinx.html.*
import kotlinx.html.dom.*
import kotlin.test.*
import org.junit.Test as test

class Html5TagsTest {
    @Test
    fun able_to_create_main_tag() {
        val tree = createHTMLDocument().html {
            body {
                main(classes = "main-test") {
                    id = "test-node"

                    +"content"
                }
            }
        }

        print(tree.serialize(true).trim().replace("\r\n", "\n"))

        assertEquals(
            "<!DOCTYPE html>\n<html><body><main class=\"main-test\" id=\"test-node\">content</main></body></html>",
            tree.serialize(false)
        )
        assertEquals(
            """
                <!DOCTYPE html>
                <html>
                  <body>
                    <main class="main-test" id="test-node">content</main>
                  </body>
                </html>""".trimIndent(), tree.serialize(true).trim().replace("\r\n", "\n")
        )
    }

    @test
    fun `able to create complex tree and render it with pretty print`() {
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

        assertEquals(
            "<!DOCTYPE html>\n<html><body><h1>header</h1><div>content<span>yo</span></div></body></html>",
            tree.serialize(false)
        )
        val serialize = tree.serialize(true)
        assertEquals(
            """
                <!DOCTYPE html>
                <html>
                  <body>
                    <h1>header</h1>
                    <div>content<span>yo</span>
                    </div>
                  </body>
                </html>""".trimIndent(), serialize.trim().replace("\r\n", "\n")
        )
    }
}