package kotlinx.html.consumers

import kotlinx.html.*

class TraceConsumer<R>(val downstream: TagConsumer<R>, val println: (String) -> Unit) : TagConsumer<R> by downstream {
    private val id = "ID-${currentTimeMillis() % 16384}"
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

    override fun onTagError(tag: Tag, exception: Throwable) {
        println("[$id] exception in ${tag.tagName}: ${exception.message}")

        downstream.onTagError(tag, exception)
    }

    override fun finalize(): R {
        val v = downstream.finalize()

        println("[$id] finalized: ${v.toString()}")

        return v
    }
}

fun <R> TagConsumer<R>.trace(println: (String) -> Unit): TagConsumer<R> = TraceConsumer(this, println)
//header fun <R> TagConsumer<R>.trace() : TagConsumer<R>
