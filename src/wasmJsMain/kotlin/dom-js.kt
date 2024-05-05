package kotlinx.html.dom

import kotlinx.html.DefaultUnsafe
import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.Unsafe
import kotlinx.html.consumers.onFinalize
import kotlinx.html.org.w3c.dom.events.Event
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.Node
import org.w3c.dom.asList
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

private inline fun Element.setEvent(name: String, noinline callback: (Event) -> Unit) {
    val eventName = name.removePrefix("on")
    addEventListener(eventName, callback)
}

class JSDOMBuilder<out R : HTMLElement>(val document: Document) : TagConsumer<R> {
    private val path = arrayListOf<Element>()
    private var lastLeaved: Element? = null

    override fun onTagStart(tag: Tag) {
        val namespace = tag.namespace
        val element: Element =
            if (namespace != null) {
                document.createElementNS(namespace, tag.tagName)
            } else {
                document.createElement(tag.tagName)
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
        when {
            path.isEmpty() -> throw IllegalStateException("No current tag")
            path.last().tagName.lowercase() != tag.tagName.lowercase() -> throw IllegalStateException("Wrong current tag")
            else -> path.last().let { node ->
                if (value == null) {
                    node.removeAttribute(attribute)
                } else {
                    node.setAttribute(attribute, value)
                }
            }
        }
    }

    override fun onTagEvent(tag: Tag, event: String, value: (Event) -> Unit) {
        when {
            path.isEmpty() -> throw IllegalStateException("No current tag")
            path.last().tagName.lowercase() != tag.tagName.lowercase() -> throw IllegalStateException("Wrong current tag")
            else -> path.last().setEvent(event, value)
        }
    }

    override fun onTagEnd(tag: Tag) {
        if (path.isEmpty() || path.last().tagName.lowercase() != tag.tagName.lowercase()) {
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
        path.last().appendChild(s.childNodes.asList().filter { it.nodeType == Node.TEXT_NODE }.first())

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

    private inline fun Element.asR(): R {
        return jsCast(this)
    }

}

fun <T : JsAny> jsCast(any: JsAny): T = js("(any)")

fun Document.createTree(): TagConsumer<Element> = JSDOMBuilder(this)
val Document.create: TagConsumer<Element>
    get() = JSDOMBuilder(this)

@OptIn(ExperimentalContracts::class)
fun Node.append(block: TagConsumer<Element>.() -> Unit): List<Element> {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return buildList {
        ownerDocumentExt.createTree().onFinalize { it, partial ->
            if (!partial) {
                add(it)
                appendChild(it)
            }
        }.block()
    }
}

@OptIn(ExperimentalContracts::class)
fun Node.prepend(block: TagConsumer<Element>.() -> Unit): List<Element> {
    contract { callsInPlace(block, InvocationKind.EXACTLY_ONCE) }
    return buildList {
        ownerDocumentExt.createTree().onFinalize { it, partial ->
            if (!partial) {
                add(it)
                insertBefore(it, firstChild)
            }
        }.block()
    }
}

val Element.append: TagConsumer<Element>
    get() = ownerDocumentExt.createTree().onFinalize { element, partial ->
        if (!partial) {
            this@append.appendChild(element)
        }
    }

val Element.prepend: TagConsumer<Element>
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
