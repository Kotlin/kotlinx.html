package kotlinx.html.dom

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.Unsafe
import kotlinx.html.consumers.DesktopTagConsumer
import kotlinx.html.consumers.onFinalize
import kotlinx.html.consumers.onFinalizeMap
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.StringReader
import java.io.StringWriter
import java.io.Writer
import java.util.*
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class HTMLDOMBuilder(val document: Document) : DesktopTagConsumer<Element> {
    private val path = mutableListOf<Element>()
    private var lastLeft: Element? = null
    private val documentBuilder: DocumentBuilder by lazy { DocumentBuilderFactory.newInstance().newDocumentBuilder() }
    
    override fun onTagStart(tag: Tag<Nothing>) {
        val element = when {
            tag.namespace != null -> document.createElementNS(tag.namespace!!, tag.tagName)
            else -> document.createElement(tag.tagName)
        }
        
        tag.attributesEntries.forEach {
            element.setAttribute(it.key, it.value)
        }
        
        if (path.isNotEmpty()) {
            path.last().appendChild(element)
        }
        
        path.add(element)
    }
    
    override fun onTagAttributeChange(tag: Tag<Nothing>, attribute: String, value: String?) {
        if (path.isEmpty()) {
            throw IllegalStateException("No current tag")
        }
        
        path.last().let { node ->
            if (value == null) {
                node.removeAttribute(attribute)
            } else {
                node.setAttribute(attribute, value)
            }
        }
    }
    
    override fun onTagEnd(tag: Tag<Nothing>) {
        if (path.isEmpty() || path.last().tagName.toLowerCase() != tag.tagName.toLowerCase()) {
            throw IllegalStateException("We haven't entered tag ${tag.tagName} but trying to leave")
        }
        
        val element = path.removeAt(path.lastIndex)
        element.setIdAttributeName()
        lastLeft = element
    }
    
    override fun onTagContent(content: CharSequence) {
        if (path.isEmpty()) {
            throw IllegalStateException("No current DOM node")
        }
        
        path.last().appendChild(document.createTextNode(content.toString()))
    }
    
    override fun onTagComment(content: CharSequence) {
        if (path.isEmpty()) {
            throw IllegalStateException("No current DOM node")
        }
        
        path.last().appendChild(document.createComment(content.toString()))
    }
    
    override fun onTagContentEntity(entity: Entities) {
        if (path.isEmpty()) {
            throw IllegalStateException("No current DOM node")
        }
        
        path.last().appendChild(document.createEntityReference(entity.name))
    }
    
    override fun finalize() = lastLeft ?: throw IllegalStateException("No tags were emitted")
    
    override fun onTagContentUnsafe(block: Unsafe.() -> Unit) {
        UnsafeImpl.block()
    }
    
    @Suppress("PrivatePropertyName")
    private val UnsafeImpl = object : Unsafe {
        override operator fun String.unaryPlus() {
            val element = documentBuilder
                .parse(InputSource(StringReader("<unsafeRoot>$this</unsafeRoot>")))
                .documentElement
            
            val importNode = document.importNode(element, true)
            
            check(importNode.nodeName == "unsafeRoot") { "the document factory hasn't created an unsafeRoot node" }
            
            val last = path.last()
            while (importNode.hasChildNodes()) {
                last.appendChild(importNode.removeChild(importNode.firstChild))
            }
        }
    }
    
    private fun Element.setIdAttributeName() {
        if (hasAttribute("id")) {
            setIdAttribute("id", true)
        }
    }
    
    override fun onTagError(tag: Tag<Nothing>, exception: Throwable): Nothing = throw exception
}

fun Document.createHTMLTree(): DesktopTagConsumer<Element> = HTMLDOMBuilder(this)
val Document.create: DesktopTagConsumer<Element>
    get() = HTMLDOMBuilder(this)

fun Node.append(block: DesktopTagConsumer<Element>.() -> Unit): List<Element> = ArrayList<Element>().let { result ->
    ownerDocumentExt.createHTMLTree().onFinalize { it, partial ->
        if (!partial) {
            appendChild(it); result.add(it)
        }
    }.block()
    
    result
}

fun Node.prepend(block: DesktopTagConsumer<Element>.() -> Unit): List<Element> = ArrayList<Element>().let { result ->
    ownerDocumentExt.createHTMLTree().onFinalize { it, partial ->
        if (!partial) {
            insertBefore(it, firstChild)
            result.add(it)
        }
    }.block()
    
    result
}

val Node.append: DesktopTagConsumer<Element>
    get() = ownerDocumentExt.createHTMLTree().onFinalize { it, partial ->
        if (!partial) {
            appendChild(it)
        }
    }

val Node.prepend: DesktopTagConsumer<Element>
    get() = ownerDocumentExt.createHTMLTree().onFinalize { it, partial ->
        if (!partial) {
            insertBefore(it, firstChild)
        }
    }

private val Node.ownerDocumentExt: Document
    get() = when (this) {
        is Document -> this
        else -> ownerDocument ?: throw IllegalArgumentException("node has no ownerDocument")
    }

fun createHTMLDocument(): DesktopTagConsumer<Document> =
    DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().let { document ->
        HTMLDOMBuilder(document).onFinalizeMap { it, partial ->
            if (!partial) {
                document.appendChild(it)
            }; document
        }
    }

inline fun document(block: Document.() -> Unit): Document =
    DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().let { document ->
        document.block()
        document
    }

fun Writer.write(document: Document, prettyPrint: Boolean = true): Writer {
    write("<!DOCTYPE html>\n")
    write(document.documentElement, prettyPrint)
    return this
}

fun Writer.write(element: Element, prettyPrint: Boolean = true): Writer {
    val transformer = TransformerFactory.newInstance().newTransformer()
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")
    transformer.setOutputProperty(OutputKeys.METHOD, "html")
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8")
    
    if (prettyPrint) {
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2")
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
    }
    
    transformer.transform(DOMSource(element), StreamResult(this))
    return this
}

fun Element.serialize(prettyPrint: Boolean = true): String = StringWriter().write(this, prettyPrint).toString()
fun Document.serialize(prettyPrint: Boolean = true): String = StringWriter().write(this, prettyPrint).toString()
