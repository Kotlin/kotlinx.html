/*
 * Generated from pom.kts on 2019-06-05 at 15:54:24.068629
 */
project("kotlinx.html JS") {

    parent(
        "org.jetbrains.kotlinx",
        "kotlinx-html",
        "0.6.13-SNAPSHOT",
        "../pom.kts"
    )

    id("org.jetbrains.kotlinx:kotlinx-html-js:0.6.13-SNAPSHOT:jar")

    description("kotlinx.html JS implementation")
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
        dependency("org.jetbrains.kotlin:kotlin-stdlib-js")
        dependency("org.jetbrains.kotlinx:kotlinx-html-common:\${project.version}")
        optional("org.jetbrains.kotlin:kotlin-test-js:\${kotlin.version}")
    }

    build {
        plugins {
            plugin("org.jetbrains.kotlin:kotlin-maven-plugin:\${kotlin.version}") {
                executions {
                    execution("compile") {
                        phase("compile")
                        goals("js")
                        configuration {
                            "moduleKind" to "umd"
                            "sourceMap" to true
                            "sourceMapEmbedSources" to "always"
                            "sourceMapPrefix" to "./"
                            "outputFile" to "\${project.build.outputDirectory}/\${project.artifactId}.js"
                            "multiPlatform" to true
                        }
                    }
                    execution("compile-js-tests") {
                        phase("test-compile")
                        goals("test-js")
                        configuration {
                            "outputFile" to "\${project.basedir}/target/test-js/\${project.artifactId}-tests.js"
                            "metaInfo" to true
                            "sourceMap" to true
                            "moduleKind" to "umd"
                            "multiPlatform" to true
                        }
                    }
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
            plugin("org.apache.maven.plugins:maven-jar-plugin:2.6") {
                configuration {
                    "forceCreation" to true
                    "classesDirectory" to "\${project.build.outputDirectory}"
                    "includes" {
                        "include" to "**/*.js"
                        "include" to "**/*.js.map"
                        "include" to "**/*.kjsm"
                        "include" to "**/*.kotlin_classes"
                        "include" to "**/*.kotlin_string_table"
                        "include" to "**/*.kotlin_file_table"
                    }
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
            plugin("org.apache.maven.plugins:maven-source-plugin") {
                executions {
                    execution("attach-sources") {
                        goals("jar")
                    }
                }
            }
            plugin("org.jetbrains.dokka:dokka-maven-plugin")
            plugin("org.apache.maven.plugins:maven-dependency-plugin:2.10") {
                executions {
                    execution {
                        phase("test-compile")
                        goals("unpack-dependencies")
                        configuration {
                            "includeScope" to "test"
                            "excludeArtifactIds" to "kotlinx-html-common"
                            "includeTypes" to "jar"
                            "outputDirectory" to "\${project.basedir}/target/test-js"
                            "includes" to "*.js"
                            "excludes" to "*.meta.js"
                        }
                    }
                }
            }
            plugin("com.github.eirslett:frontend-maven-plugin:1.6") {
                executions {
                    execution("install node and npm") {
                        phase("generate-test-resources")
                        goals("install-node-and-npm")
                        configuration {
                            "skip" to "\${maven.test.skip}"
                        }
                    }
                    execution("npm install") {
                        phase("generate-test-resources")
                        goals("npm")
                        configuration {
                            "arguments" to "install"
                            "skip" to "\${maven.test.skip}"
                        }
                    }
                    execution("javascript tests") {
                        goals("karma")
                        configuration {
                            "karmaConfPath" to "\${project.basedir}/src/test/karma/karma.conf.js"
                            "skip" to "\${maven.test.skip}"
                            "skipTests" to "\${maven.test.skip}"
                        }
                    }
                }
                configuration {
                    "nodeVersion" to "v8.9.4"
                    "npmVersion" to "5.6.0"
                    "workingDirectory" to "\${project.basedir}/src/test/karma"
                }
            }
        }
    }
}
