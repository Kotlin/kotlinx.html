package kotlinx.html.dom

import kotlinx.html.Tag
import org.w3c.dom.events.Event

object EventAttribute {
    fun set(tag : Tag, name : String, value : (Event) -> Unit) {
        tag.consumer.onTagEvent(tag, name, value)
    }
}