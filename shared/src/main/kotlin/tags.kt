package html4k

// TODO to be generated

class HTML(observer : TagConsumer<*>) : HTMLTag("html", observer) {
    override
    fun body(block : BODY.() -> Unit) = super.body(block)

    override
    fun head(block: HEAD.() -> Unit) = super.head(block)
}
class BODY(observer : TagConsumer<*>) : HTMLTag("body", observer) {
    override
    fun div(classes : List<String>, block : DIV.() -> Unit) = super.div(classes, block)
}
class HEAD(observer : TagConsumer<*>) : HTMLTag("head", observer) {
    fun title(title : String) {
        TITLE(observer).visit { observer.onTagContent(title) } // TODO ??? how could it be unified? contentTag?
    }
}
class TITLE(observer : TagConsumer<*>) : HTMLTag("title", observer)
class A(initialAttributes : Map<String, String>, observer : TagConsumer<*>) : HTMLTag("a", observer, initialAttributes) {
    var href : String by StringAttribute()
    var target : Targets by EnumAttribute(targetsValues)
}

class DIV(initialAttributes : Map<String, String>, observer : TagConsumer<*>) : HTMLTag("div", observer, initialAttributes) {
    override
    fun a(href: String?, block: A.() -> Unit) = super.a(href, block)

    override
    fun div(classes: List<String>, block: DIV.() -> Unit) = super.div(classes, block)
}

