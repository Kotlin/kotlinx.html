package kotlinx.html.js

import kotlinx.html.*
import org.w3c.dom.*

@Deprecated("Use legend instead", ReplaceWith("legend(classes, block)"))
fun TagConsumer<HTMLElement>.legEnd(classes : String? = null, block : LEGEND.() -> Unit = {}) : HTMLLegendElement = legend(classes, block)
