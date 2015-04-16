# html4k
This is just temporary workspace to HTML builders
Probably need to be imported to kotlinx.html

Provides DSL to build HTML to Writer/Appendable or DOM at JVM and JavaScript


# DOM
You can build DOM tree at JVM and JS naturally
See example at JavaScript

```kotlin
window.setInterval({
    val myDiv = document.create.div {
        classes += "panel"
        
        p { 
            +"Here is "
            a("http://kotlinlang.org") { +"official Kotlin site" } 
        }
    }

    document.getElementById("container").appendChild(myDiv)

    document.getElementById("container").append {
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
}.println()
```

Same for StringBuilder
```kotlin
StringBuilder {
    appendHTML().html {
        body {
            a("http://kotlinlang.org") { +"link" }
        }
    }
}
```

# Interceptors
You can define interceptors chain that could transform HTML during building or make some observations.
There is default "filter interceptor so you can filter out some elements
Here is example that filters HTML that way so all div will be omited but content will remain

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
	it.first.println()
	it.first.println("Generated in ${it.second} ms")
}
```

# Building

Once you open project in IDE you have to select Maven profile (in Maven tab): it should be kotlin-js or kotlin-jvm, you shouldn't enable both otherwise IDEA inspections will not work.

You can build project by Maven only, you can't use IDEA's compile facilities. If you run Maven via IDEA runner please ensure you have -Pkotlin-js,kotlin-jvm in the command line

You have to install shared module before build other dependant submodules. Don't forget to reinstall shared once you have changed something in shared
