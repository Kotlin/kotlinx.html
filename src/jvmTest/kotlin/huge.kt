package kotlinx.html.tests

import kotlinx.html.FormEncType
import kotlinx.html.FormMethod
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.form
import kotlinx.html.h1
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.li
import kotlinx.html.meta
import kotlinx.html.p
import kotlinx.html.resetInput
import kotlinx.html.stream.appendHTML
import kotlinx.html.submitInput
import kotlinx.html.textInput
import kotlinx.html.title
import kotlinx.html.ul
import org.junit.Test
import kotlin.system.measureTimeMillis

open class HugeStreamingBenchmark {
    @Test
    fun testName() {
        val sb = StringBuilder(8192)
        val count = 100
        val time = measureTimeMillis {
            for (i in 1..count) {
                testHuge(sb)
            }
        }
        println("avg time is ${time.toDouble() / count} ms, ${count.toDouble() / time} per ms")
    }
    
    open fun testHuge(sb: StringBuilder) {
        sb.delete(0, sb.length)
        sb.appendHTML().html {
            head {
                title { +"My title" }
                
                meta("description", "my huge page")
                meta("keywords", "k1, k2, k3")
            }
            
            body {
                h1 { +"Title of the huge page" }
                div("container") {
                    div("panel-left") {
                        ul {
                            li { +"main" }
                            li { +"page1" }
                            li { +"page2" }
                            li { +"page3" }
                            li { +"page4" }
                        }
                    }
                    div("footer") {
                        p {
                            +"copyleft ...."
                        }
                    }
                    div("content") {
                        div("disclaimer") {
                            p("a") {
                                +"This is a disclaimer"
                            }
                        }
                        div("warning") {
                            p {
                                +"warning 1"
                            }
                        }
                        div("warning") {
                            p {
                                +"warning 1"
                            }
                        }
                        div("warning") {
                            p {
                                +"warning 1"
                            }
                        }
                        div("warning") {
                            p {
                                +"warning 1"
                            }
                        }
                        div("warning") {
                            p {
                                +"warning 1"
                            }
                        }
                        div("main-form") {
                            form("/post", FormEncType.applicationXWwwFormUrlEncoded, method = FormMethod.post) {
                                textInput { }
                                textInput { }
                                textInput { }
                                textInput { }
                                textInput { }
                                textInput { }
                                submitInput { }
                                resetInput { }
                            }
                        }
                        div("results") {
                            div("") {
                                p {
                                    +"ok"
                                }
                                p {
                                    for (i in 1..100) {
                                        +"ok, $i\n"
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
