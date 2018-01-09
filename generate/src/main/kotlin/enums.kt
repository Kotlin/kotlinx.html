package kotlinx.html.generate

import kotlinx.html.generate.humanize.humanize

val reservedNames = setOf("class", "val", "var", "object", "true", "false", "as", "is", "for")

fun String.replaceIfReserved() = if (this in reservedNames) "html" + this.capitalize() else this

fun List<String>.toAttributeValues() : List<AttributeEnumValue> =
        map { AttributeEnumValue(it, if (it == "_") it else it.humanize().replaceIfReserved()) }

fun Appendable.enumObject(attribute : AttributeInfo) {
    val name = attribute.enumTypeName

    appendln("@Suppress(\"unused\")")
    clazz(Clazz(name, isObject = true)) {
        attribute.enumValues.forEach {
            append("    ")
            variable(Var(it.fieldName, "String", false, defaultValue = "\"${it.realName}\""))
            emptyLine()
        }

        emptyLine()
        append("    ")
//        append("private ")
        variable(Var("values", "List<String>", defaultValue = attribute.enumValues.map {"\"${it.fieldName}\""}.joinToString(", ", "listOf(", ")")))
        emptyLine()
    }

    emptyLine()
}

fun Appendable.enum(attribute : AttributeInfo) {
    val name = attribute.enumTypeName
    val realValue = Var("realValue", "String", false, true)

    appendln("@Suppress(\"unused\")")
    append("enum ")
    clazz(Clazz(name, variables = listOf(realValue), parents = listOf("AttributeEnum"))) {
        attribute.enumValues.forEachIndexed { idx, it ->
            append("    ")

            val deprecated = deprecated.firstOrNull { p -> p.first.matches("""${attribute.enumTypeName}#${it.realName}""") }?.second
            enumEntry(it.fieldName, deprecated, listOf("\"${it.realName}\""))

            if (idx != attribute.enumValues.lastIndex) {
                append(",")
            }

            appendln()
        }
    }

    emptyLine()
    append("internal ")
    variable(Var(name.decapitalize() + "Values", "Map<String, $name>", false, defaultValue = "$name.values().associateBy { it.realValue }"))
    emptyLine()
}