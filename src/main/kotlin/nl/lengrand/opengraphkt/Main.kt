package nl.lengrand.opengraphkt

import java.io.File

/**
 * Example demonstrating how to use the OpenGraphParser to extract Open Graph data from HTML.
 */
fun main() {
    val parser = OpenGraphParser()
    val fetcher = DocumentFetcher()

    // Example 1: Parse Open Graph data from a URL
    println("Example 1: Parsing from URL")
    try {
        val document = fetcher.fromUrl("https://www.imdb.com/title/tt0068646/")
        val openGraphData = parser.parse(document)

        println("Title: ${openGraphData.title}")
        println("Is valid: ${openGraphData.isValid()}")
    } catch (e: Exception) {
        println("Error parsing URL: ${e.message}")
    }

    // Example 2: Parse Open Graph data from a file
    println("\nExample 2: Parsing from File")
    try {
        val resourceUrl = object {}.javaClass.getResource("/example.html")
        val resourceFile = File(resourceUrl.toURI())

        // Parse the file
        val document = fetcher.fromFile(resourceFile)
        val openGraphData = parser.parse(document)

        println("Title: ${openGraphData.title}")
        println("Is valid: ${openGraphData.isValid()}")
    } catch (e: Exception) {
        println("Error parsing file: ${e.message}")
    }

    // Example 3: Parse Open Graph data from an HTML string
    println("\nExample 2: Parsing from HTML string")
    val html = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Open Graph Example</title>
            <meta property="og:title" content="The Rock" />
            <meta property="og:type" content="video.movie" />
            <meta property="og:url" content="https://example.com/the-rock" />
            <meta property="og:image" content="https://example.com/rock.jpg" />
            <meta property="og:image:width" content="300" />
            <meta property="og:image:height" content="200" />
            <meta property="og:description" content="An action movie about a rock" />
            <meta property="og:site_name" content="Example Movies" />
        </head>
        <body>
            <h1>Example Page</h1>
        </body>
        </html>
    """.trimIndent()

    val document = fetcher.fromString(html)
    val openGraphData = parser.parse(document)

    println("Title: ${openGraphData.title}")
    println("Is valid: ${openGraphData.isValid()}")
}
