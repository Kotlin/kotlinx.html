package html4k.dom

import html4k.*
import html4k.consumers.onFinalize
import html4k.consumers.onFinalizeMap
import org.w3c.dom.Node
import java.util.ArrayList
import kotlin.dom.first
import kotlin.dom.toList
import kotlin.js.dom.html.*

class JSDOMBuilder<R : HTMLElement>(val document : HTMLDocument) : TagConsumer<R> {
    private val path = arrayListOf<HTMLElement>()
    private var lastLeaved : HTMLElement? = null

    override fun onTagStart(tag: Tag) {
        val element = document.createElement(tag.tagName) as HTMLElement

        tag.attributes.forEach {
            element.setAttribute(it.getKey(), it.getValue())
        }

        if (path.isNotEmpty()) {
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
        if (path.isEmpty() || path.last().tagName.toLowerCase() != tag.tagName.toLowerCase()) {
            throw IllegalStateException("We haven't entered tag ${tag.tagName} but trying to leave")
        }

        lastLeaved = path.remove(path.lastIndex)
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
        path.last().appendChild(s.childNodes.toList().filter { it.nodeType == Node.TEXT_NODE }.first())

        // other solution would be
//        pathLast().innerHTML += entity.text
    }

    override fun onCDATA(content: CharSequence) {
        if (path.isEmpty()) {
            throw IllegalStateException("No current DOM node")
        }

        path.last().appendChild(document.createCDATASection(content.toString()))
    }

    override fun finalize(): R = lastLeaved?.asR() ?: throw IllegalStateException("We can't finalize as there was no tags")

    [suppress("UNCHECKED_CAST")]
    private fun HTMLElement.asR() = this as R

}


public fun HTMLDocument.createTree() : TagConsumer<HTMLElement> = JSDOMBuilder(this)
public fun Node.append(block : TagConsumer<HTMLElement>.() -> Unit) : List<HTMLElement> =
        ArrayList<HTMLElement>().let { result ->
            (ownerDocument as HTMLDocument).createTree().onFinalize { it, partial -> if (!partial) {result.add(it); appendChild(it) } }.block()

            result
        }
