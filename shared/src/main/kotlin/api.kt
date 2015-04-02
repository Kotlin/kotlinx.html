package html4k

public fun Map<*, *>.isNotEmpty() : Boolean = !isEmpty()

trait TagConsumer<R> {
    fun onTagStart(tag : Tag)
    fun onTagAttributeChange(tag : Tag, attribute : String, value : String)
    fun onTagEnd(tag : Tag)
    fun onTagContent(content : CharSequence)
    fun onTagContentEntity(entity : Entities)
    fun onCDATA(content : CharSequence)
    fun finalize() : R
}

trait Tag {
    val name : String
    val observer : TagConsumer<*>

    val attributes : MutableMap<String, String>
}

inline fun <T : Tag> T.visit(block : T.() -> Unit) {
    observer.onTagStart(this)
    this.block()
    observer.onTagEnd(this)
}

fun Iterable<Pair<String, String?>>.toAttributesMap() : Map<String, String> = filter{it.second != null}.map { it.first to it.second!! }.toMap()