package kotlinx.html

import org.w3c.dom.events.*

interface TagConsumer<out R> {
    fun onTagStart(tag: Tag)
    fun onTagAttributeChange(tag: Tag, attribute: String, value: String?)
    fun onTagEvent(tag: Tag, event: String, value: (Event) -> Unit)
    fun onTagEnd(tag: Tag)
    fun onTagContent(content: CharSequence)
    fun onTagContentEntity(entity: Entities)
    fun onTagContentUnsafe(block: Unsafe.() -> Unit)
    fun finalize(): R
}

interface Tag {
    val tagName: String
    val consumer: TagConsumer<*>
    val namespace: String?

    val attributes: MutableMap<String, String>
}

interface Unsafe {
    operator fun String.plus()
    operator fun Entities.plus() = +text
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

fun HTMLTag.unsafe(block: Unsafe.() -> Unit): Unit = consumer.onTagContentUnsafe(block)

val emptyMap: Map<String, String> = emptyMap()

class DefaultUnsafe : Unsafe {
    private val sb = StringBuilder()

    override fun String.plus() {
        sb.append(this)
    }

    override fun toString(): String = sb.toString()
}