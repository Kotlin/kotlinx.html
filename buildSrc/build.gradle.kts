plugins {
    kotlin("jvm") version "1.8.20"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.sun.xsom:xsom:20140925")
    implementation("com.squareup:kotlinpoet:1.15.3")
}