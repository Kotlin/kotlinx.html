import kotlinx.html.stream.escapeAppend
import kotlin.test.Test
import kotlin.test.assertEquals

class EscapeAppendTest {

    @Test
    fun testAppendEscaped() {
        val result = buildString {
            escapeAppend("\\&")
        }

        assertEquals("&", result)
    }

    @Test
    fun testAppendAmp() {
        val result = buildString {
            escapeAppend("&")
        }

        assertEquals("&amp;", result)
    }

    @Test
    fun testAppendId() {
        val result = buildString {
            escapeAppend("id")
        }

        assertEquals("id", result)
    }
}