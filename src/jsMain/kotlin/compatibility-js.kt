package kotlinx.html.js

import kotlinx.html.*
import org.w3c.dom.*

@Deprecated("Use legend instead", ReplaceWith("legend(classes, block)"))
fun TagConsumer<HTMLElement>.legEnd(classes : String? = null, block : LEGEND.() -> Unit = {}) : HTMLLegendElement = legend(classes, block)

@Deprecated("Use htmlObject instead", ReplaceWith("htmlObject(classes, block)", "kotlinx.html.js.htmlObject"))
fun TagConsumer<HTMLElement>.object_(classes : String? = null, block : OBJECT.() -> Unit = {}) : HTMLElement =
        htmlObject(classes, block)

@Deprecated("Use htmlVar instead", ReplaceWith("htmlVar(classes, block)", "kotlinx.html.js.htmlVar"))
fun TagConsumer<HTMLElement>.var_(classes : String? = null, block : VAR.() -> Unit = {}) : HTMLElement =
        htmlVar(classes, block)