package kotlinx.html

import kotlin.js.*

actual fun currentTimeMillis(): Long = Date().getTime().toLong()
