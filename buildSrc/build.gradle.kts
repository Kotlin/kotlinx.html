plugins {
    kotlin("jvm") version "1.9.21"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.sun.xsom:xsom:20140925")
    implementation("com.squareup:kotlinpoet:1.15.3")
}