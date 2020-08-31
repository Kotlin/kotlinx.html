package kotlinx.html

import kotlinx.html.impl.DelegatingMap

open class HTMLTag<E>(
    override val tagName: String,
    override val consumer: TagConsumer<*, E>,
    initialAttributes: Map<String, String>,
    override val namespace: String? = null,
    override val inlineTag: Boolean,
    override val emptyTag: Boolean,
) : Tag<E> {
    
    @Suppress("LeakingThis")
    override val attributes: DelegatingMap<E> = DelegatingMap(initialAttributes, this) { consumer }
    
    override val attributesEntries: Collection<Map.Entry<String, String>>
        get() = attributes.immutableEntries
}
