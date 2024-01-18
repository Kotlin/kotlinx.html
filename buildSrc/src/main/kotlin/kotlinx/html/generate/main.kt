package kotlinx.html.generate

import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec
import java.io.File
import java.io.FileOutputStream
import java.io.InputStreamReader

fun generate(pkg: String, todir: String, jsdir: String, wasmJsDir: String) {
    val repository = Repository()
    fillRepository(repository)
    fillKdocRepositoryExtension()

    File(todir).mkdirs()
    File(jsdir).mkdirs()
    File(wasmJsDir).mkdirs()

    writeIntoFile("$todir/gen-attr-traits.kt") {
        packg(pkg)
        emptyLine()
        import("kotlinx.html.*")
        import("kotlinx.html.impl.*")
        emptyLine()

        doNotEditWarning()
        emptyLine()
        emptyLine()

        repository.attributeFacades.values.forEach {
            facade(repository,it)
            emptyLine()
        }
    }

    repository.tags.values.filterIgnored().groupBy { it.name[0] }.entries.forEach { e ->
        writeIntoFile("$todir/gen-tags-${e.key}.kt") {
            packg(pkg)
            emptyLine()
            import("kotlinx.html.*")
            import("kotlinx.html.impl.*")
            import("kotlinx.html.attributes.*")
            emptyLine()

            doNotEditWarning()
            emptyLine()
            emptyLine()

            e.value.forEach {
                tagClass(repository, it, emptySet())
            }
        }
    }

    generateConsumerTags(repository, "$todir/gen-consumer-tags.kt", pkg) { tag, blockOrContent ->
        consumerBuilderSharedPoet(tag, blockOrContent)
    }

    generateConsumerTags(repository, "$jsdir/gen-consumer-tags-js.kt", "$pkg.js") { tag, blockOrContent ->
        consumerBuilderJsPoet(tag, blockOrContent, "HTMLElement")
    }
    generateEventAttrs(repository, "$jsdir/gen-event-attrs-js.kt", "$pkg.js")

    generateConsumerTags(repository, "$wasmJsDir/gen-consumer-tags-wasm-js.kt", "$pkg.js") { tag, blockOrContent ->
        consumerBuilderJsPoet(tag, blockOrContent, "Element")
    }
    generateEventAttrs(repository, "$wasmJsDir/gen-event-attrs-wasm-js.kt", "$pkg.js")

    FileOutputStream("$todir/gen-enums.kt").writer(Charsets.UTF_8).use {
        it.with {
            packg(pkg)
            emptyLine()
            import("kotlinx.html.*")
            emptyLine()

            doNotEditWarning()
            emptyLine()
            emptyLine()

            fun genEnumAttribute(attribute: AttributeInfo) {
                if (!isEnumExcluded(attribute.enumTypeName)) {
                    if (attribute.type == AttributeType.ENUM) {
                        enum(attribute)
                    } else {
                        enumObject(attribute)
                    }
                }
            }

            repository.attributeFacades.values.forEach { facade ->
                facade.attributes.filter { it.enumValues.isNotEmpty() }.filter { !isAttributeExcluded(it.name) }
                    .forEach { attribute ->
                        genEnumAttribute(attribute)
                    }
            }

            repository.tags.values.filterIgnored().forEach { tag ->
                tag.attributes.filter { it.enumValues.isNotEmpty() }.filter { !isAttributeExcluded(it.name) }
                    .forEach { attribute ->
                        genEnumAttribute(attribute)
                    }
            }
        }
    }

    FileOutputStream("$todir/gen-attributes.kt").writer(Charsets.UTF_8).use {
        it.with {
            packg(pkg)
            emptyLine()
            import("kotlinx.html.*")
            import("kotlinx.html.attributes.*")
            emptyLine()

            doNotEditWarning()
            emptyLine()
            emptyLine()

            repository.attributeDelegateRequests.toList().forEach {
                attributePseudoDelegate(it)
            }
        }
    }

    FileOutputStream("$todir/gen-tag-unions.kt").writer(Charsets.UTF_8).use {
        with(it) {
            packg(pkg)
            emptyLine()
            import("kotlinx.html.*")
            import("kotlinx.html.impl.*")
            import("kotlinx.html.attributes.*")
            emptyLine()

            doNotEditWarning()
            emptyLine()
            emptyLine()

            repository.groupUnions.values.forEach { union ->
                clazz(
                    Clazz(
                        name = union.name,
                        isInterface = true,
                        parents = union.superGroups + "Tag"
                    )
                ) {}

                emptyLine()
            }

            emptyLine()
            emptyLine()

            repository.groupUnions.values.forEach { union ->
                (union.additionalTags + union.ambiguityTags).mapNotNull { repository.tags[it] }.filterIgnored()
                    .forEach { tag ->
                        htmlTagBuilders(union.name, tag)
                    }

                emptyLine()
            }
        }
    }

    FileOutputStream("$todir/gen-tag-groups.kt").writer(Charsets.UTF_8).use {
        with(it) {
            packg(pkg)
            emptyLine()
            import("kotlinx.html.*")
            import("kotlinx.html.impl.*")
            import("kotlinx.html.attributes.*")
            emptyLine()

            doNotEditWarning()
            emptyLine()
            emptyLine()

            repository.tagGroups.values.forEach { group ->
                val unions = repository.unionsByGroups[group.name].orEmpty().map { it.name }

                clazz(Clazz(name = group.typeName, parents = unions + "Tag", isPublic = true, isInterface = true)) {
                }
                emptyLine()
            }

            repository.tagGroups.values.forEach { group ->
                val receiver = group.typeName
                val unions = repository.unionsByGroups[group.name].orEmpty()

                group.tags.mapNotNull { repository.tags[it] }.filterIgnored()
                    .filter { tag -> unions.count { tag.name in it.intersectionTags } == 0 }.forEach {
                        htmlTagBuilders(receiver, it)
                    }
            }
        }
    }

    FileOutputStream("$todir/gen-entities.kt").writer(Charsets.UTF_8).use {
        it.with {
            packg(pkg)
            emptyLine()
            import("kotlinx.html.*")
            emptyLine()

            doNotEditWarning()
            emptyLine()
            emptyLine()

            append("enum ")
            clazz(Clazz(name = "Entities")) {
                InputStreamReader("entities.txt".asResourceUrl().openStream()).useLines { lines ->
                    lines.filter { it.isNotEmpty() }.forEachIndexed { idx, ent ->
                        if (idx > 0) {
                            append(",")
                        }
                        indent()
                        append(ent)
                        emptyLine()
                    }
                }

                append(";")
                appendLine()

                variable(Var(name = "text", type = "String"))
                appendLine()
                getter()
                defineIs(StringBuilder().apply {
                    append("&".quote())
                    append(" + ")
                    receiverDot("this")
                    functionCall("toString", emptyList())
                    append(" + ")
                    append(";".quote())
                })
                appendLine()
            }
        }
    }

    generateParentInterfaces(repository, todir, pkg)
}

fun generateJsTagTests(jsdir: String, wasmJsDir: String) {
    val repository = Repository()
    fillRepository(repository)
    fillKdocRepositoryExtension()

    File(jsdir).mkdirs()
    File(wasmJsDir).mkdirs()

    generateTagTests(repository, "$jsdir/gen-tag-tests.kt", "HTMLElement")
    generateTagTests(repository, "$wasmJsDir/gen-tag-tests.kt", "Element")
}

private fun generateTagTests(repository: Repository, filePath: String, defaultTagConsumer: String) {
    val wrapper = FunSpec.builder("wrapper")
        .returns(tagConsumerJs(defaultTagConsumer))
        .addStatement("return document.body!!.append")
        .build()

    val testFunctions = repository.tags.values.filterIgnored().map { tag ->
        FunSpec.builder("test${tag.memberName.capitalize()}")
            .addAnnotation(ClassName("kotlin.test", "Test"))
            .addStatement("wrapper().${tag.memberName} {}")
            .build()
    }


    writeIntoFile(filePath) {
        import("kotlinx.html.js.*")
        emptyLine()
        doNotEditWarning()
        emptyLine()

        writeKotlinPoet {
            addImport("kotlinx.browser", "document")
            addImport("kotlinx.html.dom", "append")
            addAliasedImport(ClassName("kotlin.test", "Test"), "test")
            addType(
                TypeSpec
                    .classBuilder("JsTagCastTests")
                    .addFunction(wrapper)
                    .addFunctions(testFunctions)
                    .build()
            )
        }
    }
}

fun interface ConsumerBuilder {
    operator fun invoke(tag: TagInfo, blockOrContent: Boolean): FunSpec
}

private fun generateConsumerTags(
    repository: Repository,
    filePath: String,
    pkg: String,
    consumerBuilder: ConsumerBuilder,
) {
    val functions = sequence {
        repository.tags.values.filterIgnored().forEach { tag ->
            val lowercaseTag = tag.name.lowercase()
            val contentlessTag = lowercaseTag in contentlessTags
            val hasNoPossibleChildren = tag.possibleChildren.isEmpty()
            val isNotEmpty = lowercaseTag !in emptyTags

            if (hasNoPossibleChildren && isNotEmpty && !contentlessTag) {
                yield(consumerBuilder(tag, false))
            } else if (contentlessTag) {
                yield(
                    consumerBuilder(tag, false)
                        .toBuilder()
                        .addSuppressAnnotation("DEPRECATION")
                        .addDeprecatedAnnotation("This tag doesn't support content or requires unsafe (try unsafe {})")
                        .build()
                )
            }

            yield(consumerBuilder(tag, true))
        }
    }

    writeIntoFile(filePath) {
        doNotEditWarning()
        emptyLine()
        packg(pkg)
        emptyLine()
        import("kotlinx.html.*")
        import("kotlinx.html.attributes.*")

        emptyLine()

        writeKotlinPoet {
            functions.forEach { func ->
                addFunction(func)
            }
        }
    }
}

private fun generateEventAttrs(repository: Repository, file: String, pkg: String) {
    val isEventAttribute = { attributeName: String ->
        attributeName.startsWith("on")
    }
    val properties = sequence {
        repository
            .attributeFacades
            .filter { facade -> facade.value.attributeNames.any(isEventAttribute) }
            .forEach { facade ->
                facade.value.attributes.filter { it.name.startsWith("on") }.forEach {
                    val parentName = facade.value.name.capitalize() + "Facade"
                    val parent = ClassName("kotlinx.html", parentName)

                    yield(eventProperty(parent, it, shouldUnsafeCast = false))
                }
            }
    }

    writeIntoFile(file) {
        packg(pkg)
        emptyLine()
        doNotEditWarning()
        emptyLine()

        writeKotlinPoet {
            properties.forEach { property ->
                addProperty(property)
            }
        }
    }
}

private fun writeIntoFile(file: String, contentWriter: Appendable.() -> Unit) {
    FileOutputStream(file).writer(Charsets.UTF_8).use {
        it.with {
            contentWriter()
        }
    }
}

private fun Appendable.writeKotlinPoet(builder: FileSpec.Builder.() -> Unit) {
    FileSpec
        .builder("", "")
        .apply(builder)
        .build()
        .writeTo(this)
}