package kotlinx.html.dom

import kotlinx.html.*
import kotlinx.html.consumers.onFinalize
import org.w3c.dom.Document
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import org.w3c.dom.asList

@Suppress("NOTHING_TO_INLINE")
private inline fun HTMLElement.setEvent(name: String, noinline callback: (Event) -> Unit): Unit {
  asDynamic()[name] = callback
}

class JSDOMBuilder<out R : HTMLElement>(val document: Document) : TagConsumer<R, Event> {
  private val path = arrayListOf<HTMLElement>()
  private var lastLeaved: HTMLElement? = null
  
  override fun onTagStart(tag: Tag<Event>) {
    val element: HTMLElement = when {
      tag.namespace != null -> document.createElementNS(tag.namespace!!, tag.tagName).asDynamic()
      else -> document.createElement(tag.tagName) as HTMLElement
    }
    
    tag.attributesEntries.forEach {
      element.setAttribute(it.key, it.value)
    }
    
    if (path.isNotEmpty()) {
      path.last().appendChild(element)
    }
  
    path.add(element)
  }
  
  override fun onTagAttributeChange(tag: Tag<Event>, attribute: String, value: String?) {
    when {
      path.isEmpty() -> throw IllegalStateException("No current tag")
      path.last().tagName.toLowerCase() != tag.tagName.toLowerCase() -> throw IllegalStateException("Wrong current tag")
      else -> path.last().let { node ->
        if (value == null) {
          node.removeAttribute(attribute)
        } else {
          node.setAttribute(attribute, value)
        }
      }
    }
  }
  
  override fun onTagEvent(tag: Tag<Event>, event: String, value: (Event) -> Unit) {
    when {
      path.isEmpty() -> throw IllegalStateException("No current tag")
      path.last().tagName.toLowerCase() != tag.tagName.toLowerCase() -> throw IllegalStateException("Wrong current tag")
      else -> path.last().setEvent(event, value)
    }
  }
  
  override fun onTagEnd(tag: Tag<Event>) {
    if (path.isEmpty() || path.last().tagName.toLowerCase() != tag.tagName.toLowerCase()) {
      throw IllegalStateException("We haven't entered tag ${tag.tagName} but trying to leave")
    }
    
    lastLeaved = path.removeAt(path.lastIndex)
  }
  
  override fun onTagContent(content: CharSequence) {
    if (path.isEmpty()) {
      throw IllegalStateException("No current DOM node")
    }
    
    path.last().appendChild(document.createTextNode(content.toString()))
  }
  
  override fun onTagContentEntity(entity: Entities) {
    if (path.isEmpty()) {
      throw IllegalStateException("No current DOM node")
    }
    
    // stupid hack as browsers doesn't support createEntityReference
    val s = document.createElement("span") as HTMLElement
    s.innerHTML = entity.text
    path.last().appendChild(s.childNodes.asList().first { it.nodeType == Node.TEXT_NODE })
    
    // other solution would be
//        pathLast().innerHTML += entity.text
  }
  
  override fun onTagContentUnsafe(block: Unsafe.() -> Unit) {
    with(DefaultUnsafe()) {
      block()
  
      path.last().innerHTML += toString()
    }
  }
  
  
  override fun onTagComment(content: CharSequence) {
    if (path.isEmpty()) {
      throw IllegalStateException("No current DOM node")
    }
    
    path.last().appendChild(document.createComment(content.toString()))
  }
  
  override fun finalize(): R =
    lastLeaved?.asR() ?: throw IllegalStateException("We can't finalize as there was no tags")
  
  @Suppress("UNCHECKED_CAST")
  private fun HTMLElement.asR(): R = this.asDynamic()
  override fun onTagError(tag: Tag<Event>, exception: Throwable): Nothing = throw exception
}


fun Document.createTree(): TagConsumer<HTMLElement, Event> = JSDOMBuilder(this)
val Document.create: TagConsumer<HTMLElement, Event>
  get() = JSDOMBuilder(this)

fun Node.append(block: TagConsumer<HTMLElement, Event>.() -> Unit): List<HTMLElement> =
  ArrayList<HTMLElement>().let { result ->
    ownerDocumentExt.createTree().onFinalize { it, partial ->
      if (!partial) {
        result.add(it); appendChild(it)
      }
    }.block()
    
    result
  }

fun Node.prepend(block: TagConsumer<HTMLElement, Event>.() -> Unit): List<HTMLElement> =
  ArrayList<HTMLElement>().let { result ->
    ownerDocumentExt.createTree().onFinalize { it, partial ->
      if (!partial) {
        result.add(it)
        insertBefore(it, firstChild)
      }
    }.block()
    
    result
  }

val HTMLElement.append: TagConsumer<HTMLElement, Event>
  get() = ownerDocumentExt.createTree().onFinalize { element, partial ->
    if (!partial) {
      this@append.appendChild(element)
    }
  }

val HTMLElement.prepend: TagConsumer<HTMLElement, Event>
  get() = ownerDocumentExt.createTree().onFinalize { element, partial ->
    if (!partial) {
      this@prepend.insertBefore(element, this@prepend.firstChild)
    }
  }

private val Node.ownerDocumentExt: Document
  get() = when {
    this is Document -> this
    else -> ownerDocument ?: throw IllegalStateException("Node has no ownerDocument")
  }
