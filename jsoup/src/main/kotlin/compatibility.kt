package kotlinx.html.jsoup

import org.jsoup.nodes.Attribute
import org.jsoup.nodes.Attributes
import org.jsoup.nodes.DataNode
import org.jsoup.nodes.Document
import org.jsoup.nodes.Document.OutputSettings
import org.jsoup.nodes.Document.OutputSettings.Syntax
import org.jsoup.nodes.Document.QuirksMode
import org.jsoup.nodes.Element
import org.jsoup.nodes.Entities.EscapeMode
import org.jsoup.nodes.Node
import org.jsoup.select.Elements
import java.nio.charset.Charset

//region Document Immutable Properties
val Document.head: Element?
    get() = head()

val Document.body: Element?
    get() = body()

val Document.location: String
    get() = location()
//endregion

//region Element Immutable Properties
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

//region Node Immutable Properties
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

//region Attribute Immutable Properties
val Attribute.html: String
    get() = html()
//endregion

//region Elements Immutable Properties
val Elements.outerHtml: String
    get() = outerHtml()

val Elements.parents: Elements
    get() = parents()

val Elements.text: String
    get() = text()
//endregion

//region Attributes Immutable Properties
val Attributes.size: Int
    get() = size()
//endregion


//region Document Mutable Properties
var Document.title: String?
    get() = title().takeUnless { it.isEmpty() }
    set(value) {
        if (head == null) {
            appendElement("head")
        }
        title(value)
    }

var Document.charset: Charset
    get() = charset()
    set(value) {
        charset(value)
    }

var Document.updateMetaCharsetElement: Boolean
    get() = updateMetaCharsetElement()
    set(value) {
        updateMetaCharsetElement(value)
    }

var Document.quirksMode: QuirksMode
    get() = quirksMode()
    set(value) {
        quirksMode(value)
    }
//endregion

//region Element Mutable Properties
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

//region Node Mutable Properties
var Node.baseUri: String
    get() = baseUri()
    set(value) {
        setBaseUri(value)
    }
//endregion

//region Elements Mutable Properties
var Elements.html: String
    get() = html()
    set(value) {
        html(value)
    }

var Elements.value: String?
    get() = `val`().takeUnless { it.isEmpty() }
    set(value) {
        if (value == null) `val`("") else `val`(value)
    }
//endregion

//region OutputSettings Mutable Properties
var OutputSettings.escapeMode: EscapeMode
    get() = escapeMode()
    set(value) {
        escapeMode(value)
    }

var OutputSettings.charset: Charset
    get() = charset()
    set(value) {
        charset(value)
    }

var OutputSettings.syntax: Syntax
    get() = syntax()
    set(value) {
        syntax(value)
    }

var OutputSettings.prettyPrint: Boolean
    get() = prettyPrint()
    set(value) {
        prettyPrint(value)
    }

var OutputSettings.outline: Boolean
    get() = outline()
    set(value) {
        outline(value)
    }

var OutputSettings.indentAmount: Int
    get() = indentAmount()
    set(value) {
        indentAmount(value)
    }
//endregion


//region Element Operator Methods
operator fun Element.plusAssign(node: Node) {
    appendChild(node)
}

operator fun Element.plusAssign(html: String) {
    append(html)
}

operator fun Element.get(index: Int): Element
    = child(index)
//endregion

//region Node Operator Methods
operator fun Node.get(index: Int): Node = childNode(index)

operator fun Node.unaryMinus() = remove()
//endregion

//region Attributes Operator Methods
operator fun Attributes.set(key: String, value: String)
    = put(key, value)

operator fun Attributes.set(key: String, value: Boolean)
    = put(key, value)

operator fun Attributes.contains(key: String): Boolean
    = hasKey(key)

operator fun Attributes.contains(attribute: Pair<String, String>): Boolean
    = this.hasKey(attribute.first) && this[attribute.first] == attribute.second
//endregion


fun Document.toByteArray(): ByteArray = toString().toByteArray(charset)

fun Attributes.toMap(): Map<String, String>
    = associateBy({ it.key }, { it.value })

fun Pair<String, String>.toAttribute(): Attribute
    = Attribute(first, second)

fun outputSettings(outline: Boolean = false,
                   prettyPrint: Boolean = true,
                   indentAmount: Int = 1,
                   charset: Charset = Charsets.UTF_8,
                   escapeMode: EscapeMode = EscapeMode.base,
                   syntax: Syntax = Syntax.html)
    : OutputSettings {
    return OutputSettings()
        .outline(outline)
        .prettyPrint(prettyPrint)
        .indentAmount(indentAmount)
        .charset(charset)
        .escapeMode(escapeMode)
        .syntax(syntax)
}
