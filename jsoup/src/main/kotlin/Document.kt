package kotlinx.html.jsoup

import org.jsoup.nodes.Document
import org.jsoup.nodes.Document.QuirksMode
import org.jsoup.nodes.Element
import java.nio.charset.Charset

//region Read-Only Properties
val Document.head: Element?
    get() = head()

val Document.body: Element?
    get() = body()

val Document.location: String
    get() = location()
//endregion

//region Mutable Properties
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

//region Methods
fun Document.toByteArray(): ByteArray = toString().toByteArray(charset())
//endregion
