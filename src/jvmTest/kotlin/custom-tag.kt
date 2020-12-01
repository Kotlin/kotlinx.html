package kotlinx.html.tests

import kotlinx.html.DIV
import kotlinx.html.HTMLTag
import kotlinx.html.HtmlInlineTag
import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.span
import kotlinx.html.stream.appendHTML
import kotlinx.html.visit
import kotlinx.html.visitAndFinalize
import org.junit.Test
import kotlin.test.assertEquals

class CustomTagTest {
    @Test
    fun testCustomTagInDiv() {
        val html = StringBuilder().apply {
            appendHTML(false).div {
                custom {
                    span { +"content" }
                }
            }
        }.toString()

        assertEquals("<div><custom><span>content</span></custom></div>", html)
    }

    @Test
    fun testCustomTagRoot() {
        val html = StringBuilder().apply {
            appendHTML(false).custom {
                span {
                    +"content"
                }
            }
        }.toString()

        assertEquals("<custom><span>content</span></custom>", html)
    }
}

private class CUSTOM<E>(consumer: TagConsumer<*, E>) :
    HTMLTag<E>(
        "custom", consumer, emptyMap(),
        inlineTag = true,
        emptyTag = false
    ), HtmlInlineTag<E>

private fun <T, E> TagConsumer<T, E>.custom(block: CUSTOM<E>.() -> Unit = {}): T {
    return CUSTOM<E>(this).visitAndFinalize(this, block)
}

private fun <E> DIV<E>.custom(block: CUSTOM<E>.() -> Unit = {}) {
    CUSTOM(consumer).visit(block)
}
