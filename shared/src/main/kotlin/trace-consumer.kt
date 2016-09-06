package kotlinx.html.consumers

import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import java.util.ArrayList
import java.util.Date

class TraceConsumer<R>(val downstream : TagConsumer<R>) : TagConsumer<R> by downstream {
    private val id = "ID-${Date().getTime() % 16384}"
    private val path = ArrayList<String>(1024)

    override fun onTagStart(tag: Tag) {
        downstream.onTagStart(tag)
        path.add(tag.tagName)

        println("[$id]  open ${tag.tagName} path: ${path.joinToString(" > ")}")
    }

    override fun onTagEnd(tag: Tag) {
        downstream.onTagEnd(tag)
        path.removeAt(path.lastIndex)

        println("[$id] close ${tag.tagName} path: ${path.joinToString(" > ")}")
    }

    override fun onTagAttributeChange(tag: Tag, attribute: String, value: String?) {
        downstream.onTagAttributeChange(tag, attribute, value)

        println("[$id]     ${tag.tagName}.$attribute changed to $value")
    }

    override fun onError(tag: Tag, exception: Exception) {
        println("[$id] exception in ${tag.tagName}: ${exception.message}")

        downstream.onError(tag, exception)
    }

    override fun finalize(): R {
        val v = downstream.finalize()

        println("[$id] finalized: ${v.toString()}")

        return v
    }
}

public fun <R> TagConsumer<R>.trace() : TagConsumer<R> = TraceConsumer(this)