package fr.lengrand.opengraphkt

import org.junit.jupiter.api.Test
import java.net.URI
import java.time.OffsetDateTime
import kotlin.test.assertEquals
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
    fun `test generate with audio`() {
        // Create a Data object with audio metadata
        val data = Data(
            tags = emptyList(),
            title = "Test Audio",
            type = "website",
            url = URI("https://example.com/audio").toURL(),
            description = null,
            siteName = null,
            determiner = null,
            locale = null,
            localeAlternate = emptyList(),
            images = emptyList(),
            videos = emptyList(),
            audios = listOf(
                Audio(
                    url = "https://example.com/audio.mp3",
                    secureUrl = "https://secure.example.com/audio.mp3",
                    type = "audio/mpeg"
                ),
                Audio(
                    url = "https://example.com/audio.wav",
                    secureUrl = null,
                    type = "audio/wav"
                )
            ),
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

        // Verify that all audio tags are generated correctly
        assertTrue(html.contains("<meta property=\"og:audio\" content=\"https://example.com/audio.mp3\" />"))
        assertTrue(html.contains("<meta property=\"og:audio:secure_url\" content=\"https://secure.example.com/audio.mp3\" />"))
        assertTrue(html.contains("<meta property=\"og:audio:type\" content=\"audio/mpeg\" />"))

        assertTrue(html.contains("<meta property=\"og:audio\" content=\"https://example.com/audio.wav\" />"))
        assertTrue(html.contains("<meta property=\"og:audio:type\" content=\"audio/wav\" />"))
    }

    @Test
    fun `test generate with book`() {
        // Create a Data object with book metadata
        val data = Data(
            tags = emptyList(),
            title = "Test Book",
            type = "book",
            url = URI("https://example.com/book").toURL(),
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
            book = Book(
                authors = listOf("Author 1", "Author 2"),
                isbn = "978-3-16-148410-0",
                releaseDate = OffsetDateTime.parse("2023-01-01T00:00:00Z"),
                tags = listOf("fiction", "fantasy")
            ),
            musicSong = null,
            musicAlbum = null,
            musicPlaylist = null,
            musicRadioStation = null,
            videoMovie = null,
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that all book tags are generated correctly
        assertTrue(html.contains("<meta property=\"og:book:author\" content=\"Author 1\" />"))
        assertTrue(html.contains("<meta property=\"og:book:author\" content=\"Author 2\" />"))
        assertTrue(html.contains("<meta property=\"og:book:isbn\" content=\"978-3-16-148410-0\" />"))
        assertTrue(html.contains("<meta property=\"og:book:release_date\" content=\"2023-01-01T00:00:00Z\" />"))
        assertTrue(html.contains("<meta property=\"og:book:tag\" content=\"fiction\" />"))
        assertTrue(html.contains("<meta property=\"og:book:tag\" content=\"fantasy\" />"))
    }

    @Test
    fun `test generate with music song`() {
        // Create a Data object with music.song metadata
        val data = Data(
            tags = emptyList(),
            title = "Test Song",
            type = "music.song",
            url = URI("https://example.com/song").toURL(),
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
            musicSong = MusicSong(
                duration = 240,
                album = "Test Album",
                albumDisc = 1,
                albumTrack = 5,
                musician = listOf("Musician 1", "Musician 2")
            ),
            musicAlbum = null,
            musicPlaylist = null,
            musicRadioStation = null,
            videoMovie = null,
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that all music.song tags are generated correctly
        assertTrue(html.contains("<meta property=\"og:music:duration\" content=\"240\" />"))
        assertTrue(html.contains("<meta property=\"og:music:album\" content=\"Test Album\" />"))
        assertTrue(html.contains("<meta property=\"og:music:album:disc\" content=\"1\" />"))
        assertTrue(html.contains("<meta property=\"og:music:album:track\" content=\"5\" />"))
        assertTrue(html.contains("<meta property=\"og:music:musician\" content=\"Musician 1\" />"))
        assertTrue(html.contains("<meta property=\"og:music:musician\" content=\"Musician 2\" />"))
    }

    @Test
    fun `test generate with music album`() {
        // Create a Data object with music.album metadata
        val data = Data(
            tags = emptyList(),
            title = "Test Album",
            type = "music.album",
            url = URI("https://example.com/album").toURL(),
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
            musicAlbum = MusicAlbum(
                songs = listOf("Song 1", "Song 2"),
                songDisc = 1,
                songTrack = 1,
                musician = listOf("Musician 1", "Musician 2"),
                releaseDate = OffsetDateTime.parse("2023-01-01T00:00:00Z")
            ),
            musicPlaylist = null,
            musicRadioStation = null,
            videoMovie = null,
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that all music.album tags are generated correctly
        assertTrue(html.contains("<meta property=\"og:music:song\" content=\"Song 1\" />"))
        assertTrue(html.contains("<meta property=\"og:music:song\" content=\"Song 2\" />"))
        assertTrue(html.contains("<meta property=\"og:music:song:disc\" content=\"1\" />"))
        assertTrue(html.contains("<meta property=\"og:music:song:track\" content=\"1\" />"))
        assertTrue(html.contains("<meta property=\"og:music:musician\" content=\"Musician 1\" />"))
        assertTrue(html.contains("<meta property=\"og:music:musician\" content=\"Musician 2\" />"))
        assertTrue(html.contains("<meta property=\"og:music:release_date\" content=\"2023-01-01T00:00:00Z\" />"))
    }

    @Test
    fun `test generate with music playlist`() {
        // Create a Data object with music.playlist metadata
        val data = Data(
            tags = emptyList(),
            title = "Test Playlist",
            type = "music.playlist",
            url = URI("https://example.com/playlist").toURL(),
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
            musicPlaylist = MusicPlaylist(
                songs = listOf("Song 1", "Song 2"),
                songDisc = 1,
                songTrack = 1,
                creator = "Playlist Creator"
            ),
            musicRadioStation = null,
            videoMovie = null,
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that all music.playlist tags are generated correctly
        assertTrue(html.contains("<meta property=\"og:music:song\" content=\"Song 1\" />"))
        assertTrue(html.contains("<meta property=\"og:music:song\" content=\"Song 2\" />"))
        assertTrue(html.contains("<meta property=\"og:music:song:disc\" content=\"1\" />"))
        assertTrue(html.contains("<meta property=\"og:music:song:track\" content=\"1\" />"))
        assertTrue(html.contains("<meta property=\"og:music:creator\" content=\"Playlist Creator\" />"))
    }

    @Test
    fun `test generate with music radio station`() {
        // Create a Data object with music.radio_station metadata
        val data = Data(
            tags = emptyList(),
            title = "Test Radio Station",
            type = "music.radio_station",
            url = URI("https://example.com/radio").toURL(),
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
            musicRadioStation = MusicRadioStation(
                creator = "Radio Station Creator"
            ),
            videoMovie = null,
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that all music.radio_station tags are generated correctly
        assertTrue(html.contains("<meta property=\"og:music:creator\" content=\"Radio Station Creator\" />"))
    }

    @Test
    fun `test generate with video episode`() {
        // Create a Data object with video.episode metadata
        val data = Data(
            tags = emptyList(),
            title = "Test Episode",
            type = "video.episode",
            url = URI("https://example.com/episode").toURL(),
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
            videoEpisode = VideoEpisode(
                actors = listOf("Actor 1", "Actor 2"),
                director = listOf("Director"),
                writer = listOf("Writer 1", "Writer 2"),
                duration = 45,
                releaseDate = OffsetDateTime.parse("2023-01-01T00:00:00Z"),
                tags = listOf("drama", "comedy"),
                series = "Test Series"
            )
        )

        val html = generator.generate(data)

        // Verify that all video.episode tags are generated correctly
        assertTrue(html.contains("<meta property=\"og:video:actor\" content=\"Actor 1\" />"))
        assertTrue(html.contains("<meta property=\"og:video:actor\" content=\"Actor 2\" />"))
        assertTrue(html.contains("<meta property=\"og:video:director\" content=\"Director\" />"))
        assertTrue(html.contains("<meta property=\"og:video:writer\" content=\"Writer 1\" />"))
        assertTrue(html.contains("<meta property=\"og:video:writer\" content=\"Writer 2\" />"))
        assertTrue(html.contains("<meta property=\"og:video:duration\" content=\"45\" />"))
        assertTrue(html.contains("<meta property=\"og:video:release_date\" content=\"2023-01-01T00:00:00Z\" />"))
        assertTrue(html.contains("<meta property=\"og:video:tag\" content=\"drama\" />"))
        assertTrue(html.contains("<meta property=\"og:video:tag\" content=\"comedy\" />"))
        assertTrue(html.contains("<meta property=\"og:video:series\" content=\"Test Series\" />"))
    }

    @Test
    fun `test generate with null values`() {
        // Create a Data object with null values
        val data = Data(
            tags = emptyList(),
            title = "Test Null Values",
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

        // Verify that only non-null values are included
        assertTrue(html.contains("<meta property=\"og:title\" content=\"Test Null Values\" />"))
        assertTrue(html.contains("<meta property=\"og:type\" content=\"website\" />"))
        assertTrue(html.contains("<meta property=\"og:url\" content=\"https://example.com\" />"))

        // These should not be in the output
        assertEquals(false, html.contains("og:description"))
        assertEquals(false, html.contains("og:site_name"))
        assertEquals(false, html.contains("og:determiner"))
        assertEquals(false, html.contains("og:locale"))
    }

    @Test
    fun `test generate with empty collections`() {
        // Create a Data object with empty collections
        val data = Data(
            tags = emptyList(),
            title = "Test Empty Collections",
            type = "article",
            url = URI("https://example.com").toURL(),
            description = null,
            siteName = null,
            determiner = null,
            locale = null,
            localeAlternate = emptyList(),
            images = emptyList(),
            videos = emptyList(),
            audios = emptyList(),
            article = Article(
                publishedTime = null,
                modifiedTime = null,
                expirationTime = null,
                authors = emptyList(),
                section = null,
                tags = emptyList()
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

        // Verify that empty collections don't generate any tags
        assertTrue(html.contains("<meta property=\"og:title\" content=\"Test Empty Collections\" />"))
        assertTrue(html.contains("<meta property=\"og:type\" content=\"article\" />"))
        assertTrue(html.contains("<meta property=\"og:url\" content=\"https://example.com\" />"))

        // These should not be in the output
        assertEquals(false, html.contains("og:article:published_time"))
        assertEquals(false, html.contains("og:article:modified_time"))
        assertEquals(false, html.contains("og:article:expiration_time"))
        assertEquals(false, html.contains("og:article:author"))
        assertEquals(false, html.contains("og:article:section"))
        assertEquals(false, html.contains("og:article:tag"))
    }

    @Test
    fun `test format date time`() {
        // Create a Data object with date time values
        val data = Data(
            tags = emptyList(),
            title = "Test Date Time",
            type = "article",
            url = URI("https://example.com").toURL(),
            description = null,
            siteName = null,
            determiner = null,
            locale = null,
            localeAlternate = emptyList(),
            images = emptyList(),
            videos = emptyList(),
            audios = emptyList(),
            article = Article(
                publishedTime = OffsetDateTime.parse("2023-01-01T12:34:56+01:00"),
                modifiedTime = OffsetDateTime.parse("2023-01-02T00:00:00Z"),
                expirationTime = OffsetDateTime.parse("2023-12-31T23:59:59-08:00"),
                authors = emptyList(),
                section = null,
                tags = emptyList()
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

        // Verify that date time values are formatted correctly
        // The formatDateTime method converts to UTC/Z time
        assertTrue(html.contains("<meta property=\"og:article:published_time\" content=\"2023-01-01T11:34:56Z\" />"))
        assertTrue(html.contains("<meta property=\"og:article:modified_time\" content=\"2023-01-02T00:00:00Z\" />"))
        assertTrue(html.contains("<meta property=\"og:article:expiration_time\" content=\"2024-01-01T07:59:59Z\" />"))
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
