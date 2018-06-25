package kotlinx.html.tests

import java.io.*
import kotlin.test.*

class CompatibilityTest {
    // NOTE: this test shouldn't be started from IDEA because incremental compiler will only dump
    // changed declarations
    @Test
    fun binaryCompatibilityTest() {
        val expected = this::class.java.classLoader.getResourceAsStream("declarations.json")!!.reader().readText()
        val actual = File("target/declarations.json").readText()

        assertEquals(expected, actual)
    }
}
