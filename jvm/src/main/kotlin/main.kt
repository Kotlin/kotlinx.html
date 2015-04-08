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
				div("block deprecated") {
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

	System.out.appendHTML().filter { if (it.tagName == "div" && it.attributes["class"]?.contains("deprecated") ?: false) SKIP else PASS }.buildMe().append("\n")

	System.out.appendHTML().html {
		body {
			td {}
			form("/someurl") {
				input {
					type = InputType.checkBox
					attributes["name"] = "cb1"
					disabled = true
					+"var1"
				}

				buildMe2()

				input {
					type = InputType.submit
					+"Go"
				}
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
				CDATA("Here is my content")
			}
		}
	}.let {
		it.first.println()
		it.first.println("Generated in ${it.second} ms")
	}

	val document = document {
		val html = buildHTML().html {
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

//	document {
//
//		this@document.buildAndAppendChild {
//			div {
//			}
//		}
//	}

	println(document {
		appendChild(
			buildHTML().filter { if (it.tagName == "div") SKIP else PASS  }.html {
				body {
					div {
						a { +"link1" }
					}
					a { +"link2" }
				}
			}
		)
	}.serialize())

	document.getElementsByTagName("div").item(0).buildAndAppendChild {
		div {
			+"aaa"
		}
	}

	System.out.println(document.serialize())
}
