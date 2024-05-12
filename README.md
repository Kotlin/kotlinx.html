[![Kotlin Stable](https://kotl.in/badges/stable.svg)](https://kotlinlang.org/docs/components-stability.html)
[![Official JetBrains Project](https://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-green.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.21-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![TeamCity (simple build status)](https://teamcity.jetbrains.com/app/rest/builds/aggregated/strob:\(branch:\(buildType:\(id:KotlinTools_KotlinxHtml_BuildGradleMasterBranch\),policy:active_history_and_active_vcs_branches\),locator:\(buildType:\(id:KotlinTools_KotlinxHtml_BuildGradleMasterBranch\)\)\)/statusIcon.svg)](https://teamcity.jetbrains.com/viewType.html?buildTypeId=KotlinTools_KotlinxHtml_BuildGradleMasterBranch&branch_Kotlin_KotlinX=%3Cdefault%3E&tab=buildTypeStatusDiv&guest=1)

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

## Using kotlinx_html to create a website with http4k
Add required dependencies 
```kotlin
dependencies {
    testImplementation(kotlin("test"))
    // http4k dependency 
    implementation(platform("org.http4k:http4k-bom:5.19.0.0"))
    implementation("org.http4k:http4k-core")
    implementation("org.http4k:http4k-server-undertow")
    implementation("org.http4k:http4k-client-apache")

    //kotlinx.html dependency
    val kotlinx_html_version = "latest_version"
    // include for JVM target
    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:${kotlinx_html_version}")

    // include for JS target
    implementation("org.jetbrains.kotlinx:kotlinx-html-js:${kotlinx_html_version}")

    // include for Common module
    implementation("org.jetbrains.kotlinx:kotlinx-html:${kotlinx_html_version}")
}
```

```kotlin
import kotlinx.html.html
import kotlinx.html.body
import kotlinx.html.head
import kotlinx.html.title
import kotlinx.html.div
import kotlinx.html.a
import kotlinx.html.ATarget
import kotlinx.html.stream.createHTML
import org.http4k.core.Request
import org.http4k.core.Response
import org.http4k.core.Status
import org.http4k.server.SunHttp
import org.http4k.server.asServer


fun main() {
    // Create a page
    val page = createHTML().html {
        head {
            title {
                +"Web Page"
            }
        }
        body {
            div() {
                a("https://kotlinlang.org") {
                    target = ATarget.blank
                    +"Main site"
                }
            }
        }
    }
    // Create a server
    val app = { _: Request -> Response(Status.OK).body(page) }
    // Start the server
    val server = app.asServer(SunHttp(9000)).start()
    // Stop the server
    server.stop()
}
```

# Documentation

See [wiki](https://github.com/kotlin/kotlinx.html/wiki) pages

# Building

See [development](https://github.com/kotlin/kotlinx.html/wiki/Development) page for details.

