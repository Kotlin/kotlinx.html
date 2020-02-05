package kotlinx.html

expect fun <T : Tag> T.visitTag(block: T.() -> Unit)

expect fun <T : Tag, R> T.visitTagAndFinalize(consumer: TagConsumer<R>, block: T.() -> Unit): R