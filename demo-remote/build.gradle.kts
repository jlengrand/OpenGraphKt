plugins {
    kotlin("jvm")
    application
}

group = "fr.lengrand"

repositories {
    mavenCentral()
}

dependencies {
    implementation("fr.lengrand:opengraphkt:0.1.1")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(24)
}

application {
    mainClass = "fr.lengrand.opengraphkt.MainKt"
}
