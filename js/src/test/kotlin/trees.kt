package html4k.tests

import html4k.dom.append
import html4k.js.div
import html4k.p
import org.w3c.dom.asList
import kotlin.browser.document
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.Test as test

class DomTreeImplTest {
    test fun simpleTree() {
        val node = document.body!!.append.div {
            p {
                +"test"
            }
        }

        assertEquals("DIV", node.tagName)
        assertEquals(1, node.childNodes.length)
        assertEquals("P", node.children[0]?.tagName)

        assertTrue(document.body!!.children.length > 0)
        assertEquals(node, document.body!!.children.asList().last())
    }
}
