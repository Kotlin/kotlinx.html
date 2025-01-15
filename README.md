[![Kotlin Stable](https://kotl.in/badges/stable.svg)](https://kotlinlang.org/docs/components-stability.html)
[![Official JetBrains Project](https://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-green.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-2.1.0-blue.svg?logo=kotlin)](http://kotlinlang.org)

# kotlinx.html

The kotlinx.html library provides a DSL
to build HTML
to [Writer](https://docs.oracle.com/javase/8/docs/api/java/io/Writer.html)/[Appendable](https://docs.oracle.com/javase/8/docs/api/java/lang/Appendable.html)
or DOM.
Available to all Kotlin Multiplatform targets and browsers (or other WasmJS or JavaScript engines)
for better [Kotlin programming](https://kotlinlang.org) for Web.

# Get started

See [Getting started](https://github.com/kotlin/kotlinx.html/wiki/Getting-started) page for details how to include the
library.

# DOM

You can build a DOM tree with JVM, JS, and WASM.
The following example shows how to build the DOM for WasmJs-targeted Kotlin:

```kotlin
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.a
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.dom.create
import kotlinx.html.p

fun main() {
    val body = document.body ?: error("No body")
    body.append {
        div {
            p {
                +"Here is "
                a("https://kotlinlang.org") { +"official Kotlin site" }
            }
        }
    }

    val timeP = document.create.p {
        +"Time: 0"
    }

    body.append(timeP)

    var time = 0
    window.setInterval({
        time++
        timeP.textContent = "Time: $time"

        return@setInterval null
    }, 1000)
}
```

# Stream

You can build HTML directly to Writer (JVM) or Appendable (Multiplatform)

```kotlin
System.out.appendHTML().html {
    body {
        div {
            a("https://kotlinlang.org") {
                target = ATarget.blank
                +"Main site"
            }
        }
    }
}
```

# Documentation

See [wiki](https://github.com/kotlin/kotlinx.html/wiki) pages

# Building

See the [development](https://github.com/kotlin/kotlinx.html/wiki/Development) page for details.

