package kotlinx.html.injector

import kotlinx.html.Tag
import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import org.w3c.dom.Element
import org.w3c.dom.asList
import kotlin.reflect.KMutableProperty1

fun <F : Any, T : Any> F.injectTo(bean: T, field: KMutableProperty1<T, in F>) {
    field.set(bean, this)
}

@Suppress("UnsafeCastFromDynamic")
private fun <F : Any, T : Any> F.injectToUnsafe(bean: T, field: KMutableProperty1<T, out F>) {
    injectTo(bean, field as KMutableProperty1<T, in F>)
}

interface InjectCapture
class InjectByClassName(val className: String) : InjectCapture
class InjectByTagName(val tagName: String) : InjectCapture
object InjectRoot : InjectCapture
interface CustomCapture : InjectCapture {
    fun apply(element: Element): Boolean
}

class InjectorConsumer<out T : Any>(
    val downstream: TagConsumer<Element>,
    val bean: T,
    rules: List<Pair<InjectCapture, KMutableProperty1<T, out Element>>>,
) : TagConsumer<Element> by downstream {

    private val classesMap: Map<String, List<KMutableProperty1<T, out Element>>> = rules
        .filter { it.first is InjectByClassName }
        .map { it.first as InjectByClassName to it.second }
        .groupBy({ it.first.className }, { it.second })

    private val tagNamesMap = rules
        .filter { it.first is InjectByTagName }
        .map { it.first as InjectByTagName to it.second }
        .groupBy({ it.first.tagName.lowercase() }, { it.second })

    private val rootCaptures = rules.filter { it.first == InjectRoot }.map { it.second }
    private val customCaptures =
        rules.filter { it.first is CustomCapture }.map { it.first as CustomCapture to it.second }

    override fun onTagEnd(tag: Tag) {
        downstream.onTagEnd(tag)

        val node = downstream.finalize()

        if (classesMap.isNotEmpty()) {
            node.classList.asList().flatMap { clazz: JsString ->
                classesMap[clazz.toString()] ?: emptyList()
            }.forEach { field ->
                node.injectToUnsafe(bean, field)
            }
        }

        if (tagNamesMap.isNotEmpty()) {
            tagNamesMap[node.tagName.lowercase()]?.forEach { field ->
                node.injectToUnsafe(bean, field)
            }
        }

        customCaptures.filter { it.first.apply(node) }.map { it.second }.forEach { field ->
            node.injectToUnsafe(bean, field)
        }
    }

    override fun finalize(): Element {
        val node = downstream.finalize()
        rootCaptures.forEach { field ->
            node.injectToUnsafe(bean, field)
        }

        return node
    }
}

fun <T : Any> TagConsumer<Element>.inject(
    bean: T,
    rules: List<Pair<InjectCapture, KMutableProperty1<T, out Element>>>,
): TagConsumer<Element> = InjectorConsumer(this, bean, rules)

fun <T : Any> Element.appendAndInject(
    bean: T,
    rules: List<Pair<InjectCapture, KMutableProperty1<T, out Element>>>,
    block: TagConsumer<Element>.() -> Unit,
): List<Element> = append {
    InjectorConsumer(this@append, bean, rules).block()
    Unit
}
