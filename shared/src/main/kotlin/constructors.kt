package html4k.impl

import html4k.*

// TODO to be generated

fun buildA(initialAttributes : Map<String, String>, consumer : TagConsumer<*>, block : A.() -> Unit) : Unit = A(initialAttributes, consumer).visit(block)
fun buildDIV(initialAttributes : Map<String, String>, consumer : TagConsumer<*>, block : DIV.() -> Unit) : Unit = DIV(initialAttributes, consumer).visit(block)
fun buildHTML(initialAttributes : Map<String, String>, consumer : TagConsumer<*>, block : HTML.() -> Unit) : Unit = HTML(consumer).visit(block)
fun buildBODY(initialAttributes : Map<String, String>, consumer : TagConsumer<*>, block : BODY.() -> Unit) : Unit = BODY(consumer).visit(block)
fun buildHEAD(initialAttributes : Map<String, String>, consumer : TagConsumer<*>, block : HEAD.() -> Unit) : Unit = HEAD(consumer).visit(block)