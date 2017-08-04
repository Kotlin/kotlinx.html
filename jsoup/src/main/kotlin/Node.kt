package kotlinx.html.jsoup

import org.jsoup.nodes.Attributes
import org.jsoup.nodes.Document
import org.jsoup.nodes.Node

//region Read-Only Properties
val Node.attributes: Attributes
    get() = attributes()

val Node.parent: Node?
    get() = parent()

val Node.nodeName: String
    get() = nodeName()

val Node.childNodes: List<Node>
    get() = childNodes()

val Node.owner: Document?
    get() = ownerDocument()

val Node.root: Node
    get() = root()

val Node.outerHtml: String
    get() = outerHtml()
//endregion

//region Mutable Properties
var Node.baseUri: String
    get() = baseUri()
    set(value) {
        setBaseUri(value)
    }
//endregion

//region Methods
operator fun Node.get(index: Int): Node = childNode(index)

operator fun Node.unaryMinus() = remove()
//endregion
