package html4k

import html4k.impl.*

private fun <T, C : TagConsumer<T>, TAG : Tag> C.build(attributes : Map<String, String>, builder : (Map<String, String>, TagConsumer<T>, TAG.() -> Unit) -> Unit, block : TAG.() -> Unit) : C {
    builder(attributes, this, block)
    return this
}

// TODO to be generated

public fun <T, C : TagConsumer<T>> C.div(block : DIV.() -> Unit) : T = build(emptyMap(), ::buildDIV, block).finalize()
public fun <T, C : TagConsumer<T>> C.a(href : String? = null, target : String? = null, block : A.() -> Unit) : T = build(listOf("href" to href, "target" to target).toAttributesMap(), ::buildA, block).finalize()
public fun <T, C : TagConsumer<T>> C.html(block : HTML.() -> Unit) : T = build(emptyMap(), ::buildHTML, block).finalize()