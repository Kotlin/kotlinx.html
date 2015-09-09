package kotlinx.html

import org.w3c.dom.events.*

interface TagConsumer<out R> {
    fun onTagStart(tag: Tag)
    fun onTagAttributeChange(tag: Tag, attribute: String, value: String?)
    fun onTagEvent(tag: Tag, event: String, value: (Event) -> Unit)
    fun onTagEnd(tag: Tag)
    fun onTagContent(content: CharSequence)
    fun onTagContentEntity(entity: Entities)
    fun finalize(): R
}

interface Tag {
    val tagName: String
    val consumer: TagConsumer<*>

    val attributes: MutableMap<String, String>
}

interface AttributeEnum {
    val realValue: String
}

fun <T : Tag> T.visit(block: T.() -> Unit) {
    consumer.onTagStart(this)
    this.block()
    consumer.onTagEnd(this)
}

fun <T: Tag, R> T.visitAndFinalize(consumer: TagConsumer<R>, block: T.() -> Unit): R {
    require(this.consumer === consumer)
    visit(block)
    return consumer.finalize()
}

fun Iterable<Pair<String, String?>>.toAttributesMap(): Map<String, String> = filter { it.second != null }.map { it.first to it.second!! }.toMap()

val emptyMap: Map<String, String> = emptyMap()
