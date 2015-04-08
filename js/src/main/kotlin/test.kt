package test

import html4k.*
import html4k.dom.*
import html4k.stream.appendHTML
import java.util.HashSet
import java.util.LinkedHashSet
import java.util.TreeSet
import kotlin.js.dom.html.HTMLElement
import kotlin.js.dom.html.document
import kotlin.js.dom.html.window

fun onReady() {
    val div = document.createTree().div {
        div(setOf("bold")) {
            div {
                a("http://kotlinlang.org") {
                    target = ATarget.blank
                    classes += "zz"
                    +"Kotlin site"
                    div {
                    }
                }
                p {
                    +"content"
                }
            }
        }
    }

    document.body.appendChild(div)

    window.setInterval({
        div.append {
            div {
                +"added it"
            }
            div {
            }

            Unit
        }
    }, 10000L)
}

fun main(args: Array<String>) {
    window.onload = ::onReady
}
