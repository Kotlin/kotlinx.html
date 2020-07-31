package kotlinx.html

expect fun <T : Tag<E>, E> T.visitTag(block: T.() -> Unit)

expect fun <T : Tag<E>, R, E> T.visitTagAndFinalize(consumer: TagConsumer<R, E>, block: T.() -> Unit): R
