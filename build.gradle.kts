plugins {
    kotlin("jvm") version "2.1.20"
    application
}

group = "nl.lengrand"
version = "1.0-SNAPSHOT"

val ksoupVersion = "0.2.3"

repositories {
    mavenCentral()
}

dependencies {
//    implementation("com.fleeksoft.ksoup:ksoup:$ksoupVersion")
////    implementation("com.fleeksoft.ksoup:ksoup-kotlinx:$ksoupVersion")
//    implementation("com.fleeksoft.ksoup:ksoup-okio:$ksoupVersion")
//    implementation("com.fleeksoft.ksoup:ksoup-network:$ksoupVersion")
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
    mainClass = "nl.lengrand.MainKt"
}