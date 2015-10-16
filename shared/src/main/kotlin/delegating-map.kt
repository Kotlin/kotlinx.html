package kotlinx.html.impl

import kotlinx.html.Tag
import kotlinx.html.TagConsumer

class DelegatingMap(initialValues : Map<String, String>, val tag : Tag, val consumer : () -> TagConsumer<*>) : MutableMap<String, String> {
    private val backing = linkedMapOf<String, String>().let { it.putAll(initialValues); it }

    override val size: Int
        get() = backing.size

    override fun isEmpty(): Boolean = backing.isEmpty()

    override fun containsKey(key: String): Boolean = backing.containsKey(key)
    override fun containsValue(value: String): Boolean = backing.containsValue(value)
    override fun get(key: String): String? = backing[key]

    override fun put(key: String, value: String): String? {
        val old = backing.put(key, value)
        if (old != value) {
            consumer().onTagAttributeChange(tag, key, value)
        }

        return old
    }

    override fun remove(key: String): String? =
        backing.remove(key)?.let { removed ->
            if (key is String) {
                consumer().onTagAttributeChange(tag, key, null)
            }

            removed
        }

    override fun putAll(m: Map<out String, String>) {
        m.entries.forEach { e ->
            put(e.key, e.value)
        }

//        m.forEach { e ->
//            put(e.getKey(), e.getValue())
//        }
    }

    override fun clear() {
        backing.forEach { consumer().onTagAttributeChange(tag, it.key, null) }
        backing.clear()
    }

    override val keys: MutableSet<String>
        get() = backing.keys  // TODO we need to handle changes too

    override val values: MutableCollection<String>
        get() = backing.values  // TODO we need to handle changes too

    override val entries: MutableSet<MutableMap.MutableEntry<String, String>>
        get() = backing.entries   // TODO we need to handle changes too
}
