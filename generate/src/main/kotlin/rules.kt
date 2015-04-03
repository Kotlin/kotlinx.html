package html4k.generate

import java.util.regex.Pattern

val globalSuggestedAttributes = listOf(
        "a" to "href",
        "a" to "target",
        "img" to "src",
        "script" to "type",
        "script" to "src",
        "div" to "class"
).groupBy { it.first }.mapValues { it.getValue().map {it.second} }

val wellKnownWords = listOf("span", "class", "enabled?", "edit(able)?",
        "^on", "encoded?", "form", "type",
        "run", "href", "drag(gable)?",
        "over", "mouse",
        "start(ed)?", "end(ed)?", "stop", "key", "load(ed)?", "check(ed)?",
        "time", "ready", "content", "changed?",
        "click", "play(ing)?", "context",
        "row", "col", "group(ed)?", "auto",
        "list", "field", "data", "block", "script",
        "item", "area"
).map { Pattern.compile(it, Pattern.CASE_INSENSITIVE) }