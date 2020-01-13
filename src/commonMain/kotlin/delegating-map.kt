package kotlinx.html.impl

import kotlinx.html.*

class DelegatingMap(initialValues : Map<String, String>, private val tag : Tag, private val consumer : () -> TagConsumer<*>) : MutableMap<String, String> {
    private var backing: Map<String, String> = initialValues
    private var backingMutable = false

    override val size: Int
        get() = backing.size

    override fun isEmpty(): Boolean = backing.isEmpty()

    override fun containsKey(key: String): Boolean = backing.containsKey(key)
    override fun containsValue(value: String): Boolean = backing.containsValue(value)
    override fun get(key: String): String? = backing[key]

    override fun put(key: String, value: String): String? {
        val mutable = switchToMutable()

        val old = mutable.put(key, value)
        if (old != value) {
            consumer().onTagAttributeChange(tag, key, value)
        }

        return old
    }

    override fun remove(key: String): String? {
        val mutable = switchToMutable()

        return mutable.remove(key)?.let { removed ->
            consumer().onTagAttributeChange(tag, key, null)
            removed
        }
    }

    override fun putAll(from: Map<out String, String>) {
        if (from.isEmpty()) return

        val consumer = consumer()
        val mutable = switchToMutable()

        from.entries.forEach { e ->
            if (mutable.put(e.key, e.value) != e.value) {
                consumer.onTagAttributeChange(tag, e.key, e.value)
            }
        }
    }

    override fun clear() {
        backing.forEach { e -> consumer().onTagAttributeChange(tag, e.key, null) }
        backing = emptyMap()
        backingMutable = false
    }

    val immutableEntries: Collection<Map.Entry<String, String>>
        get() = backing.entries

    private fun switchToMutable(): MutableMap<String, String> = if (backingMutable) {
        backing
    } else {
        backingMutable = true
        backing = LinkedHashMap(backing)
        backing
    } as MutableMap

    override val keys: MutableSet<String>
        get() = switchToMutable().keys  // TODO we need to handle changes too

    override val values: MutableCollection<String>
        get() = switchToMutable().values  // TODO we need to handle changes too

    override val entries: MutableSet<MutableMap.MutableEntry<String, String>>
        get() = switchToMutable().entries   // TODO we need to handle changes too
}
