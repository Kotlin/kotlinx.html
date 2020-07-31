package kotlinx.html.stream

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.Unsafe

internal actual class HTMLStreamBuilder<out O : Appendable> actual constructor(
  val out: O,
  val prettyPrint: Boolean,
  val xhtmlCompatible: Boolean
) :
  TagConsumer<O, Nothing> {
  private var level = 0
  private var ln = true
  
  actual override fun onTagStart(tag: Tag<Nothing>) {
    if (prettyPrint && !tag.inlineTag) {
      indent()
    }
    level++
    
    out.append("<")
    out.append(tag.tagName)
    
    if (tag.namespace != null) {
      out.append(" xmlns=\"")
      out.append(tag.namespace)
      out.append("\"")
    }
    
    if (tag.attributes.isNotEmpty()) {
      tag.attributesEntries.forEachIndexed { _, e ->
        if (!e.key.isValidXmlAttributeName()) {
          throw IllegalArgumentException("Tag<Nothing> ${tag.tagName} has invalid attribute name ${e.key}")
        }
        
        out.append(' ')
        out.append(e.key)
        out.append("=\"")
        out.escapeAppend(e.value)
        out.append('\"')
      }
    }
    
    if (xhtmlCompatible && tag.emptyTag) {
      out.append("/")
    }
    
    out.append(">")
    ln = false
  }
  
  actual override fun onTagAttributeChange(tag: Tag<Nothing>, attribute: String, value: String?) {
    throw UnsupportedOperationException("tag attribute can't be changed as it was already written to the stream. Use with DelayedConsumer to be able to modify attributes")
  }
  
  actual override fun onTagEnd(tag: Tag<Nothing>) {
    level--
    if (ln) {
      indent()
    }
    
    if (!tag.emptyTag) {
      out.append("</")
      out.append(tag.tagName)
      out.append(">")
    }
    
    if (prettyPrint && !tag.inlineTag) {
      appendln()
    }
  }
  
  actual override fun onTagContent(content: CharSequence) {
    out.escapeAppend(content)
    ln = false
  }
  
  actual override fun onTagContentEntity(entity: Entities) {
    out.append(entity.text)
    ln = false
  }
  
  actual override fun finalize(): O = out
  
  actual override fun onTagContentUnsafe(block: Unsafe.() -> Unit) {
    UnsafeImpl.block()
  }
  
  actual override fun onTagComment(content: CharSequence) {
    if (prettyPrint) {
      indent()
    }
    
    out.append("<!--")
    out.escapeComment(content)
    out.append("-->")
    
    ln = false
  }
  
  actual val UnsafeImpl = object : Unsafe {
    override operator fun String.unaryPlus() {
      out.append(this)
    }
  }
  
  private fun appendln() {
    if (prettyPrint && !ln) {
      out.append("\n")
      ln = true
    }
  }
  
  private fun indent() {
    if (prettyPrint) {
      if (!ln) {
        out.append("\n")
      }
      var remaining = level
      while (remaining >= 4) {
        out.append("        ")
        remaining -= 4
      }
      while (remaining >= 2) {
        out.append("    ")
        remaining -= 2
      }
      if (remaining > 0) {
        out.append("  ")
      }
      ln = false
    }
  }
  
  actual override fun onTagError(tag: Tag<Nothing>, exception: Throwable): Unit = throw exception
  
  override fun onTagEvent(tag: Tag<Nothing>, event: String, value: (Nothing) -> Unit) {
    throw UnsupportedOperationException("you can't assign lambda event handler when building text")
  }
}
