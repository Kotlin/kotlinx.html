package html4k.consumers

import html4k.Entities
import html4k.Tag
import html4k.TagConsumer

class FinalizeConsumer<F, T>(val downstream : TagConsumer<F>, val block : (F) -> T) : TagConsumer<T> {

    override fun onTagStart(tag: Tag) = downstream.onTagStart(tag)
    override fun onTagAttributeChange(tag: Tag, attribute: String, value: String) = downstream.onTagAttributeChange(tag, attribute, value)
    override fun onTagEnd(tag: Tag) = downstream.onTagEnd(tag)
    override fun onTagContent(content: CharSequence) = downstream.onTagContent(content)
    override fun onTagContentEntity(entity: Entities) = downstream.onTagContentEntity(entity)
    override fun onCDATA(content: CharSequence) = downstream.onCDATA(content)

    override fun finalize() = block(downstream.finalize())
}

public fun <T> TagConsumer<T>.onFinalize(block : (T) -> Unit) : TagConsumer<T> = FinalizeConsumer(this) { block(it); it }
public fun <F, T> TagConsumer<F>.onFinalizeMap(block : (F) -> T) : TagConsumer<T> = FinalizeConsumer(this, block)