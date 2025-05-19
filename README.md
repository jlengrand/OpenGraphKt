# OpenGraphKt

[![build](https://github.com/jlengrand/OpenGraphKt/actions/workflows/gradle.yml/badge.svg)](https://github.com/simplex-chat/jlengrand/OpenGraphKt/workflows/gradle.yml)
![Codecov](https://img.shields.io/codecov/c/github/jlengrand/OpenGraphKt)
![GitHub Release Date](https://img.shields.io/github/release-date/jlengrand/OpenGraphKt)
![Maven Central Version](https://img.shields.io/maven-central/v/fr.lengrand/opengraphkt)
![kotlin-version](https://img.shields.io/badge/kotlin-2.1.0-blue?logo=kotlin)
![GitHub License](https://img.shields.io/github/license/jlengrand/OpenGraphKt)
![GitHub code size in bytes](https://img.shields.io/github/languages/code-size/jlengrand/OpenGraphKt)


[OpenGraphKt](https://github.com/jlengrand/OpenGraphKt) is a minimalist Kotlin library to work with the [Open Graph tags](https://ogp.me/) protocol. 
OpenGraphKt is a tiny wrapper on top of JSoup.

## Current status 

* Library can extract OpenGraph tags from HTML via a `URL`, `String` or `File` input.
* Current implementation is JVM only, due to the `JSoup` dependency.
* Protocol implementation is complete for `og:` tags, but types aren't fully correct (most types currently are `String`).
* Library should be considered in pre-alpha, use this in production at your own risks :).

## Usage

See [Main.kt](./demo-remote/src/main/kotlin/fr/lengrand/opengraphktremote/Main.kt) in the `demo-remote` submodule for usage examples. 

In short : 

* Add dependency to your Maven / Gradle file. For example : 

```bash
    implementation("fr.lengrand:opengraphkt:0.0.2")
```

* Enjoy: 

```kotlin
val parser = Parser()
val openGraphDataDoc = parser.parse("https://www.imdb.com/title/tt0068646/")

println("Title: ${openGraphDataDoc.title}")
println("Is valid: ${openGraphDataDoc.isValid()}")

// Title: The Rock
// Is valid: true
```


## Dependencies

- [JSoup](https://jsoup.org/)

## Author

* [Julien Lengrand-Lambert](https://github.com/jlengrand)

## License

* [The MIT LICENCE. See License](./LICENSE)