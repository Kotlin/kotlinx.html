package kotlinx.html

import kotlinx.cinterop.*
import platform.posix.*

@OptIn(UnsafeNumber::class, ExperimentalForeignApi::class)
actual fun currentTimeMillis(): Long {
    memScoped {
        val timeHolder = alloc<time_tVar>()
        time(timeHolder.ptr)
        return timeHolder.value * 1000L
    }
}
