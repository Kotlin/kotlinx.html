import Build_gradle.MavenPomFile
import kotlinx.html.js.packageJson

/**
 * This build script supports following parameters:
 * -PversionTag   - works together with "branch-build" profile and overrides "-SNAPSHOT" suffix of the version.
 */
plugins {
    kotlin("multiplatform") version "1.8.20"
    id("maven-publish")
    id("signing")
}

group = "org.jetbrains.kotlinx"
version = "0.8.1-SNAPSHOT"

buildscript {
    dependencies {
        classpath("org.jetbrains.kotlinx:binary-compatibility-validator:0.11.0")
    }
}

apply(plugin = "binary-compatibility-validator")

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

        create<MavenPublication>("kotlinx-html-assembly") {
            artifactId = "kotlinx-html-assembly"
            jar("jsWebJar") {
                archiveBaseName by "${project.name}-assembly"
                archiveClassifier by "webjar"
                from("$buildDir/js/packages/${project.name}/kotlin/kotlinx-html-js.js")
                into("META-INF/resources/webjars/${project.name}/${project.version}/")
            }
        }
    }
}

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        mavenPublication {
            groupId = group as String
            pom { name by "${project.name}-jvm" }

            javadocJar("jvmJavadocJar")
            jar("jvmTestSourcesJar") {
                archiveClassifier by "test-sources"
                with(sourceSets["jvmTest"]) {
                    from(kotlin, resources)
                }
            }
        }
    }
    js(BOTH) {
        moduleName = project.name

        compilations["main"].packageJson {
            main = "kotlin/kotlinx-html-js.js"
            name = "kotlinx-html-js"
        }

        compilations["main"].kotlinOptions.apply {
            outputFile = "$buildDir/js/packages/${project.name}/kotlin/${project.name}-js.js"
            moduleKind = "umd"
            sourceMap = true
            sourceMapEmbedSources = "always"
        }

        compilations["test"].kotlinOptions.apply {
            moduleKind = "umd"
            metaInfo = true
            sourceMap = true
        }

        mavenPublication {
            groupId = group as String
            pom { name by "${project.name}-js" }

            javadocJar("jsJavadocJar")
            jar("jsTestSourcesJar") {
                archiveClassifier by "test-sources"
                with(sourceSets["jsTest"]) {
                    from(kotlin, resources)
                }
            }
        }
    }

    mingwX64()
    linuxX64()
    iosX64()
    iosArm64()
    iosArm32()
    iosSimulatorArm64()
    watchosX86()
    watchosX64()
    watchosArm32()
    watchosArm64()
    watchosSimulatorArm64()
    tvosX64()
    tvosArm64()
    tvosSimulatorArm64()
    macosX64()
    macosArm64()

    metadata {
        mavenPublication {
            groupId = group as String
            artifactId = "${project.name}-common"
            pom {
                name by "${project.name}-common"
            }

            javadocJar("commonJavadocJar")
            jar("commonTestSourcesJar") {
                archiveClassifier by "test-sources"
            }
        }
    }
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }

        val jsMain by getting {
            dependencies {
                implementation(kotlin("stdlib-js"))
            }
        }

        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }

        val nativeMain by creating
        val nativeTest by creating

        val nativeTargets = listOf(
            "mingwX64",
            "linuxX64",
            "iosX64",
            "iosArm64",
            "iosArm32",
            "iosSimulatorArm64",
            "watchosX86",
            "watchosX64",
            "watchosArm32",
            "watchosArm64",
            "watchosSimulatorArm64",
            "tvosX64",
            "tvosArm64",
            "tvosSimulatorArm64",
            "macosX64",
            "macosArm64"
        )

        val commonMain by getting
        nativeMain.dependsOn(commonMain)

        nativeTargets.forEach { target ->
            findByName("${target}Main")?.dependsOn(nativeMain)
            findByName("${target}Test")?.dependsOn(nativeTest)
        }
    }
}

tasks.withType<Jar> {
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
            "kotlinx.html",
            "src/commonMain/kotlin/generated",
            "src/jsMain/kotlin/generated"
        )
    }
}

tasks.register<Copy>("jsPackagePrepare") {
    dependsOn("jsLegacyMainClasses")
    tasks["assemble"].dependsOn(this)

    group = "build"
    description = "Assembles NPM package (result is placed into 'build/tmp/jsPackage')."

    val baseTargetDir = "$buildDir/tmp/jsPackage"

    from("README-JS.md")
    from("$buildDir/js/packages/${project.name}/kotlin")
    into(baseTargetDir)

    rename("README-JS.md", "README.md")

    doLast {
        var npmVersion = version as String
        if (npmVersion.endsWith("-SNAPSHOT")) {
            npmVersion = npmVersion.replace("-SNAPSHOT", "-${System.currentTimeMillis()}")
        }

        val organization = when {
            project.hasProperty("branch-build") -> "kotlinx-branch-build"
            project.hasProperty("master-build") -> "kotlinx-master-build"
            else -> null
        }

        File(baseTargetDir, "package.json").writeText(packageJson(npmVersion, organization))
        file("$baseTargetDir/kotlinx-html-js").renameTo(File("$buildDir/js-module/kotlinx-html-js"))
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

    url by "https://github.com/Kotlin/kotlinx.html"
    name by "kotlinx.html"
    description by "A kotlinx.html library provides DSL to build HTML to Writer/Appendable or DOM at JVM and browser (or other JavaScript engine) for better Kotlin programming for Web."

    licenses {
        license {
            name by "The Apache License, Version 2.0"
            url by "https://www.apache.org/licenses/LICENSE-2.0.txt"
        }
    }

    scm {
        connection by "scm:git:git@github.com:Kotlin/kotlinx.html.git"
        url by "https://github.com/Kotlin/kotlinx.html"
        tag by "HEAD"
    }

    developers {
        developer {
            name by "Sergey Mashkov"
            organization by "JetBrains s.r.o."
            roles to "developer"
        }

        developer {
            name by "Anton Dmitriev"
            organization by "JetBrains s.r.o."
            roles to "developer"
        }
    }
}

tasks.withType<GenerateModuleMetadata> {
    enabled = true
}

fun MavenPublication.jar(taskName: String, config: Action<Jar>) = artifact(tasks.create(taskName, Jar::class, config))

fun MavenPublication.javadocJar(taskName: String, config: Jar.() -> Unit = {}) = jar(taskName) {
    archiveClassifier by "javadoc"
    config()
}

infix fun <T> Property<T>.by(value: T) {
    set(value)
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

rootProject.plugins.withType<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootPlugin> {
    val nodeM1Version = "16.13.1"
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension>().nodeVersion = nodeM1Version
}

rootProject.plugins.withType(org.jetbrains.kotlin.gradle.targets.js.yarn.YarnPlugin::class.java) {
    rootProject.the<org.jetbrains.kotlin.gradle.targets.js.yarn.YarnRootExtension>().ignoreScripts = false
}
