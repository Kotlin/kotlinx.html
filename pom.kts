/*
 * Generated from pom.kts on 2019-06-05 at 15:54:24.004835
 */
project("kotlinx.html root module") {

    id("org.jetbrains.kotlinx:kotlinx-html:0.6.13-SNAPSHOT:pom")

    description("A root module for all kotlinx.html components")
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

    modules("shared", "jvm", "js", "generate", "assembly")

    scm {
        url("https://github.com/Kotlin/kotlinx.html")
        connection("scm:git:git@github.com:Kotlin/kotlinx.html.git")
    }

    distributionManagement {
        repository("bintray") {
            url("\${bintray.url}")
        }
    }

    properties {
        "project.build.sourceEncoding" to "UTF-8"
    }

    dependencyManagement {
        dependencies {
            dependency("org.jetbrains.kotlin:kotlin-stdlib:\${kotlin.version}")
            dependency("org.jetbrains.kotlin:kotlin-stdlib-common:\${kotlin.version}")
            dependency("org.jetbrains.kotlin:kotlin-stdlib-js:\${kotlin.version}")
            dependency("org.jetbrains.kotlin:kotlin-test-junit:\${kotlin.version}")
            test("junit:junit:4.12")
        }
    }

    dependencies {
        dependency("io.takari.polyglot:polyglot-kotlin:0.4.1")
        dependency("org.apache.maven:maven-model:3.6.1")
        dependency("org.apache.maven:maven-core:3.6.1")
    }

    pluginRepositories {
        pluginRepository("JCenter") {
            id("jcenter")
            url("https://jcenter.bintray.com/")
        }
    }

    build {
        sourceDirectory("\${project.basedir}/src/main/kotlin")
        testSourceDirectory("\${project.basedir}/src/test/kotlin")
        pluginManagement {
            plugins {
                plugin("org.jetbrains.kotlin:kotlin-maven-plugin:\${kotlin.version}")
                plugin("org.jetbrains.dokka:dokka-maven-plugin:0.9.1") {
                    executions {
                        execution {
                            phase("pre-site")
                            goals("dokka")
                        }
                    }
                    configuration {
                        "sourceLinks" {
                            "link" {
                                "dir" to "\${project.basedir}/src/main/kotlin"
                                "url" to "https://github.com/Kotlin/kotlinx.html/"
                            }
                        }
                    }
                }
            }
        }
        plugins {
            plugin("org.apache.maven.plugins:maven-source-plugin:3.0.1") {
                executions {
                    execution("attach-sources") {
                        phase("prepare-package")
                        goals("jar-no-fork", "test-jar-no-fork")
                        configuration {
                            "forceCreation" to true
                        }
                    }
                }
            }
            plugin("org.apache.maven.plugins:maven-release-plugin:2.5.3") {
                configuration {
                    "allowTimestampedSnapshots" to true
                }
            }
            plugin("org.apache.maven.plugins:maven-jar-plugin:3.0.2") {
                executions {
                    execution("empty-javadoc-jar") {
                        phase("prepare-package")
                        goals("jar")
                        configuration {
                            "classifier" to "javadoc"
                            "classesDirectory" to "\${build.dir}/empty"
                            "forceCreation" to true
                        }
                    }
                }
            }
            plugin("org.apache.maven.plugins:maven-surefire-plugin:2.18.1") {
                configuration {
                    "forkCount" to "2C"
                    "reuseForks" to true
                }
            }
            plugin("com.devexperts.bintray:bintray-maven-plugin:1.3") {
                executions {
                    execution("bintray-deploy") {
                        goals("publish")
                        configuration {
                            "id" to "bintray"
                            "url" to "\${bintray.url}"
                        }
                    }
                }
            }
        }
    }

    profiles {
        profile("kotlin-latest") {
            activation {
                activeByDefault(true)
            }
            properties {
                "bintray.url" to "https://api.bintray.com/maven/kotlin/kotlinx.html/kotlinx.html"
                "kotlin.version" to "1.2.71"
            }
        }
        profile("kotlin-snapshot") {
            activation {
            }
            properties {
                "bintray.url" to "https://api.bintray.com/maven/kotlin/kotlin-dev/kotlinx.html"
                "kotlin.version" to "1.2-SNAPSHOT"
            }
            repositories {
                repository("Sonatype OSS Snapshot Repository") {
                    id("sonatype.oss.snapshots")
                    url("https://oss.sonatype.org/content/repositories/snapshots")
                    releases {
                        enabled(false)
                    }
                    snapshots {
                    }
                }
            }
            pluginRepositories {
                pluginRepository("Sonatype OSS Snapshot Repository") {
                    id("sonatype.oss.snapshots")
                    url("https://oss.sonatype.org/content/repositories/snapshots")
                    releases {
                        enabled(false)
                    }
                    snapshots {
                    }
                }
            }
        }
        profile("kotlin-dev") {
            activation {
            }
            properties {
                "bintray.url" to "https://api.bintray.com/maven/kotlin/kotlin-dev/kotlinx.html"
                "kotlin.version" to "1.2.71"
            }
            repositories {
                repository("bintray") {
                    id("bintray-kotlin-kotlin-dev")
                    url("https://dl.bintray.com/kotlin/kotlin-dev")
                    snapshots {
                        enabled(false)
                    }
                }
                repository("bintray") {
                    id("bintray-kotlin-kotlin-eap")
                    url("https://dl.bintray.com/kotlin/kotlin-eap-1.1")
                    snapshots {
                        enabled(false)
                    }
                }
            }
            pluginRepositories {
                pluginRepository("bintray-plugins") {
                    id("bintray-kotlin-plugin-kotlin-dev")
                    url("https://dl.bintray.com/kotlin/kotlin-dev")
                    snapshots {
                        enabled(false)
                    }
                }
                pluginRepository("bintray-plugins") {
                    id("bintray-kotlin-plugin-kotlin-eap")
                    url("https://dl.bintray.com/kotlin/kotlin-eap-1.1")
                    snapshots {
                        enabled(false)
                    }
                }
            }
        }
    }
}
