package myjvm

import html4k.*
import html4k.consumers.PredicateResult
import html4k.consumers.delayed
import html4k.consumers.filter
import html4k.consumers.measureTime
import html4k.dom.*
import html4k.stream.*
import org.w3c.dom.Element
import java.io.PrintStream
import kotlin.dom.first

fun main(args : Array<String>) {
	val document = document {
		val html = createHTMLTree().html {
			body {
				div {
					a("http://kotlinlang.org") {
						target = ATarget.blank
						+"me here"
					}
				}
			}
		}
		appendChild(html)
	}

	System.out.println(document.serialize())

	println(createHTMLDocument().html {
		body {
			div {
				+"test"
			}
		}
	}.serialize())

	println(document {
		appendChild(
			createHTMLTree().filter { if (it.tagName == "div") SKIP else PASS  }.html {
				body {
					div {
						a { +"link1" }
					}
					a { +"link2" }
					table {
						tbody {
							tr {
								td {
                                    +"cell"
								}
							}
						}
					}

					p {
						p {
							map {
								classes += "a"
                                asFlowContent.a ("") {
                                }
                            }
						}
					}
                    input(InputType.color) {

                    }
                    button(type = ButtonType.submit) {
                    }
				}
			}
		)
	}.serialize())

	document.getElementsByTagName("div").item(0).append {
		p { +"para1" }
		p { +"para2" }
		for (i in 3..10) {
			p { +"para$i"}
		}
		Unit
	}

	System.out.println(document.serialize())
}
