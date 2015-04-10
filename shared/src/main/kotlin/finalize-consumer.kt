package html4k.consumers

import html4k.Entities
import html4k.Tag
import html4k.TagConsumer

class FinalizeConsumer<F, T>(val downstream : TagConsumer<F>, val block : (F, Boolean) -> T) : TagConsumer<T> {
    private var level = 0

    override fun onTagStart(tag: Tag) {
        downstream.onTagStart(tag)
        level ++
    }
    override fun onTagEnd(tag: Tag) {
        downstream.onTagEnd(tag)
        level --
    }

    override fun onTagAttributeChange(tag: Tag, attribute: String, value: String) = downstream.onTagAttributeChange(tag, attribute, value)
    override fun onTagContent(content: CharSequence) = downstream.onTagContent(content)
    override fun onTagContentEntity(entity: Entities) = downstream.onTagContentEntity(entity)
    override fun onCDATA(content: CharSequence) = downstream.onCDATA(content)

    override fun finalize() = block(downstream.finalize(), level > 0)
}

public fun <T> TagConsumer<T>.onFinalize(block : (from : T, partial : Boolean) -> Unit) : TagConsumer<T> = FinalizeConsumer(this) { to, partial -> block(to, partial); to }
public fun <F, T> TagConsumer<F>.onFinalizeMap(block : (from : F, partial : Boolean) -> T) : TagConsumer<T> = FinalizeConsumer(this, block)