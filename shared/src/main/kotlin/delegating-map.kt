package html4k.impl

import html4k.Tag
import html4k.TagConsumer
import java.util.HashMap

class DelegatingMap(initialValues : Map<String, String>, val tag : Tag, val consumer : () -> TagConsumer<*>) : MutableMap<String, String> {
    private val backing = linkedMapOf<String, String>().let { it.putAll(initialValues); it }

    override fun size(): Int = backing.size()
    override fun isEmpty(): Boolean = backing.isEmpty()
    override fun containsKey(key: Any?): Boolean = backing.containsKey(key)
    override fun containsValue(value: Any?): Boolean = backing.containsValue(value)
    override fun get(key: Any?): String? = backing[key]

    override fun put(key: String, value: String): String? {
        val old = backing.put(key, value)
        if (old != value) {
            consumer().onTagAttributeChange(tag, key, value)
        }

        return old
    }

    override fun remove(key: Any?): String? =
        backing.remove(key)?.let { removed ->
            if (key is String) {
                consumer().onTagAttributeChange(tag, key, null)
            }

            removed
        }

    override fun putAll(m: Map<out String, String>) {
        m.entrySet().forEach { e ->
            put(e.getKey(), e.getValue())
        }

//        m.forEach { e ->
//            put(e.getKey(), e.getValue())
//        }
    }

    override fun clear() {
        backing.forEach { consumer().onTagAttributeChange(tag, it.getKey(), null) }
        backing.clear()
    }

    override fun keySet(): MutableSet<String> {
        return backing.keySet()  // TODO we need to handle changes too
    }

    override fun values(): MutableCollection<String> {
        return backing.values()   // TODO we need to handle changes too
    }

    override fun entrySet(): MutableSet<MutableMap.MutableEntry<String, String>> {
        return backing.entrySet()  // TODO we need to handle changes too
    }
}