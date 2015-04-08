package html4k.generate

import java.util.ArrayList

fun String.quote() = "\"$this\""

fun <O : Appendable> O.attributePseudoDelegate(request : AttributeRequest) {
    val classNamePrefix = request.type.classPrefix
    val className = "${classNamePrefix}Attribute"

    append("private ")
    variable(Var(request.delegatePropertyName, "Attribute<${request.typeName}>"))
    defineIs(StringBuilder {
        functionCallConsts(className, request.options)
    })
    emptyLine()
}

fun <O : Appendable> O.attributeProperty(attributeName : String) {
    val attribute = Repository.attributes[attributeName]
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

fun <O : Appendable> O.facade(facade : AttributeFacade) {
    clazz(Clazz(facade.name.capitalize() + "Facade", isTrait = true, parents = listOf("Tag"))) {
        facade.attributes.forEach { attributeName ->
            if (attributeName.toLowerCase() == attributeName || attributeName.toLowerCase() !in Repository.attributes) {
                attributeProperty(attributeName)
            }
        }
    }
}