package kotlinx.html.generate

import kotlinx.html.generate.humanize.humanize


private fun List<AttributeEnumValue>.replaceAllIfStartsWithUnderscore() : List<AttributeEnumValue> =
    if (all { it.fieldName.startsWith("_") && it.fieldName.length() > 1 }) map { it.copy(fieldName = it.fieldName.substring(1)) } else this

val reservedNames = setOf("class", "val", "var", "object", "true", "false", "as", "is", "for")

fun String.replaceIfReserved() = if (this in reservedNames) this + "_" else this

fun String.escapeUnsafeValues() : String = replace("[^\\w\\d_]".toRegex(), "_").humanize().replaceIfReserved()

private fun AttributeEnumValue.escapeUnsafeValues() : AttributeEnumValue = copy(fieldName = fieldName.escapeUnsafeValues())

fun List<String>.toAttributeValues() : List<AttributeEnumValue> =
        map {AttributeEnumValue(it, it)}
        .replaceAllIfStartsWithUnderscore()
        .map {it.escapeUnsafeValues()}

fun <O : Appendable> O.enumObject(attribute : AttributeInfo) {
    val name = attribute.enumTypeName

    clazz(Clazz(name, isObject = true)) {
        attribute.enumValues.forEach {
            append("    ")
            variable(Var(it.fieldName, "String", false, defaultValue = "\"${it.realName}\""))
            emptyLine()
        }

        emptyLine()
        append("    ")
//        append("private ")
        variable(Var("values", "List<String>", defaultValue = attribute.enumValues.map {"\"${it.fieldName}\""}.join(", ", "listOf(", ")")))
        emptyLine()
    }

    emptyLine()
}

fun <O : Appendable> O.enum(attribute : AttributeInfo) {
    val name = attribute.enumTypeName
    val realValue = Var("realValue", "String", false, true)

    append("enum ")
    clazz(Clazz(name, variables = listOf(realValue), parents = listOf("AttributeEnum"))) {
        attribute.enumValues.forEachIndexed { idx, it ->
            append("    ")
            enumEntry(it.fieldName, listOf("\"${it.realName}\""))

            if (idx != attribute.enumValues.lastIndex) {
                append(",")
            }

            appendln()
        }
    }

    emptyLine()
    append("private ")
    variable(Var(name.decapitalize() + "Values", "Map<String, $name>", false, defaultValue = "$name.values().toMap { it.realValue }"))
    emptyLine()
}