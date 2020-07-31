package kotlinx.html

import kotlinx.html.attributes.*

/*******************************************************************************
    DO NOT EDIT
    This file was generated by module generate
*******************************************************************************/

@Suppress("unused")
open class CANVAS<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("canvas", consumer, initialAttributes, null, false, false), HtmlBlockInlineTag<E> {
    var width : String
        get()  = attributeStringString.get(this, "width")
        set(newValue) {attributeStringString.set(this, "width", newValue)}

    var height : String
        get()  = attributeStringString.get(this, "height")
        set(newValue) {attributeStringString.set(this, "height", newValue)}


}
val <E> CANVAS<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> CANVAS<E>.asPhrasingContent : PhrasingContent<E>
    get()  = this


@Suppress("unused")
open class CAPTION<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("caption", consumer, initialAttributes, null, false, false), HtmlBlockTag<E> {

}

@Suppress("unused")
open class CITE<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("cite", consumer, initialAttributes, null, true, false), HtmlBlockInlineTag<E> {

}
val <E> CITE<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> CITE<E>.asPhrasingContent : PhrasingContent<E>
    get()  = this


@Suppress("unused")
open class CODE<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("code", consumer, initialAttributes, null, true, false), HtmlBlockInlineTag<E> {

}
val <E> CODE<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> CODE<E>.asPhrasingContent : PhrasingContent<E>
    get()  = this


@Suppress("unused")
open class COL<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("col", consumer, initialAttributes, null, false, true), CommonAttributeGroupFacade<E> {
    var span : String
        get()  = attributeStringString.get(this, "span")
        set(newValue) {attributeStringString.set(this, "span", newValue)}


}

@Suppress("unused")
open class COLGROUP<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("colgroup", consumer, initialAttributes, null, false, false), CommonAttributeGroupFacade<E> {
    var span : String
        get()  = attributeStringString.get(this, "span")
        set(newValue) {attributeStringString.set(this, "span", newValue)}


}
/**
 * Table column
 */
@HtmlTagMarker
inline fun <E> COLGROUP<E>.col(classes : String? = null, crossinline block : COL<E>.() -> Unit = {}) : Unit = COL(attributesMapOf("class", classes), consumer).visit(block)


@Suppress("unused")
open class COMMAND<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("command", consumer, initialAttributes, null, true, true), CommonAttributeGroupFacadeFlowMetaDataPhrasingContent<E> {
    var type : CommandType
        get()  = attributeCommandTypeEnumCommandTypeValues.get(this, "type")
        set(newValue) {attributeCommandTypeEnumCommandTypeValues.set(this, "type", newValue)}

    var label : String
        get()  = attributeStringString.get(this, "label")
        set(newValue) {attributeStringString.set(this, "label", newValue)}

    var icon : String
        get()  = attributeStringString.get(this, "icon")
        set(newValue) {attributeStringString.set(this, "icon", newValue)}

    var disabled : Boolean
        get()  = attributeBooleanTicker.get(this, "disabled")
        set(newValue) {attributeBooleanTicker.set(this, "disabled", newValue)}

    var checked : Boolean
        get()  = attributeBooleanTicker.get(this, "checked")
        set(newValue) {attributeBooleanTicker.set(this, "checked", newValue)}

    var radioGroup : String
        get()  = attributeStringString.get(this, "radiogroup")
        set(newValue) {attributeStringString.set(this, "radiogroup", newValue)}


}
val <E> COMMAND<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> COMMAND<E>.asMetaDataContent : MetaDataContent<E>
    get()  = this

val <E> COMMAND<E>.asPhrasingContent : PhrasingContent<E>
    get()  = this


