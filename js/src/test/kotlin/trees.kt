package html4k.tests

import html4k.*
import html4k.consumers.trace
import html4k.dom.append
import html4k.dom.create
import html4k.js.div
import html4k.js.onClickFunction
import html4k.js.span
import org.w3c.dom.events.Event
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLSpanElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.asList
import kotlin.dom.asList
import kotlin.browser.document
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail
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

    test fun appendSingleNode() {
        val myDiv: HTMLDivElement = document.body!!.append.div {
            p {
                +"test"
            }
        }

        assertEquals("DIV", myDiv.tagName)
        assertEquals(document.body, myDiv.parentNode)
        assertEquals("<div><p>test</p></div>", myDiv.outerHTML.replace("\\s+".toRegex(), ""))
    }

    test fun appendNodeWithEventHandler() {
    	var clicked = false
    	
        document.body!!.append.div {
            onClickFunction = {
				clicked = true
            }
        }
        
        document.getElementsByTagName("div").asList().forEach {
        	if (it is HTMLElement) {
        		val clickHandler = it.onclick
        		if (clickHandler != null) {
        			clickHandler(uninitialized())
        		}
        	}
        }
        
        assertTrue(clicked)
    }

    test fun appendMultipleNodes() {
        val wrapper = wrapper()

        val nodes = wrapper.append {
            div {
                +"div1"
            }
            div {
                +"div2"
            }
        }

        assertEquals(2, nodes.size())
        nodes.forEach {
            assertTrue(it in wrapper.children.asList())
        }

        assertEquals("<div>div1</div><div>div2</div>", wrapper.innerHTML)
    }

    test fun appendEntity() {
        val wrapper = wrapper()
        wrapper.append.span {
            +Entities.nbsp
        }

        assertEquals("<span>&nbsp;</span>", wrapper.innerHTML)
    }

    test fun pastTagAtrributeChangedShouldBeProhibited() {
        try {
            document.body!!.append.trace().div {
                span {
                    p {
                        this@div.id = "d1"
                    }
                }
            }

            fail("We shouldn't be able to do that")
        } catch (expected: Throwable) {
            assertTrue(true)
        }
    }

    test fun buildBiggerPage() {
        val wrapper = wrapper()

        wrapper.append {
            h1 {
                +"kotlin"
            }
            p {
                +"Here we are"
            }
            div {
                classes = setOf("root")

                div {
                    classes = setOf("menu")

                    ul {
                        li { +"item1" }
                        li { +"item2" }
                        li { +"item3" }
                    }
                }
                div {
                    classes = setOf("content")
                }
            }
        }

        assertEquals("<h1>kotlin</h1>" +
                "<p>Here we are</p>" +
                "<div class=\"root\">" +
                "<div class=\"menu\">" +
                "<ul>" +
                "<li>item1</li>" +
                "<li>item2</li>" +
                "<li>item3</li>" +
                "</ul>" +
                "</div>" +
                "<div class=\"content\"></div>" +
                "</div>", wrapper.innerHTML)
    }

    private fun wrapper() = document.body!!.append.div {}
    private fun <T> uninitialized(): T = null as T 
}
