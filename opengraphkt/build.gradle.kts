import com.vanniktech.maven.publish.SonatypeHost

plugins {
    kotlin("jvm") version "2.1.21"
    id("com.vanniktech.maven.publish") version "0.30.0"
}

group = "fr.lengrand"
version = "0.0.1"

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