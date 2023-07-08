package kotlinx.html.tests

import kotlinx.html.*
import kotlinx.html.stream.*
import org.junit.Test
import kotlin.test.*

class FragmentTest {
    @Test
    fun testFragment() {
        val html = buildString {
            appendHTML(false).fragment {
                tr {
                    +"One"
                }
                tr {
                    +"Two"
                }
                tr {
                    +"Three"
                }
            }
        }

        assertEquals("<tr>One</tr><tr>Two</tr><tr>Three</tr>", html)
    }
}
