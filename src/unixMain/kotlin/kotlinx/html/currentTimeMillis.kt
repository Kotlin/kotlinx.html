package kotlinx.html

import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.posix.gettimeofday
import platform.posix.timeval

actual fun currentTimeMillis(): Long = memScoped {
    val timeVal = alloc<timeval>()
    gettimeofday(timeVal.ptr, null)
    @Suppress("CAST_NEVER_SUCCEEDS")
    val sec = timeVal.tv_sec as Long //Required to shut up commonizer as it does not resolve typealiases. Actual type is indeed Long
    @Suppress("CAST_NEVER_SUCCEEDS")
    val uSec = timeVal.tv_usec as Long
    (sec * 1_000L) + (uSec / 1_000L)
}
