plugins {
    kotlin("jvm") version "1.8.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.sun.xsom:xsom:20140925")
}