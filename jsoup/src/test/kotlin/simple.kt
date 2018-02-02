package kotlinx.html.tests

import kotlinx.html.body
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.id
import kotlinx.html.jsoup.tagName
import kotlinx.html.span
import org.jsoup.nodes.Document
import org.junit.Test
import kotlin.test.assertEquals

class JsoupTests {
    @Test
    fun `able to create simple document`() {
        val document = Document("")
    
        document.append {
            div {
                id = "test"
            }
        }
        
        assertEquals("div", document.getElementById("test").tagName)
    }
    
    @Test
    fun `able to create complex document`() {
        val document = Document("")
    
        document.append {
            body {
                div {
                    id = "test"
                    classes += "test"
                    
                    span {
                        +"test"
                    }
                }
            }
        }
        
        assertEquals("""
            <html>
                <body>
                    <div id="test" class="test">
                        <span>
                            test
                        </span>
                    </div>
                </body>
            </html>
        """.trimIndent(), document.toString().trimIndent())
    }
}
