import kotlinx.html.stream.escapeAppend
import kotlin.test.Test
import kotlin.test.assertEquals

class EscapeAppendTest {

    @Test
    fun testAppendEscaped() {
        assertEquals("&", escape("\\&"))
        assertEquals("\\a", escape("\\a"))
    }

    @Test
    fun testAppendAmp() {
        assertEquals("&amp;", escape("&"))
    }

    @Test
    fun testAppendId() {
        assertEquals("id", escape("id"))
    }

    @Test
    fun testMixedEscape() {
        assertEquals("&&amp;", escape("\\&&"))
        assertEquals("&amp;", escape("\\&amp;"))
    }

    @Test
    fun testEscapeSlash() {
        assertEquals("\\", escape("\\"))
    }

    @Test
    fun testEscapeUnicode() {
        assertEquals("\\u003d", escape("\\u003d"))
    }
}

private fun escape(string: String): String = buildString {
    escapeAppend(string)
}
