package kotlinx.html

expect fun getTimeOfDay():Pair<Int, Int>

actual fun currentTimeMillis(): Long = getTimeOfDay().run {
    (first * 1_000L) + (second / 1_000L)
}
