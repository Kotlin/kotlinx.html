package kotlinx.html

import kotlin.js.Date

actual fun currentTimeMillis(): Long = Date().getTime().toLong()
