package fr.lengrand.opengraphkt

import com.fleeksoft.ksoup.Ksoup
import java.io.File
import java.net.URI

/**
 * Example demonstrating how to use the OpenGraphParser to extract Open Graph data from HTML.
 */
fun main() {
    val parser = Parser()

    // Example 1: Parse Open Graph data from a URL
    println("Example 1: Parsing from URL")
    try {
        val openGraphData = parser.parse(URI("https://www.imdb.com/title/tt0068646/").toURL())

        println("Title: ${openGraphData.title}")
        println("Is valid: ${openGraphData.isValid()}")
    } catch (e: Exception) {
        println("Error parsing URL: ${e.message}")
    }

    // Example 2: Parse Open Graph data from a file
    println("\nExample 2: Parsing from File")
    try {
        val resourceUrl = object {}.javaClass.getResource("/example.html")
        val resourceFile = File(resourceUrl!!.toURI())

        // Parse the file
        val openGraphData = parser.parse(resourceFile)

        println("Title: ${openGraphData.title}")
        println("Is valid: ${openGraphData.isValid()}")
    } catch (e: Exception) {
        println("Error parsing file: ${e.message}")
    }

    // Example 3: Parse Open Graph data from an HTML string
    println("\nExample 3: Parsing from HTML string")
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

    val openGraphData = parser.parse(html)

    println("Title: ${openGraphData.title}")
    println("Is valid: ${openGraphData.isValid()}")

    // Example 4: Parse Open Graph data from a Jsoup Document
    println("\nExample 4: Parsing from JSoup Document")

    val doc = Ksoup.parse(html)
    val openGraphDataDoc = parser.parse(doc)

    println("Title: ${openGraphDataDoc.title}")
    println("Is valid: ${openGraphDataDoc.isValid()}")
}
