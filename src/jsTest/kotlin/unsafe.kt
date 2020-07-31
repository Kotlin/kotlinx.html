package kotlinx.html.tests.unsafes

import kotlinx.browser.document
import kotlinx.html.div
import kotlinx.html.dom.createTree
import kotlinx.html.html
import kotlinx.html.stream.appendHTML
import kotlinx.html.unsafe
import kotlin.test.Test
import kotlin.test.assertEquals

class UnsafeContentTest {
  @Test
  fun testStream() {
    val text = StringBuilder().apply {
      appendHTML(false).html {
        unsafe {
          +"<p>para</p>"
        }
      }
    }.toString()
  
    assertEquals("<html><p>para</p></html>", text)
  }
  
  @Test
  fun testDOM() {
    val tree = document.createTree().div {
      unsafe {
        +"<p>para</p>"
      }
    }
    
    assertEquals("<p>para</p>", tree.innerHTML)
    assertEquals("<div><p>para</p></div>", tree.outerHTML)
  }
  
  @Test
  fun testDOMMultiple() {
    val tree = document.createTree().div {
      unsafe {
        +"<p>para1</p>"
        +"<p>para2</p>"
      }
      unsafe {
        +"<p>para3</p>"
      }
    }
    
    assertEquals("<p>para1</p><p>para2</p><p>para3</p>", tree.innerHTML)
  }
}
