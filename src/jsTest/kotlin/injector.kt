package kotlinx.html.tests

import kotlinx.html.*
import kotlinx.html.dom.*
import kotlinx.html.injector.*
import kotlinx.html.js.*
import org.w3c.dom.*
import kotlinx.browser.*
import kotlin.properties.*
import kotlin.test.*

class MyBeanWithDiv {
    var node: HTMLDivElement by Delegates.notNull()
}

class MyBeanWithP {
    var p: HTMLParagraphElement by Delegates.notNull()
}

class ExampleBean {
    var myDiv: HTMLDivElement by Delegates.notNull()
    var myP: HTMLParagraphElement by Delegates.notNull()
}

class InjectToLateInitVarBean {
    lateinit var myP: HTMLParagraphElement
}

class InjectorTests {
    @Test fun injectByClass() {
        val bean = MyBeanWithDiv()
        val node = document.create.inject(bean, listOf(
                InjectByClassName("my-class") to MyBeanWithDiv::node
        )).div {
            classes = setOf("my-class")
        }

        val found: HTMLDivElement = node

        assertEquals("DIV", bean.node.tagName)
        assertEquals(found, bean.node)
    }

    @Test fun injectByClassFailed() {
        val bean = MyBeanWithDiv()
        document.create.inject(bean, listOf(
                InjectByClassName("my-class") to MyBeanWithDiv::node
        )).div {
            classes = setOf("other-class")
        }

        try {
            bean.node.tagName
            fail("node shouldn't be initialized")
        } catch (e: Throwable) {
            assertTrue(true)
        }
    }

    @Test fun injectByTagName() {
        val bean = MyBeanWithP()
        document.create.inject(bean, listOf(
                InjectByTagName("p") to MyBeanWithP::p
        )).div {
            p {
            }
        }

        assertEquals("P", bean.p.tagName)
    }

    @Test fun injectToLateInitVar() {
        val bean = InjectToLateInitVarBean()
        document.create.inject(bean, listOf(
                InjectByTagName("p") to InjectToLateInitVarBean::myP
        )).div {
            p {
            }
        }

        assertEquals("P", bean.myP.tagName)
    }

    @Test fun exampleFromWiki() {
        val bean = ExampleBean()

        document.create.inject(bean, listOf(
                InjectByClassName("my-class") to ExampleBean::myDiv,
                InjectByTagName("p") to ExampleBean::myP
        )).div {
            div("my-class") {
                p {
                    +"test"
                }
            }
        }

        assertNotNull(bean.myDiv)
        assertNotNull(bean.myP)
    }
}
