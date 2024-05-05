import kotlinx.html.div
import kotlinx.html.stream.appendHTML
import kotlin.test.Test
import kotlin.test.assertEquals

class AttributesTest {

    @Test
    fun testEscapedChar() {
        val dataTest = "Test: \\&hellip;"
        val dataTestAttribute: String?
        val html = buildString {
            appendHTML(false).div {
                attributes["data-test"] = dataTest
                dataTestAttribute = attributes["data-test"]
            }
        }

        val message = "<div data-test=\"Test: &hellip;\"></div>"
        assertEquals(message, html)
        assertEquals(dataTest, dataTestAttribute)
    }
}