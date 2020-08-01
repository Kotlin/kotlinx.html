package kotlinx.html

import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import platform.posix.mingw_gettimeofday
import platform.posix.timeval

actual fun currentTimeMillis(): Long = memScoped {
  val timeVal = alloc<timeval>()
  mingw_gettimeofday(timeVal.ptr, null) // mingw: doesn't expose gettimeofday, but mingw_gettimeofday
  val sec = timeVal.tv_sec
  val usec = timeVal.tv_usec
  (sec * 1_000L) + (usec / 1_000L)
}
