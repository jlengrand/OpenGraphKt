plugins {
    kotlin("jvm") version "2.1.21"
}

group = "fr.lengrand"
version = "0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jsoup:jsoup:1.20.1")
    testImplementation(kotlin("test"))
}

java {
    withSourcesJar()
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes(mapOf("Implementation-Title" to project.name,
            "Implementation-Version" to project.version))
    }
}


kotlin {
    jvmToolchain(23)
}