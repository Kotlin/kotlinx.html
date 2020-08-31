package kotlinx.html.consumers

import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.currentTimeMillis

class TraceConsumer<R, E>(val downstream: TagConsumer<R, E>, val println: (String) -> Unit) :
    TagConsumer<R, E> by downstream {
    private val id = "ID-${currentTimeMillis() % 16384}"
    private val path = ArrayList<String>(1024)
    
    override fun onTagStart(tag: Tag<E>) {
        downstream.onTagStart(tag)
        path.add(tag.tagName)
        
        println("[$id]  open ${tag.tagName} path: ${path.joinToString(" > ")}")
    }
    
    override fun onTagEnd(tag: Tag<E>) {
        downstream.onTagEnd(tag)
        path.removeAt(path.lastIndex)
        
        println("[$id] close ${tag.tagName} path: ${path.joinToString(" > ")}")
    }
    
    override fun onTagAttributeChange(tag: Tag<E>, attribute: String, value: String?) {
        downstream.onTagAttributeChange(tag, attribute, value)
        
        println("[$id]     ${tag.tagName}.$attribute changed to $value")
    }
    
    override fun onTagError(tag: Tag<E>, exception: Throwable) {
        println("[$id] exception in ${tag.tagName}: ${exception.message}")
        
        downstream.onTagError(tag, exception)
    }
    
    override fun finalize(): R {
        val v = downstream.finalize()
        
        println("[$id] finalized: ${v.toString()}")
        
        return v
    }
}

fun <R, E> TagConsumer<R, E>.trace(println: (String) -> Unit): TagConsumer<R, E> = TraceConsumer(this, println)
//header fun <R> TagConsumer<R>.trace() : TagConsumer<R>
