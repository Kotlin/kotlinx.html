package kotlinx.html.consumers

import kotlinx.html.Entities
import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import org.w3c.dom.events.Event
import java.util.Date

private class MeasureConsumer<R>(val downstream : TagConsumer<R>) : TagConsumer<Pair<R, Long>> {
    private val start = Date()

    override fun onTagStart(tag: Tag) {
        downstream.onTagStart(tag)
    }

    override fun onTagAttributeChange(tag: Tag, attribute: String, value: String?) {
        downstream.onTagAttributeChange(tag, attribute, value)
    }

    override fun onTagEvent(tag: Tag, event: String, value: (Event) -> Unit) {
        downstream.onTagEvent(tag, event, value)
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

    override fun finalize(): Pair<R, Long> = Pair(downstream.finalize(), Date().getTime().toLong() - start.getTime())
}

public fun <R> TagConsumer<R>.measureTime() : TagConsumer<Pair<R, Long>> = MeasureConsumer(this)