package fr.lengrand.opengraphkt

import org.junit.jupiter.api.Test
import java.net.URI
import java.time.OffsetDateTime
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class GeneratorTest {

    private val generator = Generator()

    @Test
    fun `test generate with empty metadata`() {
        val data = Data(
            tags = emptyList(),
            title = null,
            type = null,
            url = null,
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

        // Verify that all basic metadata tags are generated correctly
        assertFalse(html.contains("<meta property=\"og:title\""))
        assertFalse(html.contains("<meta property=\"og:type\""))
        assertFalse(html.contains("<meta property=\"og:url\""))
    }


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

    @Test
    fun `test generate with custom type`() {
        // Create a Data object with a custom type that doesn't match any specific case in the when statement
        val data = Data(
            tags = emptyList(),
            title = "Custom Type Test",
            type = "custom.type",
            url = URI("https://example.com/custom").toURL(),
            description = "Custom type description",
            siteName = "Test Site",
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

        // Verify that basic metadata is included but no type-specific tags
        assertTrue(html.contains("<meta property=\"og:title\" content=\"Custom Type Test\" />"))
        assertTrue(html.contains("<meta property=\"og:type\" content=\"custom.type\" />"))
        assertTrue(html.contains("<meta property=\"og:url\" content=\"https://example.com/custom\" />"))
        assertTrue(html.contains("<meta property=\"og:description\" content=\"Custom type description\" />"))
        assertTrue(html.contains("<meta property=\"og:site_name\" content=\"Test Site\" />"))

        // Verify that no type-specific tags are included
        assertFalse(html.contains("og:article:"))
        assertFalse(html.contains("og:profile:"))
        assertFalse(html.contains("og:book:"))
        assertFalse(html.contains("og:music:"))
        assertFalse(html.contains("og:video:"))
    }

    @Test
    fun `test partial image properties`() {
        // Create a Data object with images that have some null properties
        val data = Data(
            tags = emptyList(),
            title = "Partial Image Test",
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
                    secureUrl = null,  // Testing null secureUrl
                    type = "image/jpeg",
                    width = null,  // Testing null width
                    height = 600,
                    alt = "Test Image 1"
                ),
                Image(
                    url = "https://example.com/image2.png",
                    secureUrl = "https://secure.example.com/image2.png",
                    type = null,  // Testing null type
                    width = 1024,
                    height = null,  // Testing null height
                    alt = null  // Testing null alt
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

        // Verify basic metadata tags
        assertTrue(html.contains("<meta property=\"og:title\" content=\"Partial Image Test\" />"))
        assertTrue(html.contains("<meta property=\"og:type\" content=\"website\" />"))
        assertTrue(html.contains("<meta property=\"og:url\" content=\"https://example.com\" />"))

        // Verify image1 tags - should have url, type, height, and alt but not secureUrl or width
        assertTrue(html.contains("<meta property=\"og:image\" content=\"https://example.com/image1.jpg\" />"))
        assertTrue(html.contains("<meta property=\"og:image:type\" content=\"image/jpeg\" />"))
        assertTrue(html.contains("<meta property=\"og:image:height\" content=\"600\" />"))
        assertTrue(html.contains("<meta property=\"og:image:alt\" content=\"Test Image 1\" />"))

        // Verify image2 tags - should have url, secureUrl, and width but not type, height, or alt
        assertTrue(html.contains("<meta property=\"og:image\" content=\"https://example.com/image2.png\" />"))
        assertTrue(html.contains("<meta property=\"og:image:secure_url\" content=\"https://secure.example.com/image2.png\" />"))
        assertTrue(html.contains("<meta property=\"og:image:width\" content=\"1024\" />"))

        // Count the total number of image-related tags to ensure no unexpected tags are present
        val expectedImageTagCount = 7 // 2 og:image + 1 secure_url + 1 type + 1 width + 1 height + 1 alt
        val actualImageTagCount = html.split("<meta property=\"og:image").size - 1
        assertEquals(expectedImageTagCount, actualImageTagCount, "Unexpected number of image tags")
    }

    @Test
    fun `test partial video properties`() {
        // Create a Data object with videos that have some null properties
        val data = Data(
            tags = emptyList(),
            title = "Partial Video Test",
            type = "website",
            url = URI("https://example.com").toURL(),
            description = null,
            siteName = null,
            determiner = null,
            locale = null,
            localeAlternate = emptyList(),
            images = emptyList(),
            videos = listOf(
                Video(
                    url = "https://example.com/video1.mp4",
                    secureUrl = null,  // Testing null secureUrl
                    type = "video/mp4",
                    width = null,  // Testing null width
                    height = 720,
                    duration = 120
                ),
                Video(
                    url = "https://example.com/video2.webm",
                    secureUrl = "https://secure.example.com/video2.webm",
                    type = null,  // Testing null type
                    width = 1280,
                    height = null,  // Testing null height
                    duration = null  // Testing null duration
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
            videoMovie = null,
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify basic metadata tags
        assertTrue(html.contains("<meta property=\"og:title\" content=\"Partial Video Test\" />"))
        assertTrue(html.contains("<meta property=\"og:type\" content=\"website\" />"))
        assertTrue(html.contains("<meta property=\"og:url\" content=\"https://example.com\" />"))

        // Verify video1 tags - should have url, type, height, and duration but not secureUrl or width
        assertTrue(html.contains("<meta property=\"og:video\" content=\"https://example.com/video1.mp4\" />"))
        assertTrue(html.contains("<meta property=\"og:video:type\" content=\"video/mp4\" />"))
        assertTrue(html.contains("<meta property=\"og:video:height\" content=\"720\" />"))
        assertTrue(html.contains("<meta property=\"og:video:duration\" content=\"120\" />"))

        // Verify video2 tags - should have url, secureUrl, and width but not type, height, or duration
        assertTrue(html.contains("<meta property=\"og:video\" content=\"https://example.com/video2.webm\" />"))
        assertTrue(html.contains("<meta property=\"og:video:secure_url\" content=\"https://secure.example.com/video2.webm\" />"))
        assertTrue(html.contains("<meta property=\"og:video:width\" content=\"1280\" />"))

        // Count the total number of video-related tags to ensure no unexpected tags are present
        val expectedVideoTagCount = 7 // 2 og:video + 1 secure_url + 1 type + 1 width + 1 height + 1 duration
        val actualVideoTagCount = html.split("<meta property=\"og:video").size - 1
        assertEquals(expectedVideoTagCount, actualVideoTagCount, "Unexpected number of video tags")
    }

    @Test
    fun `test partial audio properties`() {
        // Create a Data object with audios that have some null properties
        val data = Data(
            tags = emptyList(),
            title = "Partial Audio Test",
            type = "website",
            url = URI("https://example.com").toURL(),
            description = null,
            siteName = null,
            determiner = null,
            locale = null,
            localeAlternate = emptyList(),
            images = emptyList(),
            videos = emptyList(),
            audios = listOf(
                Audio(
                    url = "https://example.com/audio1.mp3",
                    secureUrl = null,  // Testing null secureUrl
                    type = "audio/mpeg"
                ),
                Audio(
                    url = "https://example.com/audio2.wav",
                    secureUrl = "https://secure.example.com/audio2.wav",
                    type = null  // Testing null type
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

        // Verify that only non-null audio properties generate tags
        assertTrue(html.contains("<meta property=\"og:audio\" content=\"https://example.com/audio1.mp3\" />"))
        assertFalse(html.contains("og:audio:secure_url\" content=\"https://example.com/audio1.mp3"))
        assertTrue(html.contains("<meta property=\"og:audio:type\" content=\"audio/mpeg\" />"))

        assertTrue(html.contains("<meta property=\"og:audio\" content=\"https://example.com/audio2.wav\" />"))
        assertTrue(html.contains("<meta property=\"og:audio:secure_url\" content=\"https://secure.example.com/audio2.wav\" />"))
        assertFalse(html.contains("og:audio:type\" content=\"\" />"))
    }

    @Test
    fun `test partial article properties`() {
        // Create a Data object with an article that has some null properties
        val data = Data(
            tags = emptyList(),
            title = "Partial Article Test",
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
                modifiedTime = null,  // Testing null modifiedTime
                expirationTime = OffsetDateTime.parse("2023-12-31T23:59:59Z"),
                authors = listOf("Author 1"),
                section = null,  // Testing null section
                tags = listOf("tag1", "tag2")
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

        // Verify that only non-null article properties generate tags
        assertTrue(html.contains("<meta property=\"og:article:published_time\" content=\"2023-01-01T00:00:00Z\" />"))
        assertFalse(html.contains("og:article:modified_time"))
        assertTrue(html.contains("<meta property=\"og:article:expiration_time\" content=\"2023-12-31T23:59:59Z\" />"))
        assertTrue(html.contains("<meta property=\"og:article:author\" content=\"Author 1\" />"))
        assertFalse(html.contains("og:article:section"))
        assertTrue(html.contains("<meta property=\"og:article:tag\" content=\"tag1\" />"))
        assertTrue(html.contains("<meta property=\"og:article:tag\" content=\"tag2\" />"))
    }

    @Test
    fun `test partial profile properties`() {
        // Create a Data object with a profile that has some null properties
        val data = Data(
            tags = emptyList(),
            title = "Partial Profile Test",
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
                lastName = null,  // Testing null lastName
                username = "johndoe",
                gender = null  // Testing null gender
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

        // Verify that only non-null profile properties generate tags
        assertTrue(html.contains("<meta property=\"og:profile:first_name\" content=\"John\" />"))
        assertFalse(html.contains("og:profile:last_name"))
        assertTrue(html.contains("<meta property=\"og:profile:username\" content=\"johndoe\" />"))
        assertFalse(html.contains("og:profile:gender"))
    }

    @Test
    fun `test partial book properties`() {
        // Create a Data object with a book that has some null properties
        val data = Data(
            tags = emptyList(),
            title = "Partial Book Test",
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
                authors = listOf("Author 1"),
                isbn = null,  // Testing null isbn
                releaseDate = OffsetDateTime.parse("2023-01-01T00:00:00Z"),
                tags = emptyList()  // Testing empty tags list
            ),
            musicSong = null,
            musicAlbum = null,
            musicPlaylist = null,
            musicRadioStation = null,
            videoMovie = null,
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that only non-null book properties generate tags
        assertTrue(html.contains("<meta property=\"og:book:author\" content=\"Author 1\" />"))
        assertFalse(html.contains("og:book:isbn"))
        assertTrue(html.contains("<meta property=\"og:book:release_date\" content=\"2023-01-01T00:00:00Z\" />"))
        assertFalse(html.contains("og:book:tag"))
    }

    @Test
    fun `test partial music song properties`() {
        // Create a Data object with a music song that has some null properties
        val data = Data(
            tags = emptyList(),
            title = "Partial Music Song Test",
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
                album = null,  // Testing null album
                albumDisc = 1,
                albumTrack = null,  // Testing null albumTrack
                musician = emptyList()  // Testing empty musician list
            ),
            musicAlbum = null,
            musicPlaylist = null,
            musicRadioStation = null,
            videoMovie = null,
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that only non-null music song properties generate tags
        assertTrue(html.contains("<meta property=\"og:music:duration\" content=\"240\" />"))
        assertFalse(html.contains("<meta property=\"og:music:album\" content="))
        assertTrue(html.contains("<meta property=\"og:music:album:disc\" content=\"1\" />"))
        assertFalse(html.contains("<meta property=\"og:music:album:track\" content="))
        assertFalse(html.contains("<meta property=\"og:music:musician\" content="))
    }

    @Test
    fun `test partial music album properties`() {
        // Create a Data object with a music album that has some null properties
        val data = Data(
            tags = emptyList(),
            title = "Partial Music Album Test",
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
                songs = listOf("Song 1"),
                songDisc = null,  // Testing null songDisc
                songTrack = 1,
                musician = emptyList(),  // Testing empty musician list
                releaseDate = null  // Testing null releaseDate
            ),
            musicPlaylist = null,
            musicRadioStation = null,
            videoMovie = null,
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that only non-null music album properties generate tags
        assertTrue(html.contains("<meta property=\"og:music:song\" content=\"Song 1\" />"))
        assertFalse(html.contains("og:music:song:disc"))
        assertTrue(html.contains("<meta property=\"og:music:song:track\" content=\"1\" />"))
        assertFalse(html.contains("og:music:musician"))
        assertFalse(html.contains("og:music:release_date"))
    }

    @Test
    fun `test partial music playlist properties`() {
        // Create a Data object with a music playlist that has some null properties
        val data = Data(
            tags = emptyList(),
            title = "Partial Music Playlist Test",
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
                songs = emptyList(),  // Testing empty songs list
                songDisc = null,  // Testing null songDisc
                songTrack = null,  // Testing null songTrack
                creator = "Playlist Creator"
            ),
            musicRadioStation = null,
            videoMovie = null,
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that only non-null music playlist properties generate tags
        assertFalse(html.contains("og:music:song"))
        assertFalse(html.contains("og:music:song:disc"))
        assertFalse(html.contains("og:music:song:track"))
        assertTrue(html.contains("<meta property=\"og:music:creator\" content=\"Playlist Creator\" />"))
    }

    @Test
    fun `test partial music radio station properties`() {
        // Create a Data object with a music radio station that has null properties
        val data = Data(
            tags = emptyList(),
            title = "Partial Music Radio Station Test",
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
                creator = null  // Testing null creator
            ),
            videoMovie = null,
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that no music radio station properties generate tags when they're null
        assertFalse(html.contains("og:music:creator"))
    }

    @Test
    fun `test partial video movie properties`() {
        // Create a Data object with a video movie that has some null properties
        val data = Data(
            tags = emptyList(),
            title = "Partial Video Movie Test",
            type = "video.movie",
            url = URI("https://example.com/movie").toURL(),
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
            videoMovie = VideoMovie(
                actors = emptyList(),  // Testing empty actors list
                director = listOf("Director"),
                writer = emptyList(),  // Testing empty writers list
                duration = null,  // Testing null duration
                releaseDate = OffsetDateTime.parse("2023-01-01T00:00:00Z"),
                tags = listOf("action")
            ),
            videoEpisode = null
        )

        val html = generator.generate(data)

        // Verify that only non-null video movie properties generate tags
        assertFalse(html.contains("og:video:actor"))
        assertTrue(html.contains("<meta property=\"og:video:director\" content=\"Director\" />"))
        assertFalse(html.contains("og:video:writer"))
        assertFalse(html.contains("og:video:duration"))
        assertTrue(html.contains("<meta property=\"og:video:release_date\" content=\"2023-01-01T00:00:00Z\" />"))
        assertTrue(html.contains("<meta property=\"og:video:tag\" content=\"action\" />"))
    }

    @Test
    fun `test partial video episode properties`() {
        // Create a Data object with a video episode that has some null properties
        val data = Data(
            tags = emptyList(),
            title = "Partial Video Episode Test",
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
                actors = listOf("Actor 1"),
                director = emptyList(),  // Testing empty directors list
                writer = listOf("Writer 1"),
                duration = 45,
                releaseDate = null,  // Testing null releaseDate
                tags = emptyList(),  // Testing empty tags list
                series = null  // Testing null series
            )
        )

        val html = generator.generate(data)

        // Verify that only non-null video episode properties generate tags
        assertTrue(html.contains("<meta property=\"og:video:actor\" content=\"Actor 1\" />"))
        assertFalse(html.contains("og:video:director"))
        assertTrue(html.contains("<meta property=\"og:video:writer\" content=\"Writer 1\" />"))
        assertTrue(html.contains("<meta property=\"og:video:duration\" content=\"45\" />"))
        assertFalse(html.contains("og:video:release_date"))
        assertFalse(html.contains("og:video:tag"))
        assertFalse(html.contains("og:video:series"))
    }
}
