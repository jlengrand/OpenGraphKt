package nl.lengrand.opengraphkt.examples

import nl.lengrand.opengraphkt.DocumentFetcher
import nl.lengrand.opengraphkt.OpenGraphParser

/**
 * Example demonstrating how to use the OpenGraphParser to extract Open Graph data from HTML.
 */
fun main() {
    // Create instances of the parser and document fetcher
    val parser = OpenGraphParser()
    val fetcher = DocumentFetcher()

    // Example 1: Parse Open Graph data from a URL
    println("Example 1: Parsing from URL")
    try {
        val document = fetcher.fromUrl("https://www.imdb.com/title/tt0068646/")
        val openGraphData = parser.parse(document)
        
        println("Title: ${openGraphData.title}")
        println("Type: ${openGraphData.type}")
        println("URL: ${openGraphData.url}")
        println("Description: ${openGraphData.description}")
        println("Site Name: ${openGraphData.siteName}")
        
        println("Images: ${openGraphData.images.size}")
        openGraphData.images.forEachIndexed { index, image ->
            println("Image ${index + 1}: ${image.url}")
            println("  Width: ${image.width}")
            println("  Height: ${image.height}")
            println("  Alt: ${image.alt}")
        }
        
        println("Is valid: ${openGraphData.isValid()}")
    } catch (e: Exception) {
        println("Error parsing URL: ${e.message}")
    }
    
    // Example 2: Parse Open Graph data from an HTML string
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
    println("Type: ${openGraphData.type}")
    println("URL: ${openGraphData.url}")
    println("Description: ${openGraphData.description}")
    println("Site Name: ${openGraphData.siteName}")
    
    println("Images: ${openGraphData.images.size}")
    openGraphData.images.forEachIndexed { index, image ->
        println("Image ${index + 1}: ${image.url}")
        println("  Width: ${image.width}")
        println("  Height: ${image.height}")
    }
    
    println("Is valid: ${openGraphData.isValid()}")
    
    // Example 3: Working with multiple images
    println("\nExample 3: Working with multiple images")
    val multipleImagesHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Multiple Images Example</title>
            <meta property="og:title" content="Photo Gallery" />
            <meta property="og:type" content="website" />
            <meta property="og:url" content="https://example.com/gallery" />
            <meta property="og:image" content="https://example.com/image1.jpg" />
            <meta property="og:image:width" content="800" />
            <meta property="og:image:height" content="600" />
            <meta property="og:image" content="https://example.com/image2.jpg" />
            <meta property="og:image:width" content="1024" />
            <meta property="og:image:height" content="768" />
            <meta property="og:image" content="https://example.com/image3.jpg" />
            <meta property="og:image:width" content="1200" />
            <meta property="og:image:height" content="900" />
            <meta property="og:description" content="A gallery of images" />
        </head>
        <body>
            <h1>Photo Gallery</h1>
        </body>
        </html>
    """.trimIndent()
    
    val multipleImagesDocument = fetcher.fromString(multipleImagesHtml)
    val multipleImagesData = parser.parse(multipleImagesDocument)
    
    println("Title: ${multipleImagesData.title}")
    println("Images: ${multipleImagesData.images.size}")
    multipleImagesData.images.forEachIndexed { index, image ->
        println("Image ${index + 1}: ${image.url}")
        println("  Width: ${image.width}")
        println("  Height: ${image.height}")
    }
    
    // Example 4: Working with article metadata
    println("\nExample 4: Working with article metadata")
    val articleHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Article Example</title>
            <meta property="og:title" content="Breaking News" />
            <meta property="og:type" content="article" />
            <meta property="og:url" content="https://example.com/news/breaking" />
            <meta property="og:image" content="https://example.com/news.jpg" />
            <meta property="og:description" content="Latest breaking news" />
            <meta property="og:article:published_time" content="2023-01-01T00:00:00Z" />
            <meta property="og:article:modified_time" content="2023-01-02T12:00:00Z" />
            <meta property="og:article:section" content="News" />
            <meta property="og:article:author" content="John Doe" />
            <meta property="og:article:tag" content="breaking" />
            <meta property="og:article:tag" content="news" />
        </head>
        <body>
            <h1>Breaking News</h1>
        </body>
        </html>
    """.trimIndent()
    
    val articleDocument = fetcher.fromString(articleHtml)
    val articleData = parser.parse(articleDocument)
    
    println("Title: ${articleData.title}")
    println("Type: ${articleData.type}")
    
    val article = articleData.article
    if (article != null) {
        println("Published Time: ${article.publishedTime}")
        println("Modified Time: ${article.modifiedTime}")
        println("Section: ${article.section}")
        println("Authors: ${article.authors.joinToString(", ")}")
        println("Tags: ${article.tags.joinToString(", ")}")
    }
}