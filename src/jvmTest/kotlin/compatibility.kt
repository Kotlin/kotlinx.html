package kotlinx.html.tests

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.databind.node.TextNode
import java.io.File
import java.io.InputStreamReader
import kotlin.test.Test
import kotlin.test.assertEquals

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

    private fun loadResource(path: String): InputStreamReader =
        this::class.java.classLoader.getResourceAsStream(path)!!.reader()

    private fun ObjectMapper.sortByClass(node: JsonNode): JsonNode =
        createArrayNode().addAll((node as ArrayNode).sortedBy { (it["class"] as TextNode).textValue() })
}
