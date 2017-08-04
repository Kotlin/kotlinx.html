import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.Unsafe
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.w3c.dom.events.Event

class JsoupBuilder(val document: Document) : TagConsumer<Element> {
    val path: MutableList<Element> = ArrayList()
    var current: Element? = null
    
    override fun onTagStart(tag: Tag) {
        val element = document.appendElement(tag.tagName)
        
        tag.attributes.forEach {
            key, value ->
            if (value.isNullOrEmpty()) {
                element.attr(key)
            }
            else {
                element.attr(key, value)
            }
        }
        
        if (path.isNotEmpty()) {
            path.last().appendChild(element)
        }
        
        path += element
    }
    
    override fun onTagAttributeChange(tag: Tag, attribute: String, value: String?) {
        if (path.isEmpty()) {
            throw IllegalStateException("No current tag")
        }
        
        val element = path.last()
        
        if (value == null) {
            element.removeAttr(attribute)
        }
        else {
            element.attr(attribute, value)
        }
    }
    
    override fun onTagEvent(tag: Tag, event: String, value: (Event) -> Unit) {
        throw UnsupportedOperationException("You can't assign lambda event handler on JVM")
    }
    
    override fun onTagEnd(tag: Tag) {
        current = path.removeAt(path.lastIndex)
    }
    
    override fun onTagContent(content: CharSequence) {
        if (path.isEmpty()) {
            throw IllegalStateException("No current DOM node")
        }
        
        path.last().appendText(content as String)
    }
    
    override fun onTagContentEntity(entity: Entities) {
        if (path.isEmpty()) {
            throw IllegalStateException("No current DOM node")
        }
        
        path.last().appendText(entity.text)
    }
    
    override fun onTagContentUnsafe(block: Unsafe.() -> Unit) {
        val unsafe = object : Unsafe {
            override operator fun String.unaryPlus() {
                path.last().appendChild(Element(this))
            }
        }
        
        unsafe.block()
    }
    
    override fun finalize(): Element = document
}

fun Element.appendHTML(): TagConsumer<Element> {
    if (this is Document) {
        return JsoupBuilder(this)
    }
    else {
        val document = ownerDocument() ?: throw IllegalArgumentException("There is no owner document associated with this element.")
        return JsoupBuilder(document)
    }
}

fun Element.appendHTML(action: TagConsumer<Element>.() -> Unit): TagConsumer<Element> {
    val consumer = if (this is Document) {
        JsoupBuilder(this)
    }
    else {
        val document = ownerDocument() ?: throw IllegalArgumentException("There is no owner document associated with this element.")
        return JsoupBuilder(document)
    }
    
    consumer.action()
    return consumer
}
