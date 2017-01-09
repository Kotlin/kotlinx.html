package kotlinx.html.consumers

import kotlinx.html.*

fun <R> TagConsumer<R>.trace() : TagConsumer<R> = trace(println = { console.info(it) })
