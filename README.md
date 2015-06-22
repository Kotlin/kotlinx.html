# KotlinX HTML
This is just temporary workspace to HTML builders
Probably need to be imported to kotlinx.html

A kotlinx.html library provides DSL to build HTML to Writer/Appendable or DOM at JVM and JavaScript

[ ![Download](https://api.bintray.com/packages/kotlinx/kotlinx/kotlinx.html/images/download.svg) ](https://bintray.com/kotlinx/kotlinx/kotlinx.html/_latestVersion)

Build status: [ ![status](http://teamcity.jetbrains.com/app/rest/builds/buildType:(id:Kotlin_KotlinX_Html4k)/statusIcon)](https://teamcity.jetbrains.com/viewType.html?buildTypeId=Kotlin_KotlinX_Html4k&branch_Kotlin_KotlinX=%3Cdefault%3E&tab=buildTypeStatusDiv&guest=1)

# Get started

There are three bundles available: 
- zip with two JVM jars
- jar with JavaScripts and meta-data (required for Kotlin compiler)
- webjar with JavaScripts (without meta-data)

you can grab them at [releases](https://github.com/cy6erGn0m/html4k/releases) tab and include to your project. Use first
for server-side and second for client-side

# Maven

To get it work with maven you need to add custom repository

```xml
		<repository>
            <id>bintray-kotlinx</id>
            <name>bintray</name>
            <url>http://dl.bintray.com/kotlinx/kotlinx</url>
        </repository>
```

For server-side development you can add the following dependency:

```xml
		<dependency>
            <groupId>org.jetbrains.kotlinx</groupId>
            <artifactId>kotlinx.html.jvm</artifactId>
            <version>${kotlinx.html.version}</version>
        </dependency>
```

For client-side (JavaScript) you need this one:

```xml
		<dependency>
            <groupId>org.jetbrains.kotlinx</groupId>
            <artifactId>kotlinx.html.js</artifactId>
            <version>${kotlinx.html.version}</version>
        </dependency>
```

If you are building web application with war plugin you can use overlays to pack JavaScripts from webjar like this:

```xml
			<plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>2.6</version>
                <configuration>
                    <dependentWarExcludes>META-INF/*,META-INF/**/*,*meta.js,**/*class</dependentWarExcludes>
                    <webXml>src/main/resources/web.xml</webXml>
                    <webResources>
                        <resource>
                            <directory>src/main/webapp</directory>
                        </resource>
                    </webResources>
                    <overlays>
                        <overlay>
                            <groupId>org.jetbrains.kotlin</groupId>
                            <artifactId>kotlin-js-library</artifactId>
                            <type>jar</type>
                            <includes>
                                <include>kotlin.js</include>
                            </includes>
                            <targetPath>js/</targetPath>
                        </overlay>
                        <overlay>
                            <groupId>org.jetbrains.kotlinx</groupId>
                            <artifactId>kotlinx.html.assembly</artifactId>
                            <classifier>webjar</classifier>
                            <type>jar</type>
                            <targetPath>js/</targetPath>
                        </overlay>
                    </overlays>
                </configuration>
            </plugin>
```

# Gradle

You have to add repository before:
```groovy
repositories {
    maven {
        url "http://dl.bintray.com/cy6ergn0m/maven"
    }
}

dependencies {
    // include for server side
	compile "org.jetbrains.kotlinx:kotlinx.html.jvm:${kotlinx.html.version}"
	
	// include for client-side
	compileClient "org.jetbrains.kotlinx:kotlinx.html.js:${kotlinx.html.version}"
}
```

# DOM
You can build DOM tree at JVM and JS naturally
See example at JavaScript

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
}.println()
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
