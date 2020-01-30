rootProject.name = "kotlinx-html"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven("https://dl.bintray.com/kotlin/kotlin-dev")
    }
    val kotlin_version: String by settings
    plugins {
        kotlin("multiplatform") version "${kotlin_version}"
    }
}

enableFeaturePreview("GRADLE_METADATA")
