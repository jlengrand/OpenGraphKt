plugins {
    kotlin("jvm")
    application
}

group = "fr.lengrand"

repositories {
    mavenCentral()
}

dependencies {
    implementation("fr.lengrand:opengraphkt:0.1.3")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass = "fr.lengrand.opengraphktremote.MainKt"
}
