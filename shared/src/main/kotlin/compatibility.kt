package kotlinx.html


@Deprecated("use legend instead", ReplaceWith("legend(classes, block)"))
fun <T, C : TagConsumer<T>> C.legEnd(classes : String? = null, block : LEGEND.() -> Unit = {}) : T = legend(classes, block)

@Deprecated("use legend instead", ReplaceWith("legend(classes, block)"))
fun DETAILS.legEnd(classes : String? = null, block : LEGEND.() -> Unit = {}) : Unit = legend(classes, block)

@Deprecated("use legend instead", ReplaceWith("legend(classes, block)"))
fun FIELDSET.legEnd(classes : String? = null, block : LEGEND.() -> Unit = {}) : Unit = legend(classes, block)

@Deprecated("use legend instead", ReplaceWith("legend(classes, block)"))
fun FIGURE.legEnd(classes : String? = null, block : LEGEND.() -> Unit = {}) : Unit = legend(classes, block)

@Deprecated("", ReplaceWith("Draggable.htmlTrue"))
inline val Draggable.true_: Draggable get() = Draggable.htmlTrue

@Deprecated("", ReplaceWith("Draggable.htmlFalse"))
inline val Draggable.false_: Draggable get() = Draggable.htmlFalse

@Deprecated("Use OBJECT instead", ReplaceWith("OBJECT", "kotlinx.html.OBJECT"))
typealias OBJECT_ = OBJECT

@Deprecated("Use VAR type instead", ReplaceWith("VAR", "kotlinx.html.VAR"))
typealias VAR_ = VAR

@Deprecated("", ReplaceWith("htmlObject(classes, block)", "kotlinx.html.htmlObject"))
fun <T, C : TagConsumer<T>> C.object_(classes: String? = null, block: OBJECT.() -> Unit = {}) : T =
        htmlObject(classes, block)

@Deprecated("", ReplaceWith("htmlVar(classes, block)", "kotlinx.html.htmlVar"))
fun <T, C : TagConsumer<T>> C.var_(classes: String? = null, block: VAR.() -> Unit = {}): T =
        htmlVar(classes, block)

@Deprecated("Use htmlVar instead", ReplaceWith("htmlVar(classes, block)", "kotlinx.html.htmlVar"))
fun FlowOrPhrasingContent.var_(classes: String? = null, block: VAR.() -> Unit) {
    htmlVar(classes, block)
}

@Deprecated("Use htmlObject instead", ReplaceWith("htmlObject(classes, block)", "kotlinx.html.htmlObject"))
fun FlowOrInteractiveOrPhrasingContent.object_(classes : String? = null, block : OBJECT.() -> Unit = {}) =
        htmlObject(classes, block)

@Deprecated("use htmlFor instead", ReplaceWith("htmlFor"))
var LABEL.for_: String
    get() = htmlFor
    set(value) {
        htmlFor = value
    }

@Deprecated("use htmlFor instead", ReplaceWith("htmlFor"))
var OUTPUT.for_: String
    get() = htmlFor
    set(value) {
        htmlFor = value
    }

