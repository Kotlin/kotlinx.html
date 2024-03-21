package kotlinx.html.dom

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.Unsafe
import kotlinx.html.consumers.onFinalize
import kotlinx.html.consumers.onFinalizeMap
import kotlinx.html.org.w3c.dom.events.Event
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.xml.sax.InputSource
import java.io.StringReader
import java.io.StringWriter
import java.io.Writer
import javax.xml.parsers.DocumentBuilder
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

class HTMLDOMBuilder(val document : Document) : TagConsumer<Element> {
    private val path = arrayListOf<Element>()
    private var lastLeaved : Element? = null
    private val documentBuilder: DocumentBuilder by lazy { DocumentBuilderFactory.newInstance().newDocumentBuilder() }

    override fun onTagStart(tag: Tag) {
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
        if (path.isEmpty() || path.last().tagName.lowercase() != tag.tagName.lowercase()) {
            throw IllegalStateException("We haven't entered tag ${tag.tagName} but trying to leave")
        }

        val element = path.removeAt(path.lastIndex)
        element.setIdAttributeName()
        lastLeaved = element
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

    override fun finalize() = lastLeaved ?: throw IllegalStateException("No tags were emitted")

    override fun onTagContentUnsafe(block: Unsafe.() -> Unit) {
        UnsafeImpl.block()
    }

    private val UnsafeImpl = object : Unsafe {
        override operator fun String.unaryPlus() {
            val element = documentBuilder
                .parse(InputSource(StringReader("<unsafeRoot>$this</unsafeRoot>")))
                .documentElement

            val importNode = document.importNode(element, true)

            check(importNode.nodeName == "unsafeRoot") { "the document factory hasn't created an unsafeRoot node"}

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
}

fun Document.createHTMLTree() : TagConsumer<Element> = HTMLDOMBuilder(this)
val Document.create : TagConsumer<Element>
    get() = HTMLDOMBuilder(this)

@OptIn(ExperimentalContracts::class)
fun Node.append(block : TagConsumer<Element>.() -> Unit) : List<Element> {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return buildList {
        ownerDocumentExt.createHTMLTree().onFinalize { it, partial ->
            if (!partial) {
                appendChild(it)
                add(it)
            }
        }.block()
    }
}

@OptIn(ExperimentalContracts::class)
fun Node.prepend(block: TagConsumer<Element>.() -> Unit) : List<Element> {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return buildList {
        ownerDocumentExt.createHTMLTree().onFinalize { it, partial ->
            if (!partial) {
                insertBefore(it, firstChild)
                add(it)
            }
        }.block()
    }
}

val Node.append: TagConsumer<Element>
    get() = ownerDocumentExt.createHTMLTree().onFinalize { it, partial -> if (!partial) { appendChild(it) } }

val Node.prepend: TagConsumer<Element>
    get() = ownerDocumentExt.createHTMLTree().onFinalize { it, partial -> if (!partial) {
        insertBefore(it, firstChild)
    }}

private val Node.ownerDocumentExt: Document
    get() = when {
        this is Document -> this
        else -> ownerDocument ?: throw IllegalArgumentException("node has no ownerDocument")
    }

fun createHTMLDocument() : TagConsumer<Document> = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().let {
    document -> HTMLDOMBuilder(document).onFinalizeMap { it, partial -> if (!partial) {document.appendChild(it)}; document }
}

@OptIn(ExperimentalContracts::class)
inline fun document(block : Document.() -> Unit) : Document {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().apply(block)
}

fun Writer.write(document : Document, prettyPrint : Boolean = true) : Writer {
    write("<!DOCTYPE html>\n")
    write(document.documentElement, prettyPrint)
    return this
}

fun Writer.write(element: Element, prettyPrint : Boolean = true) : Writer {
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

fun Element.serialize(prettyPrint : Boolean = true) : String = StringWriter().write(this, prettyPrint).toString()
fun Document.serialize(prettyPrint : Boolean = true) : String = StringWriter().write(this, prettyPrint).toString()