pluginManagement {
    plugins {
        kotlin("jvm") version "2.2.21"
    }
}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
rootProject.name = "OpenGraphKt"
include("opengraphkt")
include("demo")
include("demo-remote")
include("scrape-test")