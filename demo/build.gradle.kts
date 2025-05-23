import org.gradle.kotlin.dsl.implementation

plugins {
    kotlin("jvm")
    application
}

group = "fr.lengrand"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.20.1")
    implementation(project(":opengraphkt"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(23)
}

application {
    mainClass = "fr.lengrand.opengraphkt.MainKt"
}
