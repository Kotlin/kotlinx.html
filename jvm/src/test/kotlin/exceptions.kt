import kotlinx.html.*
import kotlinx.html.stream.appendHTML
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TestExceptions {
    @Test fun `default exception must result in empty tag`() {

        val sb = StringBuilder()

        var errorCaught = false

        try {
            sb.appendHTML(prettyPrint = false).html {
                body {
                    h1 {
                        +"empty"
                        throw IllegalStateException("testing errors")
                    }
                    h2 {
                        +"should NOT be written"
                    }
                }
            }
        } catch (err: IllegalStateException) {
            errorCaught = true
            assertEquals(err.message, "testing errors")
        }

        assertTrue(errorCaught, "Exception should be not re-thrown")

        assertEquals(
                "<html><body><h1>empty</h1></body></html>",
                sb.toString())
    }

    @Test fun `exception handler should add output`() {

        val sb = StringBuilder()
        TestExceptionConsumer(sb.appendHTML(prettyPrint = false)).html {
            body {
                h1 {
                    throw IllegalStateException("testing errors")
                }
                h2 {
                    +"should be present"
                }
            }
        }

        assertEquals(
                "<html><body><h1><div>error was handled</div></h1><h2>should be present</h2></body></html>",
                sb.toString())
    }
}

private class TestExceptionConsumer<R>(val underlying: TagConsumer<R>) : TagConsumer<R> by underlying {
    override fun onError(tag: Tag, exception: Exception) {

        div { +"error was handled" }

    }
}
