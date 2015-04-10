package html4k.consumers

import html4k.Tag
import html4k.TagConsumer
import java.util.ArrayList
import java.util.Date

class TraceConsumer<R>(val downstream : TagConsumer<R>, val out : Appendable) : TagConsumer<R> by downstream {
    private val id = "ID-${Date().getTime() mod 16384}"
    private val path = ArrayList<String>(1024)

    override fun onTagStart(tag: Tag) {
        downstream.onTagStart(tag)
        path.add(tag.tagName)

        out.append("[$id]  open ${tag.tagName} path: ${path.join(" > ")}\n")
    }

    override fun onTagEnd(tag: Tag) {
        downstream.onTagEnd(tag)
        path.remove(path.lastIndex)

        out.append("[$id] close ${tag.tagName} path: ${path.join(" > ")}\n")
    }

    override fun finalize(): R {
        val v = downstream.finalize()

        out.append("[$id] finalized: ${v.toString()}\n")

        return v
    }
}

object PrintlnAppendable : Appendable {
    override fun append(csq: CharSequence?): Appendable {
        print(csq ?: "")
        return this
    }

    override fun append(csq: CharSequence?, start: Int, end: Int): Appendable {
        print(csq?.subSequence(start, end) ?: "")
        return this
    }

    override fun append(c: Char): Appendable {
        print(c)
        return this
    }
}

public fun <R> TagConsumer<R>.trace(out : Appendable = PrintlnAppendable) : TagConsumer<R> = TraceConsumer(this, out)