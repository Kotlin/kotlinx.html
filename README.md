[![Kotlin Stable](https://kotl.in/badges/stable.svg)](https://kotlinlang.org/docs/components-stability.html)
[![Official JetBrains Project](https://jb.gg/badges/official.svg)](https://confluence.jetbrains.com/display/ALL/JetBrains+on+GitHub)
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-green.svg?style=flat)](https://www.apache.org/licenses/LICENSE-2.0)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.10-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![TeamCity (simple build status)](https://teamcity.jetbrains.com/app/rest/builds/aggregated/strob:\(branch:\(buildType:\(id:KotlinTools_KotlinxHtml_BuildGradleMasterBranch\),policy:active_history_and_active_vcs_branches\),locator:\(buildType:\(id:KotlinTools_KotlinxHtml_BuildGradleMasterBranch\)\)\)/statusIcon.svg)](https://teamcity.jetbrains.com/viewType.html?buildTypeId=KotlinTools_KotlinxHtml_BuildGradleMasterBranch&branch_Kotlin_KotlinX=%3Cdefault%3E&tab=buildTypeStatusDiv&guest=1)

# kotlinx.html

A kotlinx.html library provides DSL to build HTML to [Writer](https://docs.oracle.com/javase/8/docs/api/java/io/Writer.html)/[Appendable](https://docs.oracle.com/javase/8/docs/api/java/lang/Appendable.html) or DOM. Available to all Kotlin Multiplatform targets and browser(or other JavaScript engine) for better [Kotlin programming](https://kotlinlang.org) for Web.

# Get started

See [Getting started](https://github.com/kotlin/kotlinx.html/wiki/Getting-started) page for details how to include the library.

# DOM
You can build DOM tree with JVM and JS naturally

See example for JavaScript-targeted Kotlin

```kotlin
window.setInterval({
    val myDiv = document.create.div("panel") {
        p { 
            +"Here is "
            a("https://kotlinlang.org") { +"official Kotlin site" }
        }
    }

    document.getElementById("container")!!.appendChild(myDiv)

    document.getElementById("container")!!.append {
        div {
            +"added it"
        }
    }
}, 1000L)
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
See [development](https://github.com/kotlin/kotlinx.html/wiki/Development) page for details.

