package html4k.tests

import html4k.dom.create
import html4k.injector.InjectByClassName
import html4k.injector.InjectByTagName
import html4k.injector.inject
import html4k.injector.injectTo
import html4k.js.div
import html4k.p
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLParagraphElement
import kotlin.browser.document
import kotlin.properties.Delegates
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail
import org.junit.Test as test

class MyBeanWithDiv {
    var node: HTMLDivElement by Delegates.notNull()
}

class MyBeanWithP {
    var p: HTMLParagraphElement by Delegates.notNull()
}

class InjectorTests {
    test fun injectByClass() {
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

    test fun injectByClassFailed() {
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

    test fun injectByTagName() {
        val bean = MyBeanWithP()
        document.create.inject(bean, listOf(
                InjectByTagName("p") to MyBeanWithP::p
        )).div {
            p {
            }
        }

        assertEquals("P", bean.p.tagName)
    }
}
