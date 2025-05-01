package nl.lengrand.opengraphkt

import nl.lengrand.opengraphkt.nl.lengrand.opengraphkt.DocumentFetcher

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

fun main() {

    val fetcher = DocumentFetcher()

    val docUrl = fetcher.fromUrl("https://www.imdb.com/title/tt0068646/")
    val docString = fetcher.fromString(html)

    val ogUrl = Parser().extractOpenGraphTags(docUrl)
    println(ogUrl)
    println("-------------")
    val ogString = Parser().extractOpenGraphTags(docString)
    println(ogString)
}
