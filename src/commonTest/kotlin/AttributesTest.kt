import kotlinx.html.div
import kotlinx.html.stream.appendHTML
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AttributesTest {

    @Test
    fun testEscapedChar() {
        val dataTest = "Test: \\&hellip;"
        val dataTestAttribute: String?
        val html = buildString {
            appendHTML(false).div {
                attributes["data-test"] = dataTest
                dataTestAttribute = attributes["data-test"]
            }
        }

        val message = "<div data-test=\"Test: &hellip;\"></div>"
        assertEquals(message, html)
        assertEquals(dataTest, dataTestAttribute)
    }

    @Test
    fun testNonLetterNames() {
        val html = buildString {
            appendHTML(false).div {
                attributes["[quoted_bracket]"] = "quoted_bracket"
                attributes["(parentheses)"] = "parentheses"
                attributes["_underscore"] = "underscore"
                attributes["#pound"] = "pound"
                attributes["@alpine.attr"] = "alpineAttr"
            }
        }
        assertEquals(
            """
                <div [quoted_bracket]="quoted_bracket" (parentheses)="parentheses" _underscore="underscore" #pound="pound" @alpine.attr="alpineAttr"></div>
            """.trimIndent(),
            html,
        )
    }

    @Test
    fun testInvalidAttributeNames() {
        listOf(
            "", // Must not be empty
            "XMLAttr", // Cannot start with XML
            "xmlAttr", // That's case-insensitive btw
            "\"", // No double quotes
            "'", // No single quotes, either
            "a b", // No spaces
            "A\n", // No newline
            "A\t", // No tab
            "A\u000C", // No form feed
            "A>", // No greater-than sign
            "A/", // No forward-slash (solidus)
            "A=", // No equals sign
        ).forEach { attrName ->
            assertFailsWith<IllegalArgumentException>("Invalid attribute name '$attrName' validated!") {
                buildString {
                    appendHTML(false).div {
                        attributes[attrName] = "Should Fail!"
                    }
                }
            }
        }
    }
}
