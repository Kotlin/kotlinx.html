# kotlinx.html [ ![Download](https://api.bintray.com/packages/kotlinx/kotlinx/kotlinx.html/images/download.svg) ](https://bintray.com/kotlinx/kotlinx/kotlinx.html/_latestVersion) [ ![status](http://teamcity.jetbrains.com/app/rest/builds/buildType:(id:Kotlin_KotlinX_Html4k)/statusIcon)](https://teamcity.jetbrains.com/viewType.html?buildTypeId=Kotlin_KotlinX_Html4k&branch_Kotlin_KotlinX=%3Cdefault%3E&tab=buildTypeStatusDiv&guest=1)

A kotlinx.html library provides DSL to build HTML to [Writer](http://docs.oracle.com/javase/8/docs/api/java/io/Writer.html)/[Appendable](http://docs.oracle.com/javase/8/docs/api/java/lang/Appendable.html) or DOM at JVM and browser (or other JavaScript engine) for 
better [Kotlin programming](http://kotlinlang.org) for Web. 

# Get started

See [Getting started](https://github.com/kotlinx/kotlinx.html/wiki/0.-Getting-started) page for details how to include the library

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

Same for StringBuilder
```kotlin
StringBuilder {
    appendln("<!DOCTYPE html>")
    appendHTML().html {
        body {
            a("http://kotlinlang.org") { +"link" }
        }
    }
}
```

# Interceptors
You can define interceptors chains that could transform HTML during building or make some observations.
There is a default "filter interceptor", so you can filter out some elements. 

Below is an example that filters HTML that way so all div will be omited but content will remain

```kotlin
println(document {
		appendChild(
			createTree().filter { if (it.name == "div") SKIP else PASS  }.html {
				body {
					div {
						a { +"link1" }
					}
					a { +"link2" }
				}
			}
		)
	}.serialize())
```

The code above will produce the following output

```html
<!DOCTYPE html>
<html>
  <body>
    <a>link1</a><a>link2</a>
  </body>
</html>
```
The other interceptor doesn't mutate HTML but measures generation time

```kotlin
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
	println()
	println("Generated in ${it.time} ms")
}
```

# Building and development

Once you open a project in the IDE you have to select a Maven profile (in Maven tab): it should be kotlin-js or kotlin-jvm, you shouldn't enable both otherwise IDEA inspections will not work.

You can build the project only using Maven. If you run Maven via the IDEA runner please ensure you have -Pkotlin-js,kotlin-jvm in the command line

You have to install shared module before build other dependant submodules. Don't forget to reinstall shared once you have changed something in shared

You can build all by maven by command line

```bash
mvn clean package
```

# Old version

See https://github.com/kotlinx/kotlinx.html.legacy for older version. We are strongly recommend to migrate to latest version.
