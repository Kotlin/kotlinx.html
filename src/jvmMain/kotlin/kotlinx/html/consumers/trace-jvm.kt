package kotlinx.html.consumers

import kotlinx.html.dom.JVMTagConsumer

fun <R> JVMTagConsumer<R>.trace(): JVMTagConsumer<R> = TraceConsumer(this, println = ::println)

