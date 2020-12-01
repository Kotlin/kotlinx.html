package kotlinx.html.consumers

import kotlinx.html.TagConsumer

typealias DesktopTagConsumer<R> = TagConsumer<R, Nothing>

fun <R> DesktopTagConsumer<R>.trace(): DesktopTagConsumer<R> = TraceConsumer(this, println = ::println)
