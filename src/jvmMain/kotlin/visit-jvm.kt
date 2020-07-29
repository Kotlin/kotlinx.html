package kotlinx.html

actual inline fun <T : Tag> T.visitTag(block: T.() -> Unit) {
  consumer.onTagStart(this)
  try {
    this.block()
  } catch (err: Throwable) {
    consumer.onTagError(this, err)
  } finally {
    consumer.onTagEnd(this)
  }
}

actual inline fun <T : Tag, R> T.visitTagAndFinalize(consumer: TagConsumer<R>, block: T.() -> Unit): R {
  if (this.consumer !== consumer) {
    throw IllegalArgumentException("Wrong exception")
  }

    visitTag(block)
  return consumer.finalize()
}
