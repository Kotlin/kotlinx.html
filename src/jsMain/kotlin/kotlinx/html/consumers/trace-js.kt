package kotlinx.html.consumers

import kotlinx.html.TagConsumer

fun <R, E> TagConsumer<R, E>.trace(): TagConsumer<R, E> = trace(println = { console.info(it) })
