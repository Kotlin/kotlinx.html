# kotlinx.html [ ![Download](https://api.bintray.com/packages/kotlinx/kotlinx/kotlinx.html/images/download.svg) ](https://bintray.com/kotlinx/kotlinx/kotlinx.html/_latestVersion) [![TeamCity (simple build status)](https://img.shields.io/teamcity/http/teamcity.jetbrains.com/s/KotlinTools_KotlinxHtml_Snapshot.svg)](https://teamcity.jetbrains.com/viewType.html?buildTypeId=KotlinTools_KotlinxHtml_Snapshot&branch_Kotlin_KotlinX=%3Cdefault%3E&tab=buildTypeStatusDiv&guest=1)

A kotlinx.html library provides DSL to build HTML to [Writer](http://docs.oracle.com/javase/8/docs/api/java/io/Writer.html)/[Appendable](http://docs.oracle.com/javase/8/docs/api/java/lang/Appendable.html) or DOM at JVM and browser (or other JavaScript engine) for 
better [Kotlin programming](http://kotlinlang.org) for Web. 

# Get started

See [Getting started](https://github.com/kotlinx/kotlinx.html/wiki/Getting-started) page for details how to include the library

# DOM
You can build DOM tree with JVM and JS naturally

See example for JavaScript-targeted Kotlin

```kotlin
window.setInterval({
    val myDiv = document.create.div("panel") {
        p { 
            +"Here is "
            a("http://kotlinlang.org") { +"official Kotlin site" } 
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
You can build HTML directly to Writer (JVM only) or Appendable (both JVM and JS)

```kotlin
System.out.appendHTML().html {
	body {
		div {
			a("http://kotlinlang.org") {
				target = ATarget.blank
				+"Main site"
			}
		}
	}
}
```

# Documentation

See [wiki](https://github.com/kotlinx/kotlinx.html/wiki) pages

# Building 
See [development](https://github.com/kotlinx/kotlinx.html/wiki/Development) page for details

# Old version

See https://github.com/kotlinx/kotlinx.html.legacy for older version. We strongly recommend to migrate to latest version.
