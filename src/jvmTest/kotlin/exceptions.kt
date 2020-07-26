import kotlinx.html.*
import kotlinx.html.consumers.*
import kotlinx.html.stream.*
import org.junit.Test
import kotlin.test.*

class TestExceptions {
  @Test
  fun `default exception must result in empty tag`() {
    
    val sb = StringBuilder()
    
    var errorCaught = false
    
    try {
      sb.appendHTML(prettyPrint = false).html {
        body {
          h1 {
            +" empty "
            throw IllegalStateException("testing errors")
          }
          h2 {
            +" should NOT be written "
          }
        }
      }
    } catch (err: IllegalStateException) {
      errorCaught = true
      assertEquals(err.message, "testing errors")
    }
    
    assertTrue(errorCaught, "Exception should be thrown")
    
    assertEquals(
      """<html><body><h1> empty </h1></body></html>""",
      sb.toString())
  }
  
  @Test
  fun `exception handler should add output`() {
    
    val sb = StringBuilder()
    sb.appendHTML(prettyPrint = false).catch { err ->
      
      div {
        +"ERROR: "
        +err.message!!
      }
      
    }.html {
      body {
        h1 {
          +" text "
          throw IllegalStateException("testing errors")
        }
        h2 {
          +" should be present "
        }
      }
    }
    
    assertEquals(
      """<html><body><h1> text <div>ERROR: testing errors</div></h1><h2> should be present </h2></body></html>""",
      sb.toString())
  }
}

