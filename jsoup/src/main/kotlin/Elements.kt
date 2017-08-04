package kotlinx.html.jsoup

import org.jsoup.select.Elements

//region Read-Only Properties
val Elements.outerHtml: String
    get() = outerHtml()

val Elements.parents: Elements
    get() = parents()

val Elements.text: String
    get() = text()
//endregion

//region Mutable Properties
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
