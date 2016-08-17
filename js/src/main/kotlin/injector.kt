package kotlinx.html.injector

import kotlinx.html.*
import kotlinx.html.dom.*
import org.w3c.dom.HTMLElement
import kotlin.Pair
import kotlin.dom.*
import kotlin.reflect.*

fun <F : Any, T : Any> F.injectTo(bean : T, field : KMutableProperty1<T, in F>) {
    field.set(bean, this)
}

private fun <F : Any, T : Any> F.injectToUnsafe(bean : T, field : KMutableProperty1<T, out F>) {
    injectTo(bean, field.asDynamic())
}

interface InjectCapture
class InjectByClassName(val className : String) : InjectCapture
class InjectByTagName(val tagName : String) : InjectCapture
object InjectRoot : InjectCapture
interface CustomCapture : InjectCapture {
    fun apply(element : HTMLElement) : Boolean
}

class InjectorConsumer<out T: Any>(val downstream : TagConsumer<HTMLElement>, val bean : T, rules : List<Pair<InjectCapture, KMutableProperty1<T, out HTMLElement>>>) : TagConsumer<HTMLElement> by downstream {

    private val classesMap = rules
            .filter { it.first is InjectByClassName }
            .map { it.first as InjectByClassName to it.second }
            .groupBy ({ it.first.className }, { it.second })

    private val tagNamesMap = rules
            .filter { it.first is InjectByTagName }
            .map { it.first as InjectByTagName to it.second }
            .groupBy({ it.first.tagName.toLowerCase() }, { it.second })

    private val rootCaptures = rules.filter { it.first == InjectRoot }.map { it.second }
    private val customCaptures = rules.filter {it.first is CustomCapture}.map {it.first as CustomCapture to it.second}

    override fun onTagEnd(tag: Tag) {
        downstream.onTagEnd(tag)

        val node = downstream.finalize()

        if (classesMap.isNotEmpty()) {
            node.classList.asList().flatMap { classesMap[it] ?: emptyList() }.forEach { field ->
                node.injectToUnsafe(bean, field)
            }
        }

        if (tagNamesMap.isNotEmpty()) {
            tagNamesMap[node.tagName.toLowerCase()]?.forEach { field ->
                node.injectToUnsafe(bean, field)
            }
        }

        customCaptures.filter { it.first.apply(node) }.map {it.second}.forEach { field ->
            node.injectToUnsafe(bean, field)
        }
    }

    override fun finalize(): HTMLElement {
        val node = downstream.finalize()
        rootCaptures.forEach { field ->
            node.injectToUnsafe(bean, field)
        }

        return node
    }
}

fun <T: Any> TagConsumer<HTMLElement>.inject(bean : T, rules : List<Pair<InjectCapture, KMutableProperty1<T, out HTMLElement>>>) : TagConsumer<HTMLElement> = InjectorConsumer(this, bean, rules)
fun <T: Any> HTMLElement.appendAndInject(bean : T, rules : List<Pair<InjectCapture, KMutableProperty1<T, out HTMLElement>>>, block : TagConsumer<HTMLElement>.() -> Unit) : List<HTMLElement> = append {
    InjectorConsumer(this@append, bean, rules).block()
    Unit
}

