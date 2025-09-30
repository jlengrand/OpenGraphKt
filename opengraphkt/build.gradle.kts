import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm")
    id("com.vanniktech.maven.publish") version "0.32.0"
    id("org.jetbrains.kotlinx.kover") version "0.9.1"
}

group = "fr.lengrand"
version = "0.1.2-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fleeksoft.ksoup:ksoup:0.2.4")
    implementation("com.fleeksoft.ksoup:ksoup-kotlinx:0.2.4")
    implementation("com.fleeksoft.ksoup:ksoup-network:0.2.5")
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

mavenPublishing {
    publishToMavenCentral(SonatypeHost.CENTRAL_PORTAL)

    signAllPublications()

    coordinates(group.toString(), "opengraphkt", version.toString())

    pom {
        name = "OpenGraphKt"
        description = "A minimalist Kotlin library to work with the Open Graph protocol."
        inceptionYear = "2025"
        url = "https://github.com/jlengrand/OpenGraphKt"
        licenses {
            license {
                name = "The MIT License"
                url = "https://mit-license.org/"
                distribution = "https://mit-license.org/"
            }
        }
        developers {
            developer {
                id = "jlengrand"
                name = "Julien Lengrand-Lambert"
                url = "https://github.com/jlengrand"
            }
        }
        scm {
            url = "https://github.com/jlengrand/OpenGraphKt"
            connection = "scm:git:git://github.com/jlengrand/OpenGraphKt.git"
            developerConnection = "scm:git:ssh://git@github.com/jlengrand/OpenGraphKt.git"
        }
    }
}

kover {
    reports {
        verify {
            rule {
                minBound(70)
            }
        }
    }
}