# OpenGraphKt

A simple Kotlin project demonstrating how to extract Open Graph tags from webpages using JSoup.

## What is Open Graph?

Open Graph is a protocol that enables any web page to become a rich object in a social graph. It was originally created by Facebook and is now widely used by many social media platforms and websites.

Open Graph tags are meta tags with property attributes that start with "og:". They are used to define properties like title, image, description, etc.

## How to Extract Open Graph Tags with JSoup

This project demonstrates several ways to extract Open Graph tags from HTML using JSoup:

### 1. Select all Open Graph tags

```kotlin
val allOgTags = document.select("meta[property^=og:]")
allOgTags.forEach { tag ->
    println("${tag.attr("property")}: ${tag.attr("content")}")
}
```

The CSS selector `meta[property^=og:]` selects all meta tags with a property attribute that starts with "og:".

### 2. Select a specific Open Graph tag

```kotlin
val ogTitle = document.select("meta[property=og:title]").attr("content")
println("og:title: $ogTitle")
```

### 3. Extract all Open Graph data into a map

```kotlin
val ogData = document.select("meta[property^=og:]")
    .associate { it.attr("property") to it.attr("content") }

ogData.forEach { (property, content) ->
    println("$property: $content")
}
```

### 4. Using a dedicated function

```kotlin
fun extractOpenGraphTags(document: Document): OpenGraph {
    // Select all meta tags with property attributes starting with "og:"
    val ogTags = document.select("meta[property^=og:]")
    
    // Extract the basic required Open Graph properties
    val title = ogTags.select("meta[property=og:title]").attr("content")
    val image = ogTags.select("meta[property=og:image]").attr("content")
    val description = ogTags.select("meta[property=og:description]").attr("content").takeIf { it.isNotEmpty() }
    val url = ogTags.select("meta[property=og:url]").attr("content").takeIf { it.isNotEmpty() }
    val type = ogTags.select("meta[property=og:type]").attr("content").takeIf { it.isNotEmpty() }
    
    return OpenGraph(title, image, description, url, type)
}
```

## Examples

The project includes two examples:

1. `Main.kt`: Connects to a real website (IMDB) and extracts Open Graph tags
2. `Example.kt`: Uses a local HTML string with Open Graph tags for demonstration

## Running the Examples

To run the Example.kt file:

```bash
./gradlew run
```

To run the Main.kt file, update the `mainClass` in `build.gradle.kts` to "nl.lengrand.MainKt" and run:

```bash
./gradlew run
```

## Dependencies

- JSoup 1.20.1: A Java library for working with HTML