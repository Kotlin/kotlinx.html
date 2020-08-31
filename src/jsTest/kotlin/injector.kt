package kotlinx.html.tests

import kotlinx.browser.document
import kotlinx.html.classes
import kotlinx.html.div
import kotlinx.html.dom.create
import kotlinx.html.injector.InjectByClassName
import kotlinx.html.injector.InjectByTagName
import kotlinx.html.injector.inject
import kotlinx.html.js.div
import kotlinx.html.p
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLParagraphElement
import kotlin.properties.Delegates
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail
import kotlin.test.Test as test

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
    @test
    fun injectByClass() {
        val bean = MyBeanWithDiv()
        val node = document.create.inject(
            bean, listOf(
                InjectByClassName("my-class") to MyBeanWithDiv::node
            )
        ).div {
            classes = setOf("my-class")
        }
        
        val found: HTMLDivElement = node
        
        assertEquals("DIV", bean.node.tagName)
        assertEquals(found, bean.node)
    }
    
    @test
    fun injectByClassFailed() {
        val bean = MyBeanWithDiv()
        document.create.inject(
            bean, listOf(
                InjectByClassName("my-class") to MyBeanWithDiv::node
            )
        ).div {
            classes = setOf("other-class")
        }
        
        try {
            bean.node.tagName
            fail("node shouldn't be initialized")
        } catch (e: Throwable) {
            assertTrue(true)
        }
    }
    
    @test
    fun injectByTagName() {
        val bean = MyBeanWithP()
        document.create.inject(
            bean, listOf(
                InjectByTagName("p") to MyBeanWithP::p
            )
        ).div {
            p {
            }
        }
        
        assertEquals("P", bean.p.tagName)
    }
    
    @test
    fun injectToLateInitVar() {
        val bean = InjectToLateInitVarBean()
        document.create.inject(
            bean, listOf(
                InjectByTagName("p") to InjectToLateInitVarBean::myP
            )
        ).div {
            p {
            }
        }
        
        assertEquals("P", bean.myP.tagName)
    }
    
    @test
    fun exampleFromWiki() {
        val bean = ExampleBean()
        
        document.create.inject(
            bean, listOf(
                InjectByClassName("my-class") to ExampleBean::myDiv,
                InjectByTagName("p") to ExampleBean::myP
            )
        ).div {
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
