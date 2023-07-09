package kotlinx.html

@HtmlTagMarker
inline fun <T, C : TagConsumer<T>> C.fragment(crossinline block: DIV.() -> Unit): T {
    DIV(emptyMap, this).block()
    return this.finalize()
}
