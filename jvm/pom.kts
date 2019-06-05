/*
 * Generated from pom.kts on 2019-06-05 at 15:54:24.065027
 */
project("kotlinx.html JVM") {

    parent(
        "org.jetbrains.kotlinx",
        "kotlinx-html",
        "0.6.13-SNAPSHOT",
        "../pom.kts"
    )

    id("org.jetbrains.kotlinx:kotlinx-html-jvm:0.6.13-SNAPSHOT:jar")

    description("kotlinx.html JVM implementation")
    url("https://github.com/Kotlin/kotlinx.html")

    licenses {
        license("The Apache License, Version 2.0") {
            url("https://www.apache.org/licenses/LICENSE-2.0.txt")
        }
    }

    developers {
        developer("Sergey Mashkov") {
            organization("JetBrains s.r.o.")
            roles("developer")
        }
    }

    scm {
        url("https://github.com/Kotlin/kotlinx.html")
        connection("scm:git:git@github.com:Kotlin/kotlinx.html.git")
    }

    dependencies {
        test("junit:junit")
        dependency("org.jetbrains.kotlin:kotlin-stdlib")
        test("org.jetbrains.kotlin:kotlin-test-junit")
        optional("org.jetbrains.kotlinx:kotlinx-html-common:\${project.version}") {
            exclusions("org.jetbrains.kotlin:kotlin-stdlib-common")
        }
    }

    build {
        plugins {
            plugin("org.jetbrains.kotlin:kotlin-maven-plugin") {
                executions {
                    execution("compile") {
                        phase("compile")
                        goals("compile")
                        configuration {
                            "args" {
                                "arg" to "-Xdump-declarations-to=\${build.directory}/declarations.json"
                            }
                        }
                    }
                    execution("test-compile") {
                        phase("test-compile")
                        goals("test-compile")
                    }
                }
                configuration {
                    "multiPlatform" to true
                }
            }
            plugin("org.codehaus.mojo:build-helper-maven-plugin:3.0.0") {
                executions {
                    execution("add-source") {
                        phase("prepare-package")
                        goals("add-source")
                        configuration {
                            "sources" {
                                "source" to "../shared/src/main/kotlin"
                            }
                        }
                    }
                }
            }
            plugin("org.apache.maven.plugins:maven-source-plugin:2.4") {
                executions {
                    execution("attach-sources") {
                        phase("prepare-package")
                        goals("jar-no-fork", "test-jar-no-fork")
                    }
                }
            }
            plugin("org.jetbrains.dokka:dokka-maven-plugin")
        }
    }
}
