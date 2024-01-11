package kotlinx.html.js

import kotlinx.html.LEGEND
import kotlinx.html.OBJECT
import kotlinx.html.TagConsumer
import kotlinx.html.VAR
import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLLegendElement

@Deprecated("Use legend instead", ReplaceWith("legend(classes, block)"))
inline fun TagConsumer<HTMLElement>.legEnd(
    classes: String? = null,
    crossinline block: LEGEND.() -> Unit = {},
): HTMLLegendElement = legend(classes, block)

@Deprecated("Use htmlObject instead", ReplaceWith("htmlObject(classes, block)", "kotlinx.html.js.htmlObject"))
inline fun TagConsumer<HTMLElement>.object_(
    classes: String? = null,
    crossinline block: OBJECT.() -> Unit = {},
): Element =
    htmlObject(classes, block)

@Deprecated("Use htmlVar instead", ReplaceWith("htmlVar(classes, block)", "kotlinx.html.js.htmlVar"))
inline fun TagConsumer<HTMLElement>.var_(classes: String? = null, crossinline block: VAR.() -> Unit = {}): Element =
    htmlVar(classes, block)
