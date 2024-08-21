import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

/**
 * This build script supports the following parameters:
 * -PversionTag - works together with "branch-build" profile and overrides "-SNAPSHOT" suffix of the version.
 */
plugins {
    kotlin("multiplatform") version "2.0.0"
    id("maven-publish")
    id("signing")
    id("org.jetbrains.kotlinx.binary-compatibility-validator") version "0.13.2"
}

group = "org.jetbrains.kotlinx"
version = "0.11.0"

/**
 * If "release" profile is used the "-SNAPSHOT" suffix of the version is removed.
 */
if (hasProperty("release")) {
    val versionString = version as String
    if (versionString.endsWith("-SNAPSHOT")) {
        version = versionString.replace("-SNAPSHOT", "")
    }
}

/**
 * Handler of "versionTag" property.
 * Required to support Maven and NPM repositories that doesn't support "-SNAPSHOT" versions. To build and publish
 * artifacts with specific version run "./gradlew -PversionTag=my-tag" and the final version will be "0.6.13-my-tag".
 */
if (hasProperty("versionTag")) {
    val versionString = version as String
    val versionTag = properties["versionTag"]
    if (versionString.endsWith("-SNAPSHOT")) {
        version = versionString.replace("-SNAPSHOT", "-$versionTag")
        logger.lifecycle("Project will be built with version '$version'.")
    } else {
        error("Could not apply 'versionTag' together with non-snapshot version.")
    }
}

if (hasProperty("releaseVersion")) {
    version = properties["releaseVersion"] as String
}

val publishingUser = System.getenv("PUBLISHING_USER")
val publishingPassword = System.getenv("PUBLISHING_PASSWORD")
val publishingUrl = System.getenv("PUBLISHING_URL")

publishing {
    publications {
        repositories {
            if (publishingUser == null) return@repositories
            maven {
                url = uri(publishingUrl)
                credentials {
                    username = publishingUser
                    password = publishingPassword
                }
            }
        }
    }
}

repositories {
    mavenCentral()
}

val emptyJar = tasks.register<org.gradle.jvm.tasks.Jar>("emptyJar") {
    archiveAppendix.set("empty")
}

kotlin {
    jvm {
        mavenPublication {
            groupId = group as String
            pom { name = "${project.name}-jvm" }

            artifact(emptyJar) {
                classifier = "javadoc"
            }
        }
    }
    js {
        moduleName = project.name
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
        }

        mavenPublication {
            groupId = group as String
            pom { name = "${project.name}-js" }
        }
    }
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = project.name
        browser()

        mavenPublication {
            groupId = group as String
            pom { name = "${project.name}-wasm-js" }
        }
    }

    mingwX64()
    linuxX64()
    linuxArm64()
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    watchosX64()
    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()
    watchosDeviceArm64()
    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()
    macosX64()
    macosArm64()
    androidNativeArm32()
    androidNativeArm64()
    androidNativeX86()
    androidNativeX64()

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    applyDefaultHierarchyTemplate {
        common {
            group("jsCommon") {
                withJs()
                withWasmJs()
            }
        }
    }

    metadata {
        mavenPublication {
            groupId = group as String
            artifactId = "${project.name}-common"
            pom {
                name = "${project.name}-common"
            }
        }
    }
}

kotlin {
    jvmToolchain(8)

    sourceSets {
        commonTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

tasks.withType<Jar>().configureEach {
    manifest {
        attributes += sortedMapOf(
            "Built-By" to System.getProperty("user.name"),
            "Build-Jdk" to System.getProperty("java.version"),
            "Implementation-Vendor" to "JetBrains s.r.o.",
            "Implementation-Version" to archiveVersion.get(),
            "Created-By" to GradleVersion.current()
        )
    }
}

tasks.register<Task>("generate") {
    group = "source-generation"
    description = "Generate tag-handling code using tags description."

    doLast {
        kotlinx.html.generate.generate(
            pkg = "kotlinx.html",
            todir = "src/commonMain/kotlin/generated",
            jsdir = "src/jsMain/kotlin/generated",
            wasmJsDir = "src/wasmJsMain/kotlin/generated"
        )
        kotlinx.html.generate.generateJsTagTests(
            jsdir = "src/jsTest/kotlin/generated",
            wasmJsDir = "src/wasmJsTest/kotlin/generated",
        )
    }
}

publishing {
    publications {
        configureEach {
            if (this is MavenPublication) {
                pom.config()
            }
        }
    }
}

typealias MavenPomFile = MavenPom

fun MavenPomFile.config(config: MavenPomFile.() -> Unit = {}) {
    config()

    url = "https://github.com/Kotlin/kotlinx.html"
    name = "kotlinx.html"
    description = "A kotlinx.html library provides DSL to build HTML to Writer/Appendable or DOM at JVM and browser (or other JavaScript engine) for better Kotlin programming for Web."

    licenses {
        license {
            name = "The Apache License, Version 2.0"
            url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
        }
    }

    scm {
        connection = "scm:git:git@github.com:Kotlin/kotlinx.html.git"
        url = "https://github.com/Kotlin/kotlinx.html"
        tag = "HEAD"
    }

    developers {
        developer {
            name = "Sergey Mashkov"
            organization = "JetBrains s.r.o."
            roles to "developer"
        }

        developer {
            name = "Anton Dmitriev"
            organization = "JetBrains s.r.o."
            roles to "developer"
        }
    }
}

tasks.withType<GenerateModuleMetadata>().configureEach {
    enabled = true
}

val signingKey = System.getenv("SIGN_KEY_ID")
val signingKeyPassphrase = System.getenv("SIGN_KEY_PASSPHRASE")

if (!signingKey.isNullOrBlank()) {
    project.ext["signing.gnupg.keyName"] = signingKey
    project.ext["signing.gnupg.passphrase"] = signingKeyPassphrase

    signing {
        useGpgCmd()
        sign(publishing.publications)
    }
}

rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin::class.java) {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension>().ignoreScripts = false
}

tasks.named("jsBrowserTest") {
    dependsOn("wasmJsTestTestDevelopmentExecutableCompileSync")
}
tasks.named("wasmJsBrowserTest") {
    dependsOn("jsTestTestDevelopmentExecutableCompileSync")
}
