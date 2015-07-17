package kotlinx.html.injector

import kotlinx.html.*
import kotlinx.html.dom.append
import kotlinx.html.dom.createTree
import org.w3c.dom.*
import kotlin.properties.Delegates
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KMutableProperty1

fun <F : Any, T : Any> F.injectTo(bean : T, field : KMutableProperty1<T, in F>) {
    field.set(bean, this)
}

private fun <F : Any, T : Any> F.injectToUnsafe(bean : T, field : KMutableProperty1<T, out F>) {
    @suppress("UNCHECKED_CAST")
    val unsafe = field as KMutableProperty1<T, F>
    injectTo(bean, unsafe)
}

public interface InjectCapture
public class InjectByClassName(val className : String) : InjectCapture
public class InjectByTagName(val tagName : String) : InjectCapture
public object InjectRoot : InjectCapture
public interface CustomCapture : InjectCapture {
    fun apply(element : HTMLElement) : Boolean
}

class InjectorConsumer<T>(val downstream : TagConsumer<HTMLElement>, val bean : T, rules : List<Pair<InjectCapture, KMutableProperty1<T, out HTMLElement>>>) : TagConsumer<HTMLElement> by downstream {

    private val classesMap = rules
            .filter { it.first is InjectByClassName }
            .map { it.first as InjectByClassName to it.second }
            .groupBy { it.first.className }
            .mapValues { it.getValue().map {it.second} }

    private val tagNamesMap = rules
            .filter { it.first is InjectByTagName }
            .map { it.first as InjectByTagName to it.second }
            .groupBy { it.first.tagName.toLowerCase() }
            .mapValues { it.getValue().map {it.second} }

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

public fun <T> TagConsumer<HTMLElement>.inject(bean : T, rules : List<Pair<InjectCapture, KMutableProperty1<T, out HTMLElement>>>) : TagConsumer<HTMLElement> = InjectorConsumer(this, bean, rules)
public fun <T> HTMLElement.appendAndInject(bean : T, rules : List<Pair<InjectCapture, KMutableProperty1<T, out HTMLElement>>>, block : TagConsumer<HTMLElement>.() -> Unit) : List<HTMLElement> = append {
    InjectorConsumer(this@append, bean, rules).block()
    Unit
}

