import kotlinx.html.*
import kotlinx.html.dom.*
import kotlinx.html.stream.*
import org.junit.*
import kotlin.test.*

class UnsafeContentTest {
    @Test
    fun testStream() {
        val text = StringBuilder().apply {
            appendHTML(false).html {
                unsafe {
                    +"<p>para</p>"
                }
            }
        }.toString()

        assertEquals("<html><p>para</p></html>", text)
    }

    @Test
    fun testStreamPlusAny() {
        val i = 2

        val text = StringBuilder().apply {
            appendHTML(false).html {
                unsafe {
                    +"1"
                    raw(i)
                    raw(3)
                    raw(Entities.nbsp)
                }
            }
        }.toString()

        assertEquals("<html>123&nbsp;</html>", text)
    }

    @Test
    fun testDOM() {
        val tree = createHTMLDocument().html {
            body {
                unsafe {
                    +"<p>para</p>"
                }
            }
        }

        assertEquals("<html><body><p>para</p></body></html>", tree.documentElement.serialize(false))
    }
}