package html4k.generate

val globalSuggestedAttributes = listOf(
        "a" to "href",
        "a" to "target",
        "img" to "src",
        "script" to "type",
        "script" to "src",
        "div" to "class"
).groupBy { it.first }.mapValues { it.getValue().map {it.second} }

