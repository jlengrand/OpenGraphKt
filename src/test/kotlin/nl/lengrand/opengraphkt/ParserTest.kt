package nl.lengrand.opengraphkt

import nl.lengrand.opengraphkt.nl.lengrand.opengraphkt.DocumentFetcher
import org.jsoup.nodes.Document
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class ParserTest {

    private val parser = Parser()
    private val fetcher = DocumentFetcher()

    // Sample HTML with all required OpenGraph tags
    private val completeHtml = """
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

    // Sample HTML with missing required OpenGraph tags
    private val incompleteHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Open Graph Example</title>
            <meta property="og:title" content="The Rock" />
            <meta property="og:description" content="An action movie about a rock" />
        </head>
        <body>
            <h1>Example Page</h1>
        </body>
        </html>
    """.trimIndent()

    // Sample HTML with no OpenGraph tags
    private val noOgHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>No Open Graph Example</title>
        </head>
        <body>
            <h1>Example Page</h1>
        </body>
        </html>
    """.trimIndent()

    @Test
    fun `test extractOpenGraphTags with complete OpenGraph tags`() {
        val document = fetcher.fromString(completeHtml)
        val openGraph = parser.extractOpenGraphTags(document)

        // Verify that all required properties are extracted correctly
        assertEquals("The Rock", openGraph.title)
        assertEquals("video.movie", openGraph.type)
        assertEquals("https://example.com/the-rock", openGraph.url)
        assertEquals("https://example.com/rock.jpg", openGraph.image)

        // Verify that the OpenGraph object is valid
        assertTrue(openGraph.isValid())

        // Verify that all tags are extracted
        assertEquals(6, openGraph.tags.size)

        // Verify specific tag content
        val descriptionTag = openGraph.tags.find { it.property == "description" }
        assertNotNull(descriptionTag)
        assertEquals("An action movie about a rock", descriptionTag.content)

        val siteNameTag = openGraph.tags.find { it.property == "site_name" }
        assertNotNull(siteNameTag)
        assertEquals("Example Movies", siteNameTag.content)
    }

    @Test
    fun `test extractOpenGraphTags with incomplete OpenGraph tags`() {
        val document = fetcher.fromString(incompleteHtml)
        val openGraph = parser.extractOpenGraphTags(document)

        assertEquals("The Rock", openGraph.title)

        assertEquals(null, openGraph.type)
        assertEquals(null, openGraph.url)
        assertEquals(null, openGraph.image)

        assertFalse(openGraph.isValid())
        assertEquals(2, openGraph.tags.size)

        val descriptionTag = openGraph.tags.find { it.property == "description" }
        assertNotNull(descriptionTag)
        assertEquals("An action movie about a rock", descriptionTag.content)
    }

    @Test
    fun `test extractOpenGraphTags with no OpenGraph tags`() {
        val document = fetcher.fromString(noOgHtml)
        val openGraph = parser.extractOpenGraphTags(document)

        // Verify that all properties are null
        assertEquals(null, openGraph.title)
        assertEquals(null, openGraph.type)
        assertEquals(null, openGraph.url)
        assertEquals(null, openGraph.image)

        assertFalse(openGraph.isValid())
        assertEquals(0, openGraph.tags.size)
    }
}
