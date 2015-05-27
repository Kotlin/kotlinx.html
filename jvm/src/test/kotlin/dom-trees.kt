package html4k.tests

import html4k.*
import html4k.dom.*
import html4k.dom.createHTMLDocument
import kotlin.test.assertEquals
import org.junit.Test as test

class TestDOMTrees {
    test fun `able to create simple tree`() {
        val tree = createHTMLDocument().div {
            id = "test-node"
            +"content"
        }

        assertEquals("div", tree.getElementById("test-node")?.getTagName()?.toLowerCase())
    }

    test fun `able to create complex tree and render it with pretty print`() {
        val tree = createHTMLDocument().html {
            body {
                h1 {
                    +"header"
                }
                div {
                    +"content"
                    span {
                        +"yo"
                    }
                }
            }
        }

        assertEquals("<!DOCTYPE html>\n<html><body><h1>header</h1><div>content<span>yo</span></div></body></html>", tree.serialize(false))
        assertEquals("<!DOCTYPE html>\n" +
                "<html>\n" +
                "  <body>\n" +
                "    <h1>header</h1>\n" +
                "    <div>content<span>yo</span>\n" +
                "    </div>\n" +
                "  </body>\n" +
                "</html>", tree.serialize(true).trim())
    }
}