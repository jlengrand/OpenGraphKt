package fr.lengrand.opengraphkt

import org.junit.jupiter.api.Test
import java.net.URI
import java.time.OffsetDateTime
import kotlin.test.assertTrue

class GeneratorTest {

    private val generator = Generator()

    @Test
    fun `test generate with basic metadata`() {
        // Create a simple Data object with only basic metadata
        val data = Data(
            tags = emptyList(),
            title = "Test Title",
            type = "website",
            url = URI("https://example.com").toURL(),
            description = "Test Description",
            siteName = "Test Site",
            determiner = "the",
            locale = "en_US",
            localeAlternate = listOf("fr_FR", "es_ES"),
            images = emptyList(),
            videos = emptyList(),
            audios = emptyList(),
            article = null,
            profile = null,
            book = null,
            musicSong = null,
            musicAlbum = null,
            musicPlaylist = null,
            musicRadioStation = null,
            videoMovie = null,
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that all basic metadata tags are generated correctly
        assertTrue(html.contains("<meta property=\"og:title\" content=\"Test Title\" />"))
        assertTrue(html.contains("<meta property=\"og:type\" content=\"website\" />"))
        assertTrue(html.contains("<meta property=\"og:url\" content=\"https://example.com\" />"))
        assertTrue(html.contains("<meta property=\"og:description\" content=\"Test Description\" />"))
        assertTrue(html.contains("<meta property=\"og:site_name\" content=\"Test Site\" />"))
        assertTrue(html.contains("<meta property=\"og:determiner\" content=\"the\" />"))
        assertTrue(html.contains("<meta property=\"og:locale\" content=\"en_US\" />"))
        assertTrue(html.contains("<meta property=\"og:locale:alternate\" content=\"fr_FR\" />"))
        assertTrue(html.contains("<meta property=\"og:locale:alternate\" content=\"es_ES\" />"))
    }

    @Test
    fun `test generate with images`() {
        // Create a Data object with images
        val data = Data(
            tags = emptyList(),
            title = "Test Title",
            type = "website",
            url = URI("https://example.com").toURL(),
            description = null,
            siteName = null,
            determiner = null,
            locale = null,
            localeAlternate = emptyList(),
            images = listOf(
                Image(
                    url = "https://example.com/image1.jpg",
                    secureUrl = "https://secure.example.com/image1.jpg",
                    type = "image/jpeg",
                    width = 800,
                    height = 600,
                    alt = "Test Image 1"
                ),
                Image(
                    url = "https://example.com/image2.png",
                    secureUrl = null,
                    type = "image/png",
                    width = 1024,
                    height = 768,
                    alt = null
                )
            ),
            videos = emptyList(),
            audios = emptyList(),
            article = null,
            profile = null,
            book = null,
            musicSong = null,
            musicAlbum = null,
            musicPlaylist = null,
            musicRadioStation = null,
            videoMovie = null,
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that all image tags are generated correctly
        assertTrue(html.contains("<meta property=\"og:image\" content=\"https://example.com/image1.jpg\" />"))
        assertTrue(html.contains("<meta property=\"og:image:secure_url\" content=\"https://secure.example.com/image1.jpg\" />"))
        assertTrue(html.contains("<meta property=\"og:image:type\" content=\"image/jpeg\" />"))
        assertTrue(html.contains("<meta property=\"og:image:width\" content=\"800\" />"))
        assertTrue(html.contains("<meta property=\"og:image:height\" content=\"600\" />"))
        assertTrue(html.contains("<meta property=\"og:image:alt\" content=\"Test Image 1\" />"))
        
        assertTrue(html.contains("<meta property=\"og:image\" content=\"https://example.com/image2.png\" />"))
        assertTrue(html.contains("<meta property=\"og:image:type\" content=\"image/png\" />"))
        assertTrue(html.contains("<meta property=\"og:image:width\" content=\"1024\" />"))
        assertTrue(html.contains("<meta property=\"og:image:height\" content=\"768\" />"))
    }

    @Test
    fun `test generate with article`() {
        // Create a Data object with article-specific metadata
        val data = Data(
            tags = emptyList(),
            title = "Test Article",
            type = "article",
            url = URI("https://example.com/article").toURL(),
            description = null,
            siteName = null,
            determiner = null,
            locale = null,
            localeAlternate = emptyList(),
            images = emptyList(),
            videos = emptyList(),
            audios = emptyList(),
            article = Article(
                publishedTime = OffsetDateTime.parse("2023-01-01T00:00:00Z"),
                modifiedTime = OffsetDateTime.parse("2023-01-02T12:00:00Z"),
                expirationTime = null,
                authors = listOf("John Doe", "Jane Smith"),
                section = "News",
                tags = listOf("test", "article")
            ),
            profile = null,
            book = null,
            musicSong = null,
            musicAlbum = null,
            musicPlaylist = null,
            musicRadioStation = null,
            videoMovie = null,
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that all article-specific tags are generated correctly
        assertTrue(html.contains("<meta property=\"og:article:published_time\" content=\"2023-01-01T00:00:00Z\" />"))
        assertTrue(html.contains("<meta property=\"og:article:modified_time\" content=\"2023-01-02T12:00:00Z\" />"))
        assertTrue(html.contains("<meta property=\"og:article:section\" content=\"News\" />"))
        assertTrue(html.contains("<meta property=\"og:article:author\" content=\"John Doe\" />"))
        assertTrue(html.contains("<meta property=\"og:article:author\" content=\"Jane Smith\" />"))
        assertTrue(html.contains("<meta property=\"og:article:tag\" content=\"test\" />"))
        assertTrue(html.contains("<meta property=\"og:article:tag\" content=\"article\" />"))
    }

    @Test
    fun `test generate with profile`() {
        // Create a Data object with profile-specific metadata
        val data = Data(
            tags = emptyList(),
            title = "Test Profile",
            type = "profile",
            url = URI("https://example.com/profile").toURL(),
            description = null,
            siteName = null,
            determiner = null,
            locale = null,
            localeAlternate = emptyList(),
            images = emptyList(),
            videos = emptyList(),
            audios = emptyList(),
            article = null,
            profile = Profile(
                firstName = "John",
                lastName = "Doe",
                username = "johndoe",
                gender = Gender.MALE
            ),
            book = null,
            musicSong = null,
            musicAlbum = null,
            musicPlaylist = null,
            musicRadioStation = null,
            videoMovie = null,
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that all profile-specific tags are generated correctly
        assertTrue(html.contains("<meta property=\"og:profile:first_name\" content=\"John\" />"))
        assertTrue(html.contains("<meta property=\"og:profile:last_name\" content=\"Doe\" />"))
        assertTrue(html.contains("<meta property=\"og:profile:username\" content=\"johndoe\" />"))
        assertTrue(html.contains("<meta property=\"og:profile:gender\" content=\"male\" />"))
    }

    @Test
    fun `test generate with video movie`() {
        // Create a Data object with video.movie-specific metadata
        val data = Data(
            tags = emptyList(),
            title = "Test Movie",
            type = "video.movie",
            url = URI("https://example.com/movie").toURL(),
            description = null,
            siteName = null,
            determiner = null,
            locale = null,
            localeAlternate = emptyList(),
            images = emptyList(),
            videos = listOf(
                Video(
                    url = "https://example.com/movie.mp4",
                    secureUrl = null,
                    type = "video/mp4",
                    width = 1280,
                    height = 720,
                    duration = 120
                )
            ),
            audios = emptyList(),
            article = null,
            profile = null,
            book = null,
            musicSong = null,
            musicAlbum = null,
            musicPlaylist = null,
            musicRadioStation = null,
            videoMovie = VideoMovie(
                actors = listOf("Actor 1", "Actor 2"),
                director = listOf("Director"),
                writer = listOf("Writer 1", "Writer 2"),
                duration = 120,
                releaseDate = OffsetDateTime.parse("2023-01-01T00:00:00Z"),
                tags = listOf("action", "drama")
            ),
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that all video.movie-specific tags are generated correctly
        assertTrue(html.contains("<meta property=\"og:video\" content=\"https://example.com/movie.mp4\" />"))
        assertTrue(html.contains("<meta property=\"og:video:type\" content=\"video/mp4\" />"))
        assertTrue(html.contains("<meta property=\"og:video:width\" content=\"1280\" />"))
        assertTrue(html.contains("<meta property=\"og:video:height\" content=\"720\" />"))
        assertTrue(html.contains("<meta property=\"og:video:duration\" content=\"120\" />"))
        
        assertTrue(html.contains("<meta property=\"og:video:actor\" content=\"Actor 1\" />"))
        assertTrue(html.contains("<meta property=\"og:video:actor\" content=\"Actor 2\" />"))
        assertTrue(html.contains("<meta property=\"og:video:director\" content=\"Director\" />"))
        assertTrue(html.contains("<meta property=\"og:video:writer\" content=\"Writer 1\" />"))
        assertTrue(html.contains("<meta property=\"og:video:writer\" content=\"Writer 2\" />"))
        assertTrue(html.contains("<meta property=\"og:video:release_date\" content=\"2023-01-01T00:00:00Z\" />"))
        assertTrue(html.contains("<meta property=\"og:video:tag\" content=\"action\" />"))
        assertTrue(html.contains("<meta property=\"og:video:tag\" content=\"drama\" />"))
    }

    @Test
    fun `test content escaping`() {
        // Create a Data object with content that needs escaping
        val data = Data(
            tags = emptyList(),
            title = "Test \"Quoted\" Title",
            type = "website",
            url = URI("https://example.com").toURL(),
            description = null,
            siteName = null,
            determiner = null,
            locale = null,
            localeAlternate = emptyList(),
            images = emptyList(),
            videos = emptyList(),
            audios = emptyList(),
            article = null,
            profile = null,
            book = null,
            musicSong = null,
            musicAlbum = null,
            musicPlaylist = null,
            musicRadioStation = null,
            videoMovie = null,
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that quotes are properly escaped
        assertTrue(html.contains("<meta property=\"og:title\" content=\"Test &quot;Quoted&quot; Title\" />"))
    }

    @Test
    fun `test round trip conversion`() {
        // Create a sample HTML with OpenGraph tags
        val sampleHtml = """
            <!DOCTYPE html>
            <html>
            <head>
                <title>Open Graph Example</title>
                <meta property="og:title" content="The Rock" />
                <meta property="og:type" content="video.movie" />
                <meta property="og:url" content="https://example.com/the-rock" />
                <meta property="og:image" content="https://example.com/rock.jpg" />
                <meta property="og:description" content="An action movie about a rock" />
            </head>
            <body>
                <h1>Example Page</h1>
            </body>
            </html>
        """.trimIndent()

        // Parse the HTML to get a Data object
        val parser = Parser()
        val data = parser.parse(sampleHtml)

        // Generate HTML tags from the Data object
        val generatedHtml = generator.generate(data)

        // Verify that all original tags are present in the generated HTML
        assertTrue(generatedHtml.contains("<meta property=\"og:title\" content=\"The Rock\" />"))
        assertTrue(generatedHtml.contains("<meta property=\"og:type\" content=\"video.movie\" />"))
        assertTrue(generatedHtml.contains("<meta property=\"og:url\" content=\"https://example.com/the-rock\" />"))
        assertTrue(generatedHtml.contains("<meta property=\"og:image\" content=\"https://example.com/rock.jpg\" />"))
        assertTrue(generatedHtml.contains("<meta property=\"og:description\" content=\"An action movie about a rock\" />"))
    }
}