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