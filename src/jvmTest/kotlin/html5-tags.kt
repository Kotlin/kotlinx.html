package kotlinx.html.tests

import kotlinx.html.*
import kotlinx.html.dom.createHTMLDocument
import kotlinx.html.dom.serialize
import kotlin.test.Test
import kotlin.test.assertEquals
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
    assertEquals(
      """
                <!DOCTYPE html>
                <html>
                  <body>
                    <h1>header</h1>
                    <div>
                      content<span>yo</span>
                    </div>
                  </body>
                </html>""".trimIndent(), tree.serialize(true).trim().replace("\r\n", "\n")
    )
  }
}
