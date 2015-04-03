package html4k.generate

import java.io.File
import java.io.FileOutputStream
import java.io.Writer

fun main(args: Array<String>) {

    val packg = "html4k"

    Repository.attributes += listOf(
            AttributeInfo("href"),
            AttributeInfo("target", "Targets"),
            AttributeInfo("class", safeAlias = "classes")
    ).toMap { it.name }

    Repository.tags += listOf (
            TagInfo("a", listOf("span"), suggestedAttributes = listOf("href", "target")),
            TagInfo("span", listOf("span")),
            TagInfo("div", listOf("div", "p", "a", "span"), suggestedAttributes = listOf("class")),
            TagInfo("p", listOf("a", "p", "span")),
            TagInfo("head", listOf("title")),
            TagInfo("body", listOf("div", "p", "a", "span")),
            TagInfo("title"),
            TagInfo("html", listOf("head", "body"))
    ).toMap { it.name }

    FileOutputStream("shared/src/main/kotlin/gen-htmltag.kt").writer().use { it.with {
        packg(packg)
        emptyLine()
        import("html4k.*")
        import("html4k.impl.*")
        emptyLine()

        warning()
        emptyLine()
        emptyLine()

        val baseTagClass = Clazz(
                name = "HTMLTag",
                variables = listOf(
                        Var("name", "String", false, true),
                        Var("consumer", "TagConsumer<*>", false, true),
                        Var("initialAttributes", "Map<String, String>")
                ),
                parents = listOf("Tag"),
                isOpen = true
        )

        clazz(baseTagClass) {
            emptyLine()
            append("    ")
            variable(Var("attributes", "DelegatingMap", false, true))
            defineIs("DelegatingMap(initialAttributes, this) {consumer}")
            emptyLine()

            Repository.tags.values().forEach {
                if (it.possibleChildren.isEmpty()) {
                    htmlTagBuilderMethod(it, false)
                }
                htmlTagBuilderMethod(it, true)
                emptyLine()
            }

            append("""
    fun Entities.plus() {
        consumer.onTagContentEntity(this)
    }

    fun String.plus() {
        consumer.onTagContent(this)
    }

    fun CDATA(s : CharSequence) {
        consumer.onCDATA(s)
    }
            """)
        }
    }}

    FileOutputStream("shared/src/main/kotlin/gen-builders.kt").writer().use { it.with {
        packg(packg)
        emptyLine()
        import("html4k.*")
        import("html4k.impl.*")
        emptyLine()

        warning()
        emptyLine()
        emptyLine()

        Repository.tags.values().forEach {
            builderFunction(it)
        }
    }}

    FileOutputStream("shared/src/main/kotlin/gen-tags.kt").writer("UTF-8").use { it.with {
        packg(packg)
        emptyLine()
        import("html4k.*")
        import("html4k.impl.*")
        emptyLine()

        warning()
        emptyLine()
        emptyLine()

        Repository.tags.values().forEach {
            tagClass(it)
        }
    }}

    FileOutputStream("shared/src/main/kotlin/gen-consumer-tags.kt").writer("UTF-8").use { it.with {
        packg(packg)
        emptyLine()
        import("html4k.*")
        emptyLine()

        warning()
        emptyLine()
        emptyLine()

        Repository.tags.values().forEach {
            if (it.possibleChildren.isEmpty()) {
                consumerBuilder(it, false)
            }
            consumerBuilder(it, true)
            emptyLine()
        }
    }}
}