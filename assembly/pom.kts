/*
 * Generated from pom.kts on 2019-06-05 at 15:54:24.074327
 */
project(":kotlinx-html-assembly::jar") {

    parent(
        "org.jetbrains.kotlinx",
        "kotlinx-html",
        "0.6.13-SNAPSHOT",
        "../pom.kts"
    )

    dependencies {
        dependency("org.jetbrains.kotlinx:kotlinx-html-jvm:\${project.version}")
        dependency("org.jetbrains.kotlinx:kotlinx-html-js:\${project.version}")
    }

    build {
        plugins {
            plugin("org.apache.maven.plugins:maven-assembly-plugin:2.6") {
                executions {
                    execution("jvm-and-webjar") {
                        phase("package")
                        goals("single")
                        configuration {
                            "descriptors" {
                                "descriptor" to "src/main/assembly/jvm.xml"
                                "descriptor" to "src/main/assembly/webjar.xml"
                            }
                        }
                    }
                    execution("js-only") {
                        phase("package")
                        goals("single")
                        configuration {
                            "descriptor" to "src/main/assembly/js.xml"
                            "archive" {
                                "manifest" {
                                    "addDefaultImplementationEntries" to true
                                }
                                "manifestEntries" {
                                    "Kotlin-JS-Module-Name" to "kotlinx-html-js-assembly"
                                }
                            }
                        }
                    }
                }
                configuration {
                    "finalName" to "kotlinx-html-\${project.version}"
                }
            }
            plugin("org.apache.maven.plugins:maven-jar-plugin") {
                executions {
                    execution("default-jar") {
                        configuration {
                            "forceCreation" to true
                        }
                    }
                }
            }
        }
    }
}
