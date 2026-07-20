package kotlinx.html.tests

import kotlinx.html.div
import kotlinx.html.table
import kotlinx.html.th
import kotlinx.html.tr
import kotlinx.html.stream.appendHTML
import kotlin.test.Test
import kotlin.test.assertEquals

class ThFlowContentTest {
    @Test
    fun testThAllowsDiv() {
        val html = buildString {
            appendHTML(false).table {
                tr {
                    th {
                        div { +"th div" }
                    }
                }
            }
        }

        assertEquals("<table><tr><th><div>th div</div></th></tr></table>", html)
    }

    @Test
    fun testThAllowsText() {
        val html = buildString {
            appendHTML(false).table {
                tr {
                    th { +"th text" }
                }
            }
        }

        assertEquals("<table><tr><th>th text</th></tr></table>", html)
    }
}
