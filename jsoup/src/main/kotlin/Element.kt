package kotlinx.html.jsoup

import kotlinx.html.TagConsumer
import kotlinx.html.dom.JsoupBuilder
import org.jsoup.nodes.Attributes
import org.jsoup.nodes.DataNode
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.select.Elements

//region Read-Only Properties
val Element.outerHtml: String
    get() = outerHtml()

val Element.attributes: Attributes
    get() = attributes()

val Element.id: String?
    get() = id().takeUnless { it.isEmpty() }

val Element.data: String?
    get() = data().takeUnless { it.isEmpty() }

val Element.parent: Element?
    get() = parent()

val Element.parents: Elements
    get() = parents()

val Element.dataset: Map<String, String>
    get() = dataset()

val Element.dataNodes: List<DataNode>
    get() = dataNodes()

val Element.children: Elements
    get() = children()

val Element.ownText: String?
    get() = ownText().takeUnless { it.isEmpty() }

val Element.className: String?
    get() = className().takeUnless { it.isEmpty() }
//endregion

//region Mutable Properties
var Element.hyperlink: String?
    get() = attr("href").takeUnless { it.isEmpty() }
    set(value) {
        attr("href", value)
    }

var Element.html: String
    get() = html()
    set(value) {
        html(value)
    }

var Element.text: String
    get() = text()
    set(value) {
        text(value)
    }

var Element.tagName: String
    get() = tagName()
    set(value) {
        tagName(value)
    }

var Element.classNames: Set<String>
    get() = classNames()
    set(value) {
        classNames(value)
    }

var Element.value: String?
    get() = `val`().takeUnless { it.isEmpty() }
    set(value) {
        `val`(value)
    }
//endregion

//region Methods
operator fun Element.plusAssign(node: Node): Unit {
    appendChild(node)
}

operator fun Element.plusAssign(html: String): Unit {
    append(html)
}

operator fun Element.get(index: Int): Element
    = child(index)

fun Element.appendHTML(): TagConsumer<Element>
    = JsoupBuilder(this)

fun Element.appendHTML(action: TagConsumer<Element>.() -> Unit): TagConsumer<Element> {
    val consumer = JsoupBuilder(this)
    consumer.action()
    return consumer
}
//endregion
