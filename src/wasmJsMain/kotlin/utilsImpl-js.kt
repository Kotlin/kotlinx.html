package kotlinx.html

import kotlin.js.*

actual fun currentTimeMillis(): Long = currentTimeMillisJs().toLong()

private fun currentTimeMillisJs(): Double =
    js("new Date().getTime()")