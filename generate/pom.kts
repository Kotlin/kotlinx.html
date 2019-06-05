/*
 * Generated from pom.kts on 2019-06-05 at 15:54:24.072535
 */
project(":generate::jar") {

    parent(
        "org.jetbrains.kotlinx",
        "kotlinx-html",
        "0.6.13-SNAPSHOT",
        "../pom.kts"
    )

    dependencies {
        dependency("org.jetbrains.kotlin:kotlin-stdlib")
        dependency("com.sun.xsom:xsom:20140925")
    }

    build {
        plugins {
            plugin("org.jetbrains.kotlin:kotlin-maven-plugin") {
                executions {
                    execution("compile") {
                        phase("compile")
                        goals("compile")
                    }
                    execution("test-compile") {
                        phase("test-compile")
                        goals("test-compile")
                    }
                    execution("activate-profile") {
                        phase("none")
                        goals("script")
                        configuration {
                            "scriptFile" to "../scripts/activate-profile.kts"
                        }
                    }
                }
            }
            plugin("org.apache.maven.plugins:maven-deploy-plugin:2.8.2") {
                executions {
                    execution {
                        phase("none")
                    }
                }
                configuration {
                    "skip" to true
                }
            }
        }
    }
}
