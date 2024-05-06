package kotlinx.html.generate

val reservedNames = setOf("class", "val", "var", "object", "true", "false", "as", "is", "for")

fun String.replaceIfReserved() = if (this in reservedNames) "html" + this.capitalize() else this

fun List<String>.toAttributeValues() : List<AttributeEnumValue> =
        map { AttributeEnumValue(it, if (it == "_") it else it.humanize().replaceIfReserved()) }

fun Appendable.enumObject(attribute : AttributeInfo) {
    val name = attribute.enumTypeName

    appendLine("@Suppress(\"unused\", \"ConstPropertyName\")")
    clazz(Clazz(name, isObject = true)) {
        attribute.enumValues.forEach {
            append("    ")
            variable(Var(it.fieldName, "String", varType = VarType.CONST, defaultValue = "\"${it.realName}\""))
            emptyLine()
        }

        emptyLine()
        append("    ")
        variable(
            Var(
                name = "values",
                type = "List<String>",
                defaultValue = attribute
                    .enumValues
                    .joinToString(", ", "listOf(", ")") { "\"${it.fieldName}\"" },
            )
        )
        emptyLine()
    }

    emptyLine()
}

fun Appendable.enum(attribute : AttributeInfo) {
    val name = attribute.enumTypeName
    val realValue = Var(name = "realValue", type = "String", varType = VarType.IMMUTABLE, override = true)

    appendLine("@Suppress(\"unused\", \"EnumEntryName\")")
    append("enum ")
    clazz(Clazz(name, variables = listOf(realValue), parents = listOf("AttributeEnum"))) {
        attribute.enumValues.forEachIndexed { idx, it ->
            append("    ")

            val deprecated = findEnumDeprecation(attribute, it)
            enumEntry(it.fieldName, deprecated, listOf("\"${it.realName}\""))

            if (idx != attribute.enumValues.lastIndex) {
                append(",")
            }

            appendLine()
        }
    }

    emptyLine()
    append("internal ")
    variable(
        Var(
            name = name.decapitalize() + "Values",
            type = "Map<String, $name>",
            varType = VarType.IMMUTABLE,
            defaultValue = "$name.entries.associateBy { it.realValue }",
        ),
    )
    emptyLine()
}