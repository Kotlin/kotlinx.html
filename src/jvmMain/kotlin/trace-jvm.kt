package kotlinx.html.consumers

import kotlinx.html.*

fun <R> TagConsumer<R>.trace(): TagConsumer<R> = TraceConsumer(this, println = ::println)

