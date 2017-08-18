package kotlinx.html.dom

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.Unsafe
import kotlinx.html.consumers.onFinalize
import kotlinx.html.consumers.onFinalizeMap
import kotlinx.html.jsoup.owner
import kotlinx.html.jsoup.plusAssign
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.w3c.dom.events.Event

class HTMLJsoupBuilder(private val root: Document) : TagConsumer<Element> {
    private val path: MutableList<Element> = ArrayList()
    private var current: Element? = null
    
    override fun onTagStart(tag: Tag) {
        val element = root.appendElement(tag.tagName)
        
        tag.attributes.forEach { key, value ->
            if (value.isNullOrEmpty()) {
                element.attr(key)
            }
            else {
                element.attr(key, value)
            }
        }
        
        if (path.isNotEmpty()) {
            path.last() += element
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
        
        path.last() += content as String
    }
    
    override fun onTagContentEntity(entity: Entities) {
        if (path.isEmpty()) {
            throw IllegalStateException("No current DOM node")
        }
        
        path.last() += entity.text
    }
    
    override fun onTagContentUnsafe(block: Unsafe.() -> Unit) {
        val unsafe = object : Unsafe {
            override operator fun String.unaryPlus() {
                path.last() += Element(this)
            }
        }
        
        unsafe.block()
    }
    
    override fun finalize(): Element = root
}

fun Document.createHTMLTree(): TagConsumer<Element> = HTMLJsoupBuilder(this)
val Document.create: TagConsumer<Element>
    get() = HTMLJsoupBuilder(this)

fun Element.append(block: TagConsumer<Element>.() -> Unit): List<Element> {
    val list = ArrayList<Element>()
    
    ownerDocumentExt.createHTMLTree()
        .onFinalize { from, partial ->
            if (!partial) {
                this += from
                list += from
            }
        }
        .block()
    
    return list
    
}

val Element.append: TagConsumer<Element>
    get() = ownerDocumentExt.createHTMLTree().onFinalize { from, partial ->
        if (!partial) {
            this += from
        }
    }

private val Element.ownerDocumentExt: Document
    get() = when {
        this is Document -> this
        else             -> owner ?: throw IllegalArgumentException("Element has no owner document.")
    }

fun createJsoupDocument(baseUri: String = ""): TagConsumer<Document> {
    val document = Document(baseUri)
    val consumer = HTMLJsoupBuilder(document)
    return consumer.onFinalizeMap { from, partial ->
        if (!partial) {
            document.appendChild(from)
        }
        
        document
    }
}
