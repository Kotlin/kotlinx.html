package html4k.generate

import java.util.ArrayList

fun String.quote() = "\"$this\""

fun <O : Appendable> O.attribute(request : AttributeRequest) {
    val classNamePrefix = when (request.type) {
        "String" -> "String"
        "Boolean" -> "Boolean"
        else -> "Enum"
    }
    val className = "${classNamePrefix}Attribute${if (request.isShared) "Shared" else ""}"

    variable(Var(request.delegatePropertyName, "Attribute<${request.type}>"))
    defineIs(StringBuilder {
        functionCallConsts(className, request.options)
    })
    emptyLine()
}

fun <O : Appendable> O.facadeProperty(attribute : AttributeInfo) {
    variable(Var(attribute.facadePropertyName, "PropertyMetadata"))
    defineIs(StringBuilder {
        functionCall("PropertyMetadataImpl", listOf(attribute.fieldName.quote()))
    })
}

fun <O : Appendable> O.facade(facade : AttributeFacade) {
    clazz(Clazz(facade.name.capitalize() + "Facade", isTrait = true, parents = listOf("Tag"))) {
        facade.attributes.forEach { attributeName ->
            if (attributeName.toLowerCase() == attributeName || attributeName.toLowerCase() !in Repository.attributes) {
                val attribute = Repository.attributes[attributeName]
                val request = tagAttributeVar(attribute)
                append("\n    ")
                getter() defineIs(StringBuilder {
                    append(request.delegatePropertyName)
                    append(".")
                    functionCall("get", listOf("this", attribute.facadePropertyName))
                })
                append("    ")
                setter {
                    append(request.delegatePropertyName)
                    append(".")
                    functionCall("set", listOf("this", attribute.facadePropertyName, "newValue"))
                }

                emptyLine()
            }
        }
    }
}