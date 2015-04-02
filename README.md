# html4k
This is just temporary workspace to HTML builders
Probably need to be imported to kotlinx.html

Provides DSL to build HTML to Writer/Appendable or DOM at JVM and JavaScript

# Stream
You can build HTML directly to Writer (JVM only) or Appendable (both JVM and JS)

```kotlin
System.out.appendHTML().html {
	body {
		div {
			a("http://kotlinlang.org") {
				target = Targets._blank
				+"Main site"
			}
		}
	}
}.println()
```

Same for StringBuilder
```kolin
StringBuilder {
    appendHTML().html {
        body {
            a("http://kotlinlang.org") { +"link" }
        }
    }
}
```

# DOM
You can build DOM tree at JVM and JS naturally
See example at JavaScript

```kotlin
window.setInterval({
    document.getElementById("container").buildAndAppendChild {
        div {
            +"added it"
        }
    }
}, 1000L)
```

# Interceptors
You can define interceptors chain that could transform HTML during building. 
There is default "filter interceptor so you can filter out some elements
Here is example that filters HTML that way so all div will be omited but content will remain

```kotlin
println(document {
		appendChild(
			buildHTML().filter { if (it.name == "div") SKIP else PASS  }.html {
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
