import kotlinx.html.*
import kotlinx.html.dom.*
import kotlinx.html.stream.*
import org.junit.*
import kotlin.test.*

class UnsafeContentTest {
    @Test
    fun testStream() {
        val text = StringBuilder {
            appendHTML(false).html {
                unsafe {
                    +"<p>para</p>"
                }
            }
        }.toString()

        assertEquals("<html><p>para</p></html>", text)
    }

    @Test
    @Ignore // not supported yet
    fun testDOM() {
        val tree = createHTMLDocument().html {
            body {
                unsafe {
                    +"<p>para</p>"
                }
            }
        }

        assertEquals("<body><p>para</p></body>", tree.documentElement.serialize(false))
    }
}