/*
 * Generated from pom.kts on 2019-06-05 at 15:54:24.062670
 */
project(":kotlinx-html-common::jar") {

    parent(
        "org.jetbrains.kotlinx",
        "kotlinx-html",
        "0.6.13-SNAPSHOT",
        "../pom.kts"
    )

    dependencies {
        dependency("org.jetbrains.kotlin:kotlin-stdlib-common:\${kotlin.version}")
    }

    build {
        plugins {
            plugin("org.apache.maven.plugins:maven-resources-plugin:3.0.2")
            plugin("org.jetbrains.kotlin:kotlin-maven-plugin") {
                executions {
                    execution("metadata") {
                        goals("metadata")
                    }
                }
            }
            plugin("org.jetbrains.dokka:dokka-maven-plugin")
            plugin("org.apache.maven.plugins:maven-jar-plugin:2.6") {
                executions {
                    execution("default-jar") {
                        phase("none")
                    }
                    execution("pack-meta") {
                        phase("package")
                        goals("jar")
                        configuration {
                            "classesDirectory" to "\${project.build.outputDirectory}"
                        }
                    }
                }
                configuration {
                    "forceCreation" to true
                    "archive" {
                        "forced" {
                        }
                        "manifestEntries" {
                            "Built-By" to "\${user.name}"
                            "Implementation-Vendor" to "JetBrains s.r.o."
                            "Implementation-Version" to "\${project.version}"
                        }
                    }
                }
            }
        }
    }
}
