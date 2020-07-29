package kotlinx.html.tests

import kotlinx.html.*
import org.junit.Test
import kotlin.test.assertEquals

class CustomTagTest {
  @Test
  fun testCustomTagInDiv() {
    val html = buildString {
      appendHTML(false).div {
        custom {
          span { +"content" }
        }
      }
    }

    assertEquals("<div><custom><span>content</span></custom></div>", html)
  }

  @Test
  fun testCustomTagRoot() {
    val html =
        buildString {
          appendHTML(false).custom {
            span {
              +"content"
            }
          }
        }

    assertEquals("<custom><span>content</span></custom>", html)
  }
}

private class CUSTOM(consumer: TagConsumer<*>) :
    HTMLTag(
        "custom", consumer, emptyMap(),
        inlineTag = true,
        emptyTag = false
    ), HtmlInlineTag

private fun <T> TagConsumer<T>.custom(block: CUSTOM.() -> Unit = {}): T {
  return CUSTOM(this).visitAndFinalize(this, block)
}

private fun DIV.custom(block: CUSTOM.() -> Unit = {}) {
  CUSTOM(consumer).visit(block)
}
