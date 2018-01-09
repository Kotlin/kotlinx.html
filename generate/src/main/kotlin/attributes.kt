package kotlinx.html.generate

fun String.quote() = "\"$this\""

fun Appendable.attributePseudoDelegate(request: AttributeRequest) {
    val classNamePrefix = request.type.classPrefix
    val className = "${classNamePrefix}Attribute"

    append("internal ")
    variable(Var(request.delegatePropertyName, "Attribute<${request.typeName}>"))
    defineIs(StringBuilder().apply {
        functionCallConsts(className, request.options)
    })
    emptyLine()
}

fun Appendable.attributeProperty(attribute: AttributeInfo, receiver: String? = null, indent: Int = 1) {
    val attributeName = attribute.name
    val request = tagAttributeVar(attribute, receiver, indent)
    append("\n")

    indent(indent)
    getter().defineIs(StringBuilder().apply {
        receiverDot(request.delegatePropertyName)
        functionCall("get", listOf("this", attributeName.quote()))
    })

    indent(indent)
    setter {
        receiverDot(request.delegatePropertyName)
        functionCall("set", listOf("this", attributeName.quote(), "newValue"))
    }

    emptyLine()
}

fun Appendable.facade(facade: AttributeFacade) {
    val facadeName = facade.name.capitalize() + "Facade"

    clazz(Clazz(facadeName, isInterface = true, parents = listOf("Tag"))) {
    }

    facade.attributes.filter { !isAttributeExcluded(it.name) }.forEach { attribute ->
        if (attribute.name.isLowerCase() || attribute.name.toLowerCase() !in facade.attributeNames) {
            attributeProperty(attribute, receiver = facadeName, indent = 0)
        }
    }
}

fun Appendable.eventProperty(parent: String, attribute: AttributeInfo) {
    variable(receiver = parent, variable = Var(
            name = attribute.fieldName + "Function",
            type = "(Event) -> Unit",
            mutable = true
    ))
    emptyLine()

    getter().defineIs(StringBuilder().apply {
        append("throw ")
        functionCall("UnsupportedOperationException", listOf("You can't read variable ${attribute.fieldName}".quote()))
    })
    setter {
        receiverDot("consumer")
        functionCall("onTagEvent", listOf(
                "this",
                attribute.name.quote(),
                "newValue"
        ))
    }
    emptyLine()
}
