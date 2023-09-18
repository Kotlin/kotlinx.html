import kotlinx.html.div
import kotlinx.html.stream.appendHTML
import kotlin.test.Test
import kotlin.test.assertEquals

class AttributesTest {

    @Test
    fun testEscapedChar() {
        val html = buildString {
            appendHTML(false).div {
                attributes["data-test"] = "Test: \\&hellip;"
            }
        }

        val message = "<div data-test=\"Test: &hellip;\"></div>"
        assertEquals(message, html)
    }
}