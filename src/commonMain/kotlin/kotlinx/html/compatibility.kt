@file:Suppress("unused", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName", "FunctionName")

package kotlinx.html


@Deprecated("use legend instead", ReplaceWith("legend(classes, block)"))
fun <T, E, C : TagConsumer<T, E>> C.legEnd(classes: String? = null, block: LEGEND<E>.() -> Unit = {}): T =
    legend(classes, block)

@Deprecated("use legend instead", ReplaceWith("legend(classes, block)"))
fun <E> DETAILS<E>.legEnd(classes: String? = null, block: LEGEND<E>.() -> Unit = {}): Unit = legend(classes, block)

@Deprecated("use legend instead", ReplaceWith("legend(classes, block)"))
fun <E> FIELDSET<E>.legEnd(classes: String? = null, block: LEGEND<E>.() -> Unit = {}): Unit = legend(classes, block)

@Deprecated("use legend instead", ReplaceWith("legend(classes, block)"))
fun <E> FIGURE<E>.legEnd(classes: String? = null, block: LEGEND<E>.() -> Unit = {}): Unit = legend(classes, block)

@Deprecated("", ReplaceWith("Draggable.htmlTrue"))
inline val Draggable.true_: Draggable
    get() = Draggable.htmlTrue

@Deprecated("", ReplaceWith("Draggable.htmlFalse"))
inline val Draggable.false_: Draggable
    get() = Draggable.htmlFalse

@Deprecated("Use OBJECT instead", ReplaceWith("OBJECT", "kotlinx.html.OBJECT"))
typealias OBJECT_<E> = OBJECT<E>

@Deprecated("Use VAR type instead", ReplaceWith("VAR", "kotlinx.html.VAR"))
typealias VAR_<E> = VAR<E>

@Deprecated("", ReplaceWith("htmlObject(classes, block)", "kotlinx.html.htmlObject"))
fun <T, E, C : TagConsumer<T, E>> C.object_(classes: String? = null, block: OBJECT<E>.() -> Unit = {}): T =
    htmlObject(classes, block)

@Deprecated("", ReplaceWith("htmlVar(classes, block)", "kotlinx.html.htmlVar"))
fun <T, E, C : TagConsumer<T, E>> C.var_(classes: String? = null, block: VAR<E>.() -> Unit = {}): T =
    htmlVar(classes, block)

@Deprecated("Use htmlVar instead", ReplaceWith("htmlVar(classes, block)", "kotlinx.html.htmlVar"))
fun <E> FlowOrPhrasingContent<E>.var_(classes: String? = null, block: VAR<E>.() -> Unit) {
    htmlVar(classes, block)
}

@Deprecated("Use htmlObject instead", ReplaceWith("htmlObject(classes, block)", "kotlinx.html.htmlObject"))
fun <E> FlowOrInteractiveOrPhrasingContent<E>.object_(classes: String? = null, block: OBJECT<E>.() -> Unit = {}) =
    htmlObject(classes, block)

@Deprecated("use htmlFor instead", ReplaceWith("htmlFor"))
var <E>LABEL<E>.for_: String
    get() = htmlFor
    set(value) {
        htmlFor = value
    }

@Deprecated("use htmlFor instead", ReplaceWith("htmlFor"))
var <E>OUTPUT<E>.for_: String
    get() = htmlFor
    set(value) {
        htmlFor = value
    }

@Deprecated("Use onTouchCancel instead", ReplaceWith("onTouchCancel"))
var CommonAttributeGroupFacade<Nothing>.onTouchcancel: String
    get() = onTouchCancel
    set(newValue) {
        onTouchCancel = newValue
    }

@Deprecated("Use onTouchMove instead", ReplaceWith("onTouchMove"))
var CommonAttributeGroupFacade<Nothing>.onTouchmove: String
    get() = onTouchMove
    set(newValue) {
        onTouchMove = newValue
    }

