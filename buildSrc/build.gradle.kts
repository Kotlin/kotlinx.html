plugins {
    kotlin("jvm") version "1.3.72"
}

repositories {
    jcenter()
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.sun.xsom:xsom:20140925")
}
