# DSL markers example

Please note that both kotllin and kotlinx.html versions are nightly builds so you should use them with care. 
You also need corresponding kotlin IDE plugin.

build.gradle
```groovy
group 'org.jetbrains.kotlin.examples'
version '1.0-SNAPSHOT'

buildscript {
    ext.kotlin_version = '1.1.0-dev-4721'

    repositories {
        mavenCentral()
        maven {
            url = "https://dl.bintray.com/kotlin/kotlin-dev"
        }
    }
    dependencies {
        classpath "org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlin_version"
    }
}

apply plugin: 'kotlin'

repositories {
    mavenCentral()
    maven {
        url = "https://dl.bintray.com/kotlin/kotlin-dev"
    }
}

dependencies {
    compile "org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version"
    compile 'org.jetbrains.kotlinx:kotlinx.html.jvm:latest-1.1.0-dev-4717-94'
}
```

main.kt
```kotlin
package example

import kotlinx.html.*
import kotlinx.html.stream.*

fun main(args: Array<String>) {
    System.out.appendHTML().html {
        head {
            title("Hello")
        }
        body {
            h1 {
                +"Title"
//                h1 { // can't be here
//                }
            }
        }
    }
}
```
