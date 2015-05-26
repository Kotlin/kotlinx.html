package html4k.tests

import html4k.dom.append
import html4k.js.div
import html4k.js.onClickFunction
import html4k.p
import org.w3c.dom.events.Event
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.asList
import kotlin.dom.asList
import kotlin.browser.document
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
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
    
    private fun <T> uninitialized(): T = null as T 
}
