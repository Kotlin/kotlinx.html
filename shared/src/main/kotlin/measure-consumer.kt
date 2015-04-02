package html4k.consumers

import html4k.Entities
import html4k.Tag
import html4k.TagConsumer
import java.util.Date

private class MeasureConsumer<R>(val downstream : TagConsumer<R>) : TagConsumer<Pair<R, Long>> {
    private val start = Date()

    override fun onTagStart(tag: Tag) {
        downstream.onTagStart(tag)
    }

    override fun onTagAttributeChange(tag: Tag, attribute: String, value: String) {
        downstream.onTagAttributeChange(tag, attribute, value)
    }

    override fun onTagEnd(tag: Tag) {
        downstream.onTagEnd(tag)
    }

    override fun onTagContent(content: CharSequence) {
        downstream.onTagContent(content)
    }

    override fun onTagContentEntity(entity: Entities) {
        downstream.onTagContentEntity(entity)
    }

    override fun onCDATA(content: CharSequence) {
        downstream.onCDATA(content)
    }

    override fun finalize(): Pair<R, Long> = Pair(downstream.finalize(), (Date().getTime() - start.getTime()).toLong())
}

public fun <R> TagConsumer<R>.measureTime() : TagConsumer<Pair<R, Long>> = MeasureConsumer(this)