package kotlinx.html.generate

fun String.quote() = "\"$this\""

fun <O : Appendable> O.attributePseudoDelegate(request: AttributeRequest) {
    val classNamePrefix = request.type.classPrefix
    val className = "${classNamePrefix}Attribute"

    append("internal ")
    variable(Var(request.delegatePropertyName, "Attribute<${request.typeName}>"))
    defineIs(StringBuilder {
        functionCallConsts(className, request.options)
    })
    emptyLine()
}

fun <O : Appendable> O.attributeProperty(attribute: AttributeInfo) {
    val attributeName = attribute.name
    val request = tagAttributeVar(attribute)
    append("\n    ")
    getter() defineIs(StringBuilder {
        append(request.delegatePropertyName)
        append(".")
        functionCall("get", listOf("this", attributeName.quote()))
    })
    append("    ")
    setter {
        append(request.delegatePropertyName)
        append(".")
        functionCall("set", listOf("this", attributeName.quote(), "newValue"))
    }

    emptyLine()
}

fun <O : Appendable> O.facade(facade: AttributeFacade) {
    clazz(Clazz(facade.name.capitalize() + "Facade", isTrait = true, parents = listOf("Tag"))) {
        facade.attributes.filter { !isAttributeExcluded(it.name) }.forEach { attribute ->
            if (attribute.name.isLowerCase() || attribute.name.toLowerCase() !in facade.attributeNames) {
                attributeProperty(attribute)
            }
        }
    }
}

fun <O : Appendable> O.eventProperty(parent: String, attribute: AttributeInfo) {
    variable(receiver = parent, variable = Var(
            name = attribute.fieldName + "Function",
            type = "(Event) -> Unit",
            mutable = true
    ))
    emptyLine()

    getter() defineIs(StringBuilder {
        append("throw ")
        functionCall("UnsupportedOperationException", listOf("You can't read variable ${attribute.fieldName}".quote()))
    })
    setter {
        append("EventAttribute.")
        functionCall("set", listOf(
                "this",
                attribute.name.quote(),
                "newValue"
        ))
    }
    emptyLine()
}