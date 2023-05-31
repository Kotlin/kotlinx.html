import kotlinx.html.*
import kotlinx.html.consumers.*
import kotlinx.html.dom.*
import kotlinx.html.js.*
import kotlinx.browser.*
import kotlin.test.*

class TestExceptions {

    @Test
    fun `exception_handler_should_add_output`() {

        val container = document.body!!.append.catch { err ->

            div {
                +"ERROR: "
                +err.message!!
            }

        }.div {
            h1 {
                +" text "
                throw IllegalStateException("testing errors")
            }
            h2 {
                +" should be present "
            }
        }

        assertEquals(
            """<h1> text <div>ERROR: testing errors</div></h1><h2> should be present </h2>""",
            container.innerHTML.trimTagSpace()
        )
    }

    fun String.trimTagSpace() = replace(">\\s+<".toRegex(), "><")
}

