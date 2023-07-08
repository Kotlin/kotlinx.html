package kotlinx.html

@HtmlTagMarker
inline fun <T, C : TagConsumer<T>> C.fragment(crossinline block: TagConsumer<T>.() -> Unit): T {
    this.block()
    return this.finalize()
}
