package html4k.generate


private fun List<AttributeEnumValue>.replaceAllIfStartsWithUnderscore() : List<AttributeEnumValue> =
    if (all { it.fieldName.startsWith("_") && it.fieldName.length() > 1 }) map { it.copy(fieldName = it.fieldName.substring(1)) } else this

private val reservedNames = setOf("class", "val", "var")

private fun String.replaceIfReserved() = if (this in reservedNames) this + "_" else this

private fun AttributeEnumValue.escapeUnsafeValues() : AttributeEnumValue = this.copy(fieldName =
        this.fieldName.replaceAll("[^\\w\\d_]", "_").replaceIfReserved()
)

fun List<String>.toAttributeValues() : List<AttributeEnumValue> =
        map {AttributeEnumValue(it, it)}
        .replaceAllIfStartsWithUnderscore()
        .map {it.escapeUnsafeValues()}

fun <O : Appendable> O.enum(name : String) {
    append("enum ")
    val realValue = Var("realValue", "String", false, true)
    clazz(Clazz(name, variables = listOf(realValue), parents = listOf("AttributeEnum"))) {
        Repository.attributeEnums[name].forEach {
            append("    ")
            append(it.fieldName)
            append(" : ")
            append(name)
            append("(\"")
            append(it.realName)
            append("\")\n")
        }
    }

    emptyLine()
    variable(Var(name.decapitalize() + "Values", "Map<String, $name>", false, defaultValue = "${name}.values().toMap { it.name() }"))
    emptyLine()
}