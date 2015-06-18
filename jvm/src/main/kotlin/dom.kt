package kotlinx.html.dom

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.consumers.onFinalize
import kotlinx.html.consumers.onFinalizeMap
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.events.Event
import java.io.StringWriter
import java.io.Writer
import java.util.ArrayList
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.*
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.dom.createDocument
import kotlin.dom.writeXmlString

class HTMLDOMBuilder(val document : Document) : TagConsumer<Element> {
    private val path = arrayListOf<Element>()
    private var lastLeaved : Element? = null

    override fun onTagStart(tag: Tag) {
        val element = document.createElement(tag.tagName)

        tag.attributes.forEach {
            element.setAttribute(it.getKey(), it.getValue())
        }

        if (path.isNotEmpty()) {
            path.last().appendChild(element)
        }

        path.add(element)
    }

    override fun onTagAttributeChange(tag: Tag, attribute: String, value: String?) {
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

    override fun onTagEvent(tag: Tag, event: String, value: (Event) -> Unit) {
        throw UnsupportedOperationException("You can't assign lambda event handler on JVM")
    }

    override fun onTagEnd(tag: Tag) {
        if (path.isEmpty() || path.last().getTagName().toLowerCase() != tag.tagName.toLowerCase()) {
            throw IllegalStateException("We haven't entered tag ${tag.tagName} but trying to leave")
        }

        val element = path.remove(path.lastIndex)
        element.setIdAttributeName()
        lastLeaved = element
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

        path.last().appendChild(document.createEntityReference(entity.name()))
    }

    override fun finalize() = lastLeaved ?: throw IllegalStateException("No tags were emitted")

    private fun Element.setIdAttributeName() {
        if (hasAttribute("id")) {
            setIdAttribute("id", true)
        }
    }
}

public fun Document.createHTMLTree() : TagConsumer<Element> = HTMLDOMBuilder(this)
public fun Node.append(block : TagConsumer<Element>.() -> Unit) : List<Element> = ArrayList<Element>().let { result ->
    getOwnerDocument().createHTMLTree().onFinalize { it, partial -> if (!partial) {appendChild(it); result.add(it)} }.block()

    result
}

public fun createHTMLDocument() : TagConsumer<Document> = createDocument().let { document -> HTMLDOMBuilder(document).onFinalizeMap { it, partial -> if (!partial) {document.appendChild(it)}; document } }

public inline fun document(block : Document.() -> Unit) : Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().let { document ->
    document.block()
    document
}

public fun Writer.write(document : Document, prettyPrint : Boolean = true) : Writer {
    write("<!DOCTYPE html>\n")
    write(document.getDocumentElement(), prettyPrint)
    return this
}

public fun Writer.write(element: Element, prettyPrint : Boolean = true) : Writer {
    val transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    transformer.setOutputProperty(OutputKeys.METHOD, "html");
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

    if (prettyPrint) {
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
    }

    transformer.transform(DOMSource(element), StreamResult(this))
    return this
}

public fun Element.serialize(prettyPrint : Boolean = true) : String = StringWriter().let { it.write(this, prettyPrint).toString() }
public fun Document.serialize(prettyPrint : Boolean = true) : String = StringWriter().let { it.write(this, prettyPrint).toString() }