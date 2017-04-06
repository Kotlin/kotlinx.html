package kotlinx.html


@Deprecated("use legend instead", ReplaceWith("legend(classes, block)"))
fun <T, C : TagConsumer<T>> C.legEnd(classes : String? = null, block : LEGEND.() -> Unit = {}) : T = legend(classes, block)

@Deprecated("use legend instead", ReplaceWith("legend(classes, block)"))
fun DETAILS.legEnd(classes : String? = null, block : LEGEND.() -> Unit = {}) : Unit = legend(classes, block)

@Deprecated("use legend instead", ReplaceWith("legend(classes, block)"))
fun FIELDSET.legEnd(classes : String? = null, block : LEGEND.() -> Unit = {}) : Unit = legend(classes, block)

@Deprecated("use legend instead", ReplaceWith("legend(classes, block)"))
fun FIGURE.legEnd(classes : String? = null, block : LEGEND.() -> Unit = {}) : Unit = legend(classes, block)

