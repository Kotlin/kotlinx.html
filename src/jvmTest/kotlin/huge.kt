package kotlinx.html.tests

import kotlinx.html.*
import kotlinx.html.stream.*
import org.junit.*
import kotlin.system.*

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
