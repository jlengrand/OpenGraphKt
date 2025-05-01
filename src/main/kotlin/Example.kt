package nl.lengrand

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun main() {
    // Example HTML with Open Graph tags
    val html = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Open Graph Example</title>
            <meta property="og:title" content="The Rock" />
            <meta property="og:type" content="video.movie" />
            <meta property="og:url" content="https://example.com/the-rock" />
            <meta property="og:image" content="https://example.com/rock.jpg" />
            <meta property="og:description" content="An action movie about a rock" />
            <meta property="og:site_name" content="Example Movies" />
        </head>
        <body>
            <h1>Example Page</h1>
        </body>
        </html>
    """.trimIndent()

    // Parse the HTML string into a Document
    val doc = Jsoup.parse(html)
    
    // Demonstrate how to select all Open Graph tags
    println("Example 1: Select all Open Graph tags")
    val allOgTags = doc.select("meta[property^=og:]")
    allOgTags.forEach { tag ->
        println("${tag.attr("property")}: ${tag.attr("content")}")
    }
    
    // Demonstrate how to select a specific Open Graph tag
    println("\nExample 2: Select a specific Open Graph tag")
    val ogTitle = doc.select("meta[property=og:title]").attr("content")
    println("og:title: $ogTitle")
    
    // Demonstrate how to extract all Open Graph data into a map
    println("\nExample 3: Extract all Open Graph data into a map")
    val ogData = doc.select("meta[property^=og:]")
        .associate { it.attr("property") to it.attr("content") }
    
    ogData.forEach { (property, content) ->
        println("$property: $content")
    }
    
    // Demonstrate using our extractOpenGraphTags function
    println("\nExample 4: Using our extractOpenGraphTags function")
    val openGraph = extractOpenGraphTags(doc)
    println("Title: ${openGraph.title}")
    println("Image: ${openGraph.image}")
    println("Description: ${openGraph.description}")
    println("URL: ${openGraph.url}")
    println("Type: ${openGraph.type}")
}