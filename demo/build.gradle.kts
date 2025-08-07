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
    implementation("com.fleeksoft.ksoup:ksoup:0.2.4")
    implementation("com.fleeksoft.ksoup:ksoup-kotlinx:0.2.5")
    implementation("com.fleeksoft.ksoup:ksoup-network:0.2.4")
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
