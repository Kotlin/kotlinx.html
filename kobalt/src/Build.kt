import com.beust.kobalt.*
import com.beust.kobalt.plugin.packaging.*
import com.beust.kobalt.plugin.application.*
import com.beust.kobalt.plugin.kotlin.*

val repos = repos()

val projectBuildSourceEncoding = "UTF-8"

val shared = project {
    name = "shared"
    directory = "shared"
}

val generate = project {
    name = "generate"
    directory = "generate"

    dependencies {
        compile("com.sun.xsom:xsom:20140925")
    }
}

val jvm = project(shared) {

    name = "kotlinx.html-jvm"
    group = "org.jetbrains.kotlinx"
    artifactId = name
    version = "0.5.8-SNAPSHOT"
    directory = "jvm"

    dependencies {
    }

    dependenciesTest {
        compile("junit:junit:4.12")

    }

    assemble {
        jar {
        }
    }

    application {
        mainClass = "com.example.MainKt"
    }


}
