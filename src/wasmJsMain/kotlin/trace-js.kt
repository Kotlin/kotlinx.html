package kotlinx.html.consumers

import kotlinx.html.*

fun <R> TagConsumer<R>.trace() : TagConsumer<R> = trace(println = { consoleInfo(it) })

private fun consoleInfo(message: String) {
    js("console.info(message)")
}