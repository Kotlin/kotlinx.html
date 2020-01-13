package kotlinx.html.tests

import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.node.*
import java.io.*
import kotlin.test.*

class CompatibilityTest {
    // NOTE: this test shouldn't be started from IDEA because incremental compiler will only dump
    // changed declarations
    @Test
    fun binaryCompatibilityTest() {
        val objectMapper = ObjectMapper()

        val expectedJson = objectMapper.readTree(loadResource("declarations.json"))
        val actualJson = objectMapper.readTree(File("build/declarations.json").readText())

        val expected = objectMapper.sortByClass(expectedJson)
        val actual = objectMapper.sortByClass(actualJson)

        if (expected != actual) {
            // Show text diff.
            assertEquals(expected.toPrettyString(), actual.toPrettyString())
        }
    }

    private fun loadResource(path: String): InputStreamReader
            = this::class.java.classLoader.getResourceAsStream(path)!!.reader()

    private fun ObjectMapper.sortByClass(node: JsonNode): JsonNode
            = createArrayNode().addAll((node as ArrayNode).sortedBy { (it["class"] as TextNode).textValue() })
}
