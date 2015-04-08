package html4k.generate

import org.jetbrains
import java.io.File
import java.io.FileOutputStream
import java.io.Writer
import java.util.ArrayList


fun main(args: Array<String>) {
    fillRepository()

    val packg = "html4k"
    val todir = "shared/src/main/kotlin/generated"
    val jsdir = "js/src/main/kotlin/generated"
    File(todir).mkdirs()
    File(jsdir).mkdirs()

    FileOutputStream("$todir/gen-attr-traits.kt").writer().use {
        it.with {
            packg(packg)
            emptyLine()
            import("html4k.*")
            import("html4k.impl.*")
            emptyLine()

            warning()
            emptyLine()
            emptyLine()

            Repository.attributeFacades.values().forEach {
                facade(it)
                emptyLine()
            }
        }
    }

    FileOutputStream("$todir/gen-htmltag.kt").writer().use {
        it.with {
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
                            Var("tagName", "String", override = true),
                            Var("consumer", "TagConsumer<*>", override = true),
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
                    val probablyContentOnly = it.possibleChildren.isEmpty()
                    htmlTagBuilderMethod(it, true)
                    if (probablyContentOnly) {
                        htmlTagBuilderMethod(it, false)
                    }

                    val someEnumAttribute = it.attributes.filter { it.type == AttributeType.ENUM && it.enumValues.isNotEmpty() }.maxBy { it.enumValues.size() } // ??
                    if (someEnumAttribute != null) {
                        htmlTagEnumBuilderMethod(it, true, someEnumAttribute)
                        if (probablyContentOnly) {
                            htmlTagEnumBuilderMethod(it, false, someEnumAttribute)
                        }
                    }

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
        }
    }

    FileOutputStream("$todir/gen-builders.kt").writer().use {
        it.with {
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
        }
    }

    Repository.tags.values().groupBy { it.name[0] }.entrySet().forEach { e ->
        FileOutputStream("$todir/gen-tags-${e.getKey()}.kt").writer("UTF-8").use {
            it.with {
                packg(packg)
                emptyLine()
                import("html4k.*")
                import("html4k.impl.*")
                emptyLine()

                warning()
                emptyLine()
                emptyLine()

                e.getValue().forEach {
                    tagClass(it, emptySet())
                }
            }
        }
    }

    FileOutputStream("$todir/gen-consumer-tags.kt").writer("UTF-8").use {
        it.with {
            packg(packg)
            emptyLine()
            import("html4k.*")
            emptyLine()

            warning()
            emptyLine()
            emptyLine()

            Repository.tags.values().forEach {
                if (it.possibleChildren.isEmpty()) {
                    consumerBuilderShared(it, false)
                }
                consumerBuilderShared(it, true)
                emptyLine()
            }
        }
    }

    FileOutputStream("$jsdir/gen-consumer-tags.kt").writer("UTF-8").use {
        it.with {
            packg(packg + ".js")
            emptyLine()
            import("html4k.*")
            import("kotlin.js.dom.html.*")
            import("kotlin.js.dom.html5.*")
            emptyLine()

            warning()
            emptyLine()
            emptyLine()

            Repository.tags.values().forEach {
                if (it.possibleChildren.isEmpty()) {
                    consumerBuilderJS(it, false)
                }
                consumerBuilderJS(it, true)
                emptyLine()
            }
        }
    }

    FileOutputStream("$todir/gen-enums.kt").writer("UTF-8").use {
        it.with {
            packg(packg)
            emptyLine()
            import("html4k.*")
            emptyLine()

            warning()
            emptyLine()
            emptyLine()

            fun genEnumAttribute(attribute : AttributeInfo) {
                if (attribute.type == AttributeType.ENUM) {
                    enum(attribute)
                } else {
                    enumObject(attribute)
                }
            }

            Repository.attributeFacades.values().forEach { facade ->
                facade.attributes.filter {it.enumValues.isNotEmpty()}.filter {!isAtrributeExcluded(it.name)}.forEach { attribute ->
                    genEnumAttribute(attribute)
                }
            }

            Repository.tags.values().forEach { tag ->
                tag.attributes.filter {it.enumValues.isNotEmpty()}.filter { !isAtrributeExcluded(it.name) }.forEach { attribute ->
                    genEnumAttribute(attribute)
                }
            }
        }
    }

    FileOutputStream("$todir/gen-attributes.kt").writer("UTF-8").use {
        it.with {
            packg(packg)
            emptyLine()
            import("html4k.*")
            emptyLine()

            warning()
            emptyLine()
            emptyLine()

            Repository.attributeDelegateRequests.toList().forEach {
                attributePseudoDelegate(it)
            }
        }
    }
}