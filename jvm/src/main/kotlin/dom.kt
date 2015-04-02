package html4k.dom

import html4k.Entities
import html4k.Tag
import html4k.TagConsumer
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.StringWriter
import java.io.Writer
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.*
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.dom.writeXmlString

class HTMLDOMBuilder(val document : Document) : TagConsumer<Element> {
    private val path = arrayListOf<Element>()

    override fun onTagStart(tag: Tag) {
        val element = document.createElement(tag.name)

        tag.attributes.forEach {
            element.setAttribute(it.getKey(), it.getValue())
        }

        if (path.isEmpty()) {
            path.add(element)
        } else {
            path.last().appendChild(element)
        }

        path.add(element)
    }

    override fun onTagAttributeChange(tag: Tag, attribute: String, value: String) {
        if (path.isEmpty()) {
            throw IllegalStateException("No current tag")
        }

        path.last().setAttribute(attribute, value)
    }

    override fun onTagEnd(tag: Tag) {
        if (path.isEmpty() || path.last().getTagName().toLowerCase() != tag.name.toLowerCase()) {
            throw IllegalStateException("We haven't entered tag ${tag.name} but trying to leave")
        }

        path.remove(path.lastIndex)
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

    override fun onCDATA(content: CharSequence) {
        if (path.isEmpty()) {
            throw IllegalStateException("No current DOM node")
        }

        path.last().appendChild(document.createCDATASection(content.toString()))
    }

    override fun finalize() = path.last()
}

fun Document.buildHTML() : TagConsumer<Element> = HTMLDOMBuilder(this)
inline fun Node.buildAndAppendChild(block : TagConsumer<Element>.() -> Element) : Element =
    getOwnerDocument().buildHTML().block().let { element ->
        appendChild(element)
        element
    }

inline fun document(block : Document.() -> Unit) : Document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().let { document ->
    document.block()
    document
}

fun Writer.write(document : Document, prettyPrint : Boolean = true) : Writer {
    write("<!DOCTYPE html>\n")
    write(document.getDocumentElement(), prettyPrint)
    return this
}

fun Writer.write(element: Element, prettyPrint : Boolean = true) : Writer {
    val transformer = TransformerFactory.newInstance().newTransformer();
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    transformer.setOutputProperty(OutputKeys.METHOD, "html");
    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

    if (prettyPrint) {
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        transformer.transform(DOMSource(element), StreamResult(this))
    }

    return this
}

fun Element.serialize(prettyPrint : Boolean = true) : String = StringWriter().let { it.write(this, prettyPrint).toString() }
fun Document.serialize(prettyPrint : Boolean = true) : String = StringWriter().let { it.write(this, prettyPrint).toString() }