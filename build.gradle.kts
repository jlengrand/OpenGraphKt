plugins {
    kotlin("jvm") version "2.1.20"
    application
}

group = "nl.lengrand"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.20.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(23)
}

application {
    mainClass = "nl.lengrand.opengraphkt.MainKt"
}
