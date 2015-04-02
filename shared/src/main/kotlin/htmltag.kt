package html4k

import html4k.impl.*
import html4k.impl.DelegatingMap

// TODO to be generated

open class HTMLTag(override val name: String, val consumer: TagConsumer<*>, initialAttributes : Map<String, String> = emptyMap()) : Tag {
    override val attributes: DelegatingMap = DelegatingMap(initialAttributes, this) {observer}
    override val observer : TagConsumer<*>
        get() = consumer

    deprecated("you shouldn't use this tag here")
    open fun div(classes: List<String> = listOf(), block: DIV.() -> Unit) = buildDIV(listOf("class" to classes.join(" ")).filter{it.second.isNotEmpty()}.toAttributesMap(), observer, block)

    deprecated("you shouldn't use this tag here")
    open fun a(href: String? = null, block: A.() -> Unit) = buildA(listOf("href" to href).toAttributesMap(), observer, block)

    deprecated("you shouldn't use this tag here")
    open fun body(block : BODY.() -> Unit) = buildBODY(emptyMap(), observer, block)

    deprecated("you shouldn't use this tag here")
    open fun head(block : HEAD.() -> Unit) = buildHEAD(emptyMap(), observer, block)

    fun Entities.plus() {
        observer.onTagContentEntity(this)
    }

    fun String.plus() {
        observer.onTagContent(this)
    }

    fun CDATA(s : CharSequence) {
        observer.onCDATA(s)
    }
}