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

fun <T> TagConsumer<T>.buildMe() = html { buildMe2() }
fun HTMLTag.buildMe2() =
				div(setOf("block", "deprecated")) {
					a(href = "http://kotlinlang.org") {
						target = ATarget.blank
						attributes["custom"] = "custom"
						+"test me"
					}
				}

fun main(args : Array<String>) {
	System.out.appendHTML().buildMe().println()
	System.out.appendHTML().filter { if (it.tagName == "div") SKIP else PASS }.buildMe().println()
	System.out.appendHTML().filter { if (it.tagName == "div") DROP else PASS }.buildMe().println()

	println("deprecated:")
	System.out.appendHTML().filter { if (it.tagName == "div" && "deprecated" in (it as DIV).classes) SKIP else PASS }.buildMe().append("\n")

	System.out.appendHTML().html {
		head {
			title("test")
			link { rel = LinkRel.stylesheet; href = "/styles.css" }
		}
		body {
			td {}
			form("/someurl") {
				checkBoxInput("cb1") {
					+"var1"
				}
				input(InputType.checkBox, "cb2") {
					disabled = true
					+"var2"
				}

				buildMe2()

				submitInput(content = "Go!")
			}
		}
	}.println()

	System.out.appendHTML().measureTime().html {
		head {
			title("Welcome page")
		}
		body {
			div {
				+"<<<special chars & entities goes here>>>"
			}
			div {
			}
		}
	}.let {
		it.first.println()
		it.first.println("Generated in ${it.second} ms")
	}

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
