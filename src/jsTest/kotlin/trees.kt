package kotlinx.html.tests

import kotlinx.html.*
import kotlinx.html.consumers.*
import kotlinx.html.dom.*
import kotlinx.html.js.*
import org.w3c.dom.*
import kotlin.browser.*
import kotlin.dom.*
import kotlin.test.*
import kotlin.test.Test as test

class DomTreeImplTest {
    @test fun simpleTree() {
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

    @test fun appendSingleNode() {
        val myDiv: HTMLDivElement = document.body!!.append.div {
            p {
                +"test"
            }
        }

        assertEquals("DIV", myDiv.tagName)
        assertEquals(document.body, myDiv.parentNode)
        assertEquals("<div><p>test</p></div>", myDiv.outerHTML.replace("\\s+".toRegex(), ""))
    }

    @test fun appendNodeWithEventHandler() {
    	var clicked = false
    	
        document.body!!.append.div {
            onClickFunction = {
				clicked = true
            }
        }
        document.create.div("a b c ") {
            a("http://kotlinlang.org") { +"official Kotlin site" }
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

    @test fun testAtMainPage() {
        val containerCreated = document.body!!.append.div {
            id = "container"
        }

        val myDiv = document.create.div("panel") {
            p {
                +"Here is "
                a("http://kotlinlang.org") { +"official Kotlin site" }
            }
        }

        val container = document.getElementById("container")
        if (container == null) {
            fail("container not found")
        }

        container.appendChild(myDiv)

        assertEquals("<div class=\"panel\"><p>Here is <a href=\"http://kotlinlang.org\">official Kotlin site</a></p></div>", container.innerHTML)
    }

    @test fun appendMultipleNodes() {
        val wrapper = wrapper()

        val nodes = wrapper.append {
            div {
                +"div1"
            }
            div {
                +"div2"
            }
        }

        assertEquals(2, nodes.size)
        nodes.forEach {
            assertTrue(it in wrapper.children.asList())
        }

        assertEquals("<div>div1</div><div>div2</div>", wrapper.innerHTML)
    }

    @test fun appendEntity() {
        val wrapper = wrapper()
        wrapper.append.span {
            +Entities.nbsp
        }

        assertEquals("<span>&nbsp;</span>", wrapper.innerHTML)
    }

    @test fun pastTagAttributeChangedShouldBeProhibited() {
        try {
            document.body!!.append.trace().div {
                p {
                    span {
                        this@div.id = "d1"
                    }
                }
            }

            fail("We shouldn't be able to modify attribute for outer tag")
        } catch (expected: Throwable) {
            assertTrue(true)
        }
    }

    @test fun buildBiggerPage() {
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

        assertEquals("""
                <h1>kotlin</h1>
                <p>Here we are</p>
                <div class="root">
                <div class="menu">
                <ul>
                <li>item1</li>
                <li>item2</li>
                <li>item3</li>
                </ul>
                </div>
                <div class="content"></div>
                </div>""".trimLines(), wrapper.innerHTML)
    }

    @test fun testAppendAndRemoveClass() {
        val wrapper = wrapper()

        wrapper.append {
            span("class1") {
                classes += "class2"
                classes -= "class1"
            }
        }

        assertEquals("<span class=\"class2\"></span>", wrapper.innerHTML)
    }

    @test fun testSvg() {
        val wrapper = wrapper()

        wrapper.append.svg {
        }

        @Suppress("UNCHECKED_CAST")
        assertEquals("http://www.w3.org/2000/svg", (wrapper.childNodes.asList() as List<Element>).first { it.tagName.toLowerCase() == "svg" }.namespaceURI)
    }

    @test fun assignEvent() {
        val wrapper = wrapper()
        var invoked = false

        wrapper.append {
            form {
                id = "my-form"
                onSubmitFunction = { event ->
                    invoked = true
                }
            }
        }

        val event = document.createEvent("Event")
        event.initEvent("submit", true, true)

        println("Got event $event")

        (wrapper.getElementsByTagName("form").asList().first { it.id == "my-form" } as HTMLFormElement).dispatchEvent(event)

        assertTrue { invoked }
    }

    @test fun testTdThColColGroupCreation() {
        val td = document.create.td()
        val th = document.create.th()
        val col = document.create.col()
        val colGroup = document.create.colGroup()

        assertEquals("TH", th.tagName.toUpperCase())
        assertEquals("TD", td.tagName.toUpperCase())
        assertEquals("COL", col.tagName.toUpperCase())
        assertEquals("COLGROUP", colGroup.tagName.toUpperCase())
    }

    @test fun testPrepend() {
        val wrapper = wrapper()
        wrapper.appendChild(document.createElement("A").apply { textContent = "aaa" })

        val nodes = wrapper.prepend {
            p {
                text("OK")
            }
        }

        assertEquals("<p>OK</p><a>aaa</a>", wrapper.innerHTML)
    }

    @test fun testComment() {
        val wrapper = wrapper()
        wrapper.append.div {
            comment("commented")
        }

        assertEquals("<div><!--commented--></div>", wrapper.innerHTML)
    }

    private fun wrapper() = document.body!!.append.div {}
    private fun <T> uninitialized(): T = null as T
    private fun String.trimLines() = trimIndent().lines().filter { it.isNotBlank() }.joinToString("")
}
