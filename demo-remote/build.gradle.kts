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
    implementation("fr.lengrand:opengraphkt:0.0.2")
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
