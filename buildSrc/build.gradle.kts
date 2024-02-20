plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.sun.xsom:xsom:20140925")
    implementation("com.squareup:kotlinpoet:1.15.3")
}
