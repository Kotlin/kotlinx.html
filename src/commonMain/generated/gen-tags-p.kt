package kotlinx.html

import kotlinx.html.attributes.*

/*******************************************************************************
    DO NOT EDIT
    This file was generated by module generate
*******************************************************************************/

@Suppress("unused")
open class P<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("p", consumer, initialAttributes, null, false, false), HtmlBlockInlineTag<E> {

}
val <E> P<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> P<E>.asPhrasingContent : PhrasingContent<E>
    get()  = this


@Suppress("unused")
open class PARAM<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("param", consumer, initialAttributes, null, true, true) {
    var name : String
        get()  = attributeStringString.get(this, "name")
        set(newValue) {attributeStringString.set(this, "name", newValue)}

    var value : String
        get()  = attributeStringString.get(this, "value")
        set(newValue) {attributeStringString.set(this, "value", newValue)}


}

@Suppress("unused")
open class PRE<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("pre", consumer, initialAttributes, null, false, false), HtmlBlockInlineTag<E> {

}
val <E> PRE<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> PRE<E>.asPhrasingContent : PhrasingContent<E>
    get()  = this


@Suppress("unused")
open class PROGRESS<E>(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*, E>) : HTMLTag<E>("progress", consumer, initialAttributes, null, true, false), HtmlBlockInlineTag<E> {
    var value : String
        get()  = attributeStringString.get(this, "value")
        set(newValue) {attributeStringString.set(this, "value", newValue)}

    var max : String
        get()  = attributeStringString.get(this, "max")
        set(newValue) {attributeStringString.set(this, "max", newValue)}


}
val <E> PROGRESS<E>.asFlowContent : FlowContent<E>
    get()  = this

val <E> PROGRESS<E>.asPhrasingContent : PhrasingContent<E>
    get()  = this


