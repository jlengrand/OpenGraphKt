package fr.lengrand.opengraphktremote

import fr.lengrand.opengraphkt.Parser

/**
 * This module is only here to verify that the latest Maven Central release can be imported and used as intended.
 */
fun main() {
    val parser = Parser()

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

    println("Parsing from JSoup Document")

    val openGraphDataDoc = parser.parse(html)

    println("Title: ${openGraphDataDoc.title}")
    println("Is valid: ${openGraphDataDoc.isValid()}")
}
