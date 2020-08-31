package kotlinx.html

inline fun <T : Tag<E>, E> T.visitTag(block: T.() -> Unit) {
    consumer.onTagStart(this)
    try {
        this.block()
    } catch (err: Throwable) {
        consumer.onTagError(this, err)
    } finally {
        consumer.onTagEnd(this)
    }
}

inline fun <T : Tag<E>, R, E> T.visitTagAndFinalize(consumer: TagConsumer<R, E>, block: T.() -> Unit): R {
    if (this.consumer !== consumer) {
        throw IllegalArgumentException("Wrong exception")
    }

    visitTag(block)
    return consumer.finalize()
}
