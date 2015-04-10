package html4k.consumers

import html4k.Tag
import html4k.TagConsumer
import java.util.ArrayList
import java.util.Date

class TraceConsumer<R>(val downstream : TagConsumer<R>) : TagConsumer<R> by downstream {
    private val id = "ID-${Date().getTime() mod 16384}"
    private val path = ArrayList<String>(1024)

    override fun onTagStart(tag: Tag) {
        downstream.onTagStart(tag)
        path.add(tag.tagName)

        println("[$id]  open ${tag.tagName} path: ${path.join(" > ")}")
    }

    override fun onTagEnd(tag: Tag) {
        downstream.onTagEnd(tag)
        path.remove(path.lastIndex)

        println("[$id] close ${tag.tagName} path: ${path.join(" > ")}")
    }

    override fun finalize(): R {
        val v = downstream.finalize()

        println("[$id] finalized: ${v.toString()}")

        return v
    }
}

public fun <R> TagConsumer<R>.trace() : TagConsumer<R> = TraceConsumer(this)