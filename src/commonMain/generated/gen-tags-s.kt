package kotlinx.html

import kotlinx.html.attributes.*

/*******************************************************************************
    DO NOT EDIT
    This file was generated by module generate
*******************************************************************************/

@Suppress("unused")
open class SAMP<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("samp", consumer, initialAttributes, null, true, false), HtmlBlockInlineTag<E> {

}
val <E> SAMP<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> SAMP<E>.asPhrasingContent : PhrasingContent<E>
    get()  = this


@Suppress("unused")
open class SCRIPT<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("script", consumer, initialAttributes, null, false, false), FlowMetaDataPhrasingContent<E> {
    var charset : String
        get()  = attributeStringString.get(this, "charset")
        set(newValue) {attributeStringString.set(this, "charset", newValue)}

    var type : String
        get()  = attributeStringString.get(this, "type")
        set(newValue) {attributeStringString.set(this, "type", newValue)}

    var src : String
        get()  = attributeStringString.get(this, "src")
        set(newValue) {attributeStringString.set(this, "src", newValue)}

    var defer : Boolean
        get()  = attributeBooleanTicker.get(this, "defer")
        set(newValue) {attributeBooleanTicker.set(this, "defer", newValue)}

    var async : Boolean
        get()  = attributeBooleanTicker.get(this, "async")
        set(newValue) {attributeBooleanTicker.set(this, "async", newValue)}

    var nonce : String
        get()  = attributeStringString.get(this, "nonce")
        set(newValue) {attributeStringString.set(this, "nonce", newValue)}

    var integrity : String
        get()  = attributeStringString.get(this, "integrity")
        set(newValue) {attributeStringString.set(this, "integrity", newValue)}

    @Deprecated("This tag most likely doesn't support text content or requires unsafe content (try unsafe {}")
    override operator fun Entities.unaryPlus() : Unit {
        @Suppress("DEPRECATION") entity(this)
    }

    @Deprecated("This tag most likely doesn't support text content or requires unsafe content (try unsafe {}")
    override operator fun String.unaryPlus() : Unit {
        @Suppress("DEPRECATION") text(this)
    }

    @Deprecated("This tag most likely doesn't support text content or requires unsafe content (try unsafe {}")
    override fun text(s : String) : Unit {
        super<HTMLTag>.text(s)
    }

    @Deprecated("This tag most likely doesn't support text content or requires unsafe content (try unsafe {}")
    override fun text(n : Number) : Unit {
        super<HTMLTag>.text(n)
    }

    @Deprecated("This tag most likely doesn't support text content or requires unsafe content (try unsafe {}")
    override fun entity(e : Entities) : Unit {
        super<HTMLTag>.entity(e)
    }

}
val <E> SCRIPT<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> SCRIPT<E>.asMetaDataContent : MetaDataContent<E>
    get()  = this

val <E> SCRIPT<E>.asPhrasingContent : PhrasingContent<E>
    get()  = this


@Suppress("unused")
open class SECTION<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("section", consumer, initialAttributes, null, false, false), CommonAttributeGroupFacadeFlowSectioningContent<E> {

}
val <E> SECTION<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> SECTION<E>.asSectioningContent : SectioningContent<E>
    get()  = this


@Suppress("unused")
open class SELECT<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("select", consumer, initialAttributes, null, true, false), CommonAttributeGroupFacadeFlowInteractivePhrasingContent<E> {
    var autoFocus : Boolean
        get()  = attributeBooleanTicker.get(this, "autofocus")
        set(newValue) {attributeBooleanTicker.set(this, "autofocus", newValue)}

    var disabled : Boolean
        get()  = attributeBooleanTicker.get(this, "disabled")
        set(newValue) {attributeBooleanTicker.set(this, "disabled", newValue)}

    var form : String
        get()  = attributeStringString.get(this, "form")
        set(newValue) {attributeStringString.set(this, "form", newValue)}

    var multiple : Boolean
        get()  = attributeBooleanTicker.get(this, "multiple")
        set(newValue) {attributeBooleanTicker.set(this, "multiple", newValue)}

    var name : String
        get()  = attributeStringString.get(this, "name")
        set(newValue) {attributeStringString.set(this, "name", newValue)}

    var size : String
        get()  = attributeStringString.get(this, "size")
        set(newValue) {attributeStringString.set(this, "size", newValue)}

    var required : Boolean
        get()  = attributeBooleanTicker.get(this, "required")
        set(newValue) {attributeBooleanTicker.set(this, "required", newValue)}


}
/**
 * Selectable choice
 */
@HtmlTagMarker
inline fun <E> SELECT<E>.option(classes : String? = null, crossinline block : OPTION<E>.() -> Unit = {}) : Unit = OPTION(attributesMapOf("class", classes), consumer).visit(block)
/**
 * Selectable choice
 */
@HtmlTagMarker
fun <E> SELECT<E>.option(classes : String? = null, content : String = "") : Unit = OPTION(attributesMapOf("class", classes), consumer).visit({+content})

/**
 * Option group
 */
@HtmlTagMarker
inline fun <E> SELECT<E>.optGroup(label : String? = null, classes : String? = null, crossinline block : OPTGROUP<E>.() -> Unit = {}) : Unit = OPTGROUP(attributesMapOf("label", label,"class", classes), consumer).visit(block)

val <E> SELECT<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> SELECT<E>.asInteractiveContent : InteractiveContent<E>
    get()  = this

val <E> SELECT<E>.asPhrasingContent : PhrasingContent<E>
    get()  = this


@Suppress("unused")
open class SMALL<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("small", consumer, initialAttributes, null, true, false), HtmlBlockInlineTag<E> {

}
val <E> SMALL<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> SMALL<E>.asPhrasingContent : PhrasingContent<E>
    get()  = this


@Suppress("unused")
open class SOURCE<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("source", consumer, initialAttributes, null, true, true), CommonAttributeGroupFacade<E> {
    var src : String
        get()  = attributeStringString.get(this, "src")
        set(newValue) {attributeStringString.set(this, "src", newValue)}

    var type : String
        get()  = attributeStringString.get(this, "type")
        set(newValue) {attributeStringString.set(this, "type", newValue)}

    var media : String
        get()  = attributeStringString.get(this, "media")
        set(newValue) {attributeStringString.set(this, "media", newValue)}


}

@Suppress("unused")
open class SPAN<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("span", consumer, initialAttributes, null, true, false), HtmlBlockInlineTag<E> {

}
val <E> SPAN<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> SPAN<E>.asPhrasingContent : PhrasingContent<E>
    get()  = this


@Suppress("unused")
open class STRONG<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("strong", consumer, initialAttributes, null, true, false), HtmlBlockInlineTag<E> {

}
val <E> STRONG<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> STRONG<E>.asPhrasingContent : PhrasingContent<E>
    get()  = this


@Suppress("unused")
open class STYLE<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("style", consumer, initialAttributes, null, false, false), CommonAttributeGroupFacadeFlowMetaDataContent<E> {
    var type : String
        get()  = attributeStringString.get(this, "type")
        set(newValue) {attributeStringString.set(this, "type", newValue)}

    var media : String
        get()  = attributeStringString.get(this, "media")
        set(newValue) {attributeStringString.set(this, "media", newValue)}

    var scoped : Boolean
        get()  = attributeBooleanTicker.get(this, "scoped")
        set(newValue) {attributeBooleanTicker.set(this, "scoped", newValue)}

    var nonce : String
        get()  = attributeStringString.get(this, "nonce")
        set(newValue) {attributeStringString.set(this, "nonce", newValue)}

    @Deprecated("This tag most likely doesn't support text content or requires unsafe content (try unsafe {}")
    override operator fun Entities.unaryPlus() : Unit {
        @Suppress("DEPRECATION") entity(this)
    }

    @Deprecated("This tag most likely doesn't support text content or requires unsafe content (try unsafe {}")
    override operator fun String.unaryPlus() : Unit {
        @Suppress("DEPRECATION") text(this)
    }

    @Deprecated("This tag most likely doesn't support text content or requires unsafe content (try unsafe {}")
    override fun text(s : String) : Unit {
        super<HTMLTag>.text(s)
    }

    @Deprecated("This tag most likely doesn't support text content or requires unsafe content (try unsafe {}")
    override fun text(n : Number) : Unit {
        super<HTMLTag>.text(n)
    }

    @Deprecated("This tag most likely doesn't support text content or requires unsafe content (try unsafe {}")
    override fun entity(e : Entities) : Unit {
        super<HTMLTag>.entity(e)
    }

}
val <E> STYLE<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> STYLE<E>.asMetaDataContent : MetaDataContent<E>
    get()  = this


@Suppress("unused")
open class SUB<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("sub", consumer, initialAttributes, null, true, false), HtmlBlockInlineTag<E> {

}
val <E> SUB<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> SUB<E>.asPhrasingContent : PhrasingContent<E>
    get()  = this


@Suppress("unused")
open class SUMMARY<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("summary", consumer, initialAttributes, null, true, false), CommonAttributeGroupFacadeFlowHeadingPhrasingContent<E> {

}

@Suppress("unused")
open class SUP<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("sup", consumer, initialAttributes, null, true, false), HtmlBlockInlineTag<E> {

}
val <E> SUP<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> SUP<E>.asPhrasingContent : PhrasingContent<E>
    get()  = this


@Suppress("unused")
open class SVG<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("svg", consumer, initialAttributes, "http://www.w3.org/2000/svg", false, false), HtmlBlockInlineTag<E> {

}
val <E> SVG<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> SVG<E>.asPhrasingContent : PhrasingContent<E>
    get()  = this


