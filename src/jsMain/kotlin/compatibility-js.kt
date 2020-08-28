package kotlinx.html.js

import kotlinx.html.*
import org.w3c.dom.*

@Deprecated("Use legend instead", ReplaceWith("legend(classes, block)"))
inline fun TagConsumer<HTMLElement>.legEnd(classes : String? = null, crossinline block : LEGEND.() -> Unit = {}) : HTMLLegendElement = legend(classes, block)

@Deprecated("Use htmlObject instead", ReplaceWith("htmlObject(classes, block)", "kotlinx.html.js.htmlObject"))
inline fun TagConsumer<HTMLElement>.object_(classes : String? = null, crossinline block : OBJECT.() -> Unit = {}) : HTMLElement =
        htmlObject(classes, block)

@Deprecated("Use htmlVar instead", ReplaceWith("htmlVar(classes, block)", "kotlinx.html.js.htmlVar"))
inline fun TagConsumer<HTMLElement>.var_(classes : String? = null, crossinline block : VAR.() -> Unit = {}) : HTMLElement =
        htmlVar(classes, block)
