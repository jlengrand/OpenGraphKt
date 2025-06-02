package fr.lengrand.opengraphkt

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.net.URL
import java.time.OffsetDateTime
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

// Helper function to compare URL objects with String URLs
private fun assertUrlEquals(expected: String, actual: URL?) {
    assertNotNull(actual)
    assertEquals(expected, actual.toString())
}

class ParserTest {

    private val parser = Parser()

    // Sample HTML with all required OpenGraph tags and some structured properties
    private val completeHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Open Graph Example</title>
            <meta property="og:title" content="The Rock" />
            <meta property="og:type" content="video.movie" />
            <meta property="og:url" content="https://example.com/the-rock" />
            <meta property="og:image" content="https://example.com/rock.jpg" />
            <meta property="og:image:secure_url" content="https://secure.example.com/rock.jpg" />
            <meta property="og:image:type" content="image/jpeg" />
            <meta property="og:image:width" content="300" />
            <meta property="og:image:height" content="200" />
            <meta property="og:image:alt" content="A promotional image for The Rock" />
            <meta property="og:description" content="An action movie about a rock" />
            <meta property="og:site_name" content="Example Movies" />
            <meta property="og:locale" content="en_US" />
            <meta property="og:locale:alternate" content="fr_FR" />
            <meta property="og:locale:alternate" content="es_ES" />
            <meta property="og:video" content="https://example.com/rock-trailer.mp4" />
            <meta property="og:video:width" content="1280" />
            <meta property="og:video:height" content="720" />
            <meta property="og:video:type" content="video/mp4" />
            <meta property="og:audio" content="https://example.com/rock-theme.mp3" />
            <meta property="og:audio:type" content="audio/mpeg" />
        </head>
        <body>
            <h1>Example Page</h1>
        </body>
        </html>
    """.trimIndent()

    // Sample HTML with article-specific tags
    private val articleHtml = """
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
            <meta property="og:article:author" content="Jane Smith" />
            <meta property="og:article:tag" content="breaking" />
            <meta property="og:article:tag" content="news" />
        </head>
        <body>
            <h1>Breaking News</h1>
        </body>
        </html>
    """.trimIndent()

    // Sample HTML with profile-specific tags
    private val profileHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Profile Example</title>
            <meta property="og:title" content="John Doe" />
            <meta property="og:type" content="profile" />
            <meta property="og:url" content="https://example.com/profile/johndoe" />
            <meta property="og:image" content="https://example.com/johndoe.jpg" />
            <meta property="og:description" content="John Doe's profile" />
            <meta property="og:profile:first_name" content="John" />
            <meta property="og:profile:last_name" content="Doe" />
            <meta property="og:profile:username" content="johndoe" />
            <meta property="og:profile:gender" content="male" />
        </head>
        <body>
            <h1>John Doe</h1>
        </body>
        </html>
    """.trimIndent()

    // Sample HTML with book-specific tags
    private val bookHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Book Example</title>
            <meta property="og:title" content="The Great Novel" />
            <meta property="og:type" content="book" />
            <meta property="og:url" content="https://example.com/books/great-novel" />
            <meta property="og:image" content="https://example.com/book-cover.jpg" />
            <meta property="og:description" content="A great novel" />
            <meta property="og:book:author" content="Famous Author" />
            <meta property="og:book:isbn" content="1234567890123" />
            <meta property="og:book:release_date" content="2023-01-01" />
            <meta property="og:book:tag" content="fiction" />
            <meta property="og:book:tag" content="novel" />
        </head>
        <body>
            <h1>The Great Novel</h1>
        </body>
        </html>
    """.trimIndent()

    // Sample HTML with multiple images
    private val multipleImagesHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Multiple Images Example</title>
            <meta property="og:title" content="Photo Gallery" />
            <meta property="og:type" content="website" />
            <meta property="og:url" content="https://example.com/gallery" />
            <meta property="og:image" content="https://example.com/image1.jpg" />
            <meta property="og:image:secure_url" content="https://secure.example.com/image1.jpg" />
            <meta property="og:image:type" content="image/jpeg" />
            <meta property="og:image:width" content="800" />
            <meta property="og:image:height" content="600" />
            <meta property="og:image" content="https://example.com/image2.jpg" />
            <meta property="og:image:secure_url" content="https://secure.example.com/image2.jpg" />
            <meta property="og:image:type" content="image/png" />
            <meta property="og:image:width" content="1024" />
            <meta property="og:image:height" content="768" />
            <meta property="og:image" content="https://example.com/image3.jpg" />
            <meta property="og:image:secure_url" content="https://secure.example.com/image3.jpg" />
            <meta property="og:image:type" content="image/gif" />
            <meta property="og:image:width" content="1200" />
            <meta property="og:image:height" content="900" />
            <meta property="og:description" content="A gallery of images" />
        </head>
        <body>
            <h1>Photo Gallery</h1>
        </body>
        </html>
    """.trimIndent()

    @Test
    fun `test parse with complete OpenGraph tags`() {
        val openGraphData = parser.parse(completeHtml)

        // Verify that all required properties are extracted correctly
        assertEquals("The Rock", openGraphData.title)
        assertEquals("video.movie", openGraphData.type)
        assertUrlEquals("https://example.com/the-rock", openGraphData.url)

        // Verify that the OpenGraphData object is valid
        assertTrue(openGraphData.isValid())

        // Verify that all tags are extracted
        assertEquals(20, openGraphData.tags.size)

        // Verify image properties
        assertEquals(1, openGraphData.images.size)
        val image = openGraphData.images[0]
        assertEquals("https://example.com/rock.jpg", image.url)
        assertEquals("https://secure.example.com/rock.jpg", image.secureUrl)
        assertEquals("image/jpeg", image.type)
        assertEquals(300, image.width)
        assertEquals(200, image.height)
        assertEquals("A promotional image for The Rock", image.alt)

        // Verify video properties
        assertEquals(1, openGraphData.videos.size)
        val video = openGraphData.videos[0]
        assertEquals("https://example.com/rock-trailer.mp4", video.url)
        assertEquals(1280, video.width)
        assertEquals(720, video.height)
        assertEquals("video/mp4", video.type)

        // Verify audio properties
        assertEquals(1, openGraphData.audios.size)
        val audio = openGraphData.audios[0]
        assertEquals("https://example.com/rock-theme.mp3", audio.url)
        assertEquals("audio/mpeg", audio.type)

        // Verify locale properties
        assertEquals("en_US", openGraphData.locale)
        assertEquals(2, openGraphData.localeAlternate.size)
        assertTrue(openGraphData.localeAlternate.contains("fr_FR"))
        assertTrue(openGraphData.localeAlternate.contains("es_ES"))

        // Verify video.movie properties
        assertNotNull(openGraphData.videoMovie)
        assertEquals(0, openGraphData.videoMovie.actors.size)
        assertEquals(0, openGraphData.videoMovie.director.size)
        assertEquals(0, openGraphData.videoMovie.writer.size)
        assertEquals(null, openGraphData.videoMovie.duration)
        assertEquals(null, openGraphData.videoMovie.releaseDate)
        assertEquals(0, openGraphData.videoMovie.tags.size)
    }

    @Test
    fun `test parse with article-specific tags`() {
        val openGraphData = parser.parse(articleHtml)

        // Verify basic properties
        assertEquals("Breaking News", openGraphData.title)
        assertEquals("article", openGraphData.type)
        assertUrlEquals("https://example.com/news/breaking", openGraphData.url)
        assertEquals("Latest breaking news", openGraphData.description)

        // Verify article-specific properties
        assertNotNull(openGraphData.article)
        assertNotNull(openGraphData.article.publishedTime)
        assertEquals(OffsetDateTime.parse("2023-01-01T00:00:00Z"), openGraphData.article.publishedTime)
        assertNotNull(openGraphData.article.modifiedTime)
        assertEquals(OffsetDateTime.parse("2023-01-02T12:00:00Z"), openGraphData.article.modifiedTime)
        assertEquals("News", openGraphData.article.section)
        assertEquals(2, openGraphData.article.authors.size)
        assertTrue(openGraphData.article.authors.contains("John Doe"))
        assertTrue(openGraphData.article.authors.contains("Jane Smith"))
        assertEquals(2, openGraphData.article.tags.size)
        assertTrue(openGraphData.article.tags.contains("breaking"))
        assertTrue(openGraphData.article.tags.contains("news"))
    }

    @Test
    fun `test parse with profile-specific tags`() {
        val openGraphData = parser.parse(profileHtml)

        // Verify basic properties
        assertEquals("John Doe", openGraphData.title)
        assertEquals("profile", openGraphData.type)
        assertUrlEquals("https://example.com/profile/johndoe", openGraphData.url)
        assertEquals("John Doe's profile", openGraphData.description)

        // Verify profile-specific properties
        assertNotNull(openGraphData.profile)
        assertEquals("John", openGraphData.profile.firstName)
        assertEquals("Doe", openGraphData.profile.lastName)
        assertEquals("johndoe", openGraphData.profile.username)
        assertEquals(Gender.MALE, openGraphData.profile.gender)
    }

    @Test
    fun `test parse with book-specific tags`() {
        val openGraphData = parser.parse(bookHtml)

        // Verify basic properties
        assertEquals("The Great Novel", openGraphData.title)
        assertEquals("book", openGraphData.type)
        assertUrlEquals("https://example.com/books/great-novel", openGraphData.url)
        assertEquals("A great novel", openGraphData.description)

        // Verify book-specific properties
        assertNotNull(openGraphData.book)
        assertEquals(1, openGraphData.book.authors.size)
        assertEquals("Famous Author", openGraphData.book.authors.get(0))
        assertEquals("1234567890123", openGraphData.book.isbn)
        assertNotNull(openGraphData.book.releaseDate)
        assertEquals(OffsetDateTime.parse("2023-01-01T00:00:00Z"), openGraphData.book.releaseDate)
        assertEquals(2, openGraphData.book.tags.size)
        assertTrue(openGraphData.book.tags.contains("fiction"))
        assertTrue(openGraphData.book.tags.contains("novel"))
    }

    @Test
    fun `test parse with multiple images`() {
        val openGraphData = parser.parse(multipleImagesHtml)

        // Verify basic properties
        assertEquals("Photo Gallery", openGraphData.title)
        assertEquals("website", openGraphData.type)
        assertUrlEquals("https://example.com/gallery", openGraphData.url)
        assertEquals("A gallery of images", openGraphData.description)

        // Verify multiple images
        assertEquals(3, openGraphData.images.size)

        // First image
        assertEquals("https://example.com/image1.jpg", openGraphData.images[0].url)
        assertEquals("https://secure.example.com/image1.jpg", openGraphData.images[0].secureUrl)
        assertEquals("image/jpeg", openGraphData.images[0].type)
        assertEquals(800, openGraphData.images[0].width)
        assertEquals(600, openGraphData.images[0].height)

        // Second image
        assertEquals("https://example.com/image2.jpg", openGraphData.images[1].url)
        assertEquals("https://secure.example.com/image2.jpg", openGraphData.images[1].secureUrl)
        assertEquals("image/png", openGraphData.images[1].type)
        assertEquals(1024, openGraphData.images[1].width)
        assertEquals(768, openGraphData.images[1].height)

        // Third image
        assertEquals("https://example.com/image3.jpg", openGraphData.images[2].url)
        assertEquals("https://secure.example.com/image3.jpg", openGraphData.images[2].secureUrl)
        assertEquals("image/gif", openGraphData.images[2].type)
        assertEquals(1200, openGraphData.images[2].width)
        assertEquals(900, openGraphData.images[2].height)
    }

    @Test
    fun `test parse with File`(@TempDir tempDir: File) {
        // Create a temporary HTML file
        val htmlFile = File(tempDir, "test.html")
        htmlFile.writeText(articleHtml)

        val openGraphData = parser.parse(htmlFile)

        // Verify basic properties
        assertEquals("Breaking News", openGraphData.title)
        assertEquals("article", openGraphData.type)
        assertUrlEquals("https://example.com/news/breaking", openGraphData.url)
        assertEquals("Latest breaking news", openGraphData.description)

        // Verify article-specific properties
        assertNotNull(openGraphData.article)
        assertNotNull(openGraphData.article.publishedTime)
        assertEquals(OffsetDateTime.parse("2023-01-01T00:00:00Z"), openGraphData.article.publishedTime)
        assertNotNull(openGraphData.article.modifiedTime)
        assertEquals(OffsetDateTime.parse("2023-01-02T12:00:00Z"), openGraphData.article.modifiedTime)
        assertEquals("News", openGraphData.article.section)
        assertEquals(2, openGraphData.article.authors.size)
        assertTrue(openGraphData.article.authors.contains("John Doe"))
        assertTrue(openGraphData.article.authors.contains("Jane Smith"))
    }

    // Sample HTML with video.movie-specific tags
    private val videoMovieHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Video Movie Example</title>
            <meta property="og:title" content="The Matrix" />
            <meta property="og:type" content="video.movie" />
            <meta property="og:url" content="https://example.com/movies/the-matrix" />
            <meta property="og:image" content="https://example.com/matrix-poster.jpg" />
            <meta property="og:description" content="A sci-fi action movie" />
            <meta property="og:video:actor" content="Keanu Reeves" />
            <meta property="og:video:actor" content="Laurence Fishburne" />
            <meta property="og:video:director" content="Lana Wachowski" />
            <meta property="og:video:director" content="Lilly Wachowski" />
            <meta property="og:video:writer" content="Lana Wachowski" />
            <meta property="og:video:writer" content="Lilly Wachowski" />
            <meta property="og:video:duration" content="136" />
            <meta property="og:video:release_date" content="1999-03-31" />
            <meta property="og:video:tag" content="sci-fi" />
            <meta property="og:video:tag" content="action" />
        </head>
        <body>
            <h1>The Matrix</h1>
        </body>
        </html>
    """.trimIndent()

    private val noTypeHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Video Movie Example</title>
            <meta property="og:title" content="The Matrix" />
        </head>
        <body>
            <h1>The Matrix</h1>
        </body>
        </html>
    """.trimIndent()

    private val unknownTypeHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Video Movie Example</title>
            <meta property="og:title" content="The Matrix" />
            <meta property="og:type" content="test" />
        </head>
        <body>
            <h1>The Matrix</h1>
        </body>
        </html>
    """.trimIndent()

    @Test
    fun `test parse with video movie-specific tags`() {
        val openGraphData = parser.parse(videoMovieHtml)

        // Verify basic properties
        assertEquals("The Matrix", openGraphData.title)
        assertEquals("video.movie", openGraphData.type)
        assertUrlEquals("https://example.com/movies/the-matrix", openGraphData.url)
        assertEquals("A sci-fi action movie", openGraphData.description)

        // Verify video.movie-specific properties
        assertNotNull(openGraphData.videoMovie)
        assertEquals(2, openGraphData.videoMovie.actors.size)
        assertTrue(openGraphData.videoMovie.actors.contains("Keanu Reeves"))
        assertTrue(openGraphData.videoMovie.actors.contains("Laurence Fishburne"))
        assertEquals(2, openGraphData.videoMovie.director.size)
        assertTrue(openGraphData.videoMovie.director.contains("Lana Wachowski"))
        assertTrue(openGraphData.videoMovie.director.contains("Lilly Wachowski"))
        assertEquals(2, openGraphData.videoMovie.writer.size)
        assertTrue(openGraphData.videoMovie.writer.contains("Lana Wachowski"))
        assertTrue(openGraphData.videoMovie.writer.contains("Lilly Wachowski"))
        assertEquals(136, openGraphData.videoMovie.duration)
        assertNotNull(openGraphData.videoMovie.releaseDate)
        assertEquals(OffsetDateTime.parse("1999-03-31T00:00:00Z"), openGraphData.videoMovie.releaseDate)
        assertEquals(2, openGraphData.videoMovie.tags.size)
        assertTrue(openGraphData.videoMovie.tags.contains("sci-fi"))
        assertTrue(openGraphData.videoMovie.tags.contains("action"))
    }

    // Sample HTML with music.album-specific tags
    private val musicAlbumHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Music Album Example</title>
            <meta property="og:title" content="Greatest Hits" />
            <meta property="og:type" content="music.album" />
            <meta property="og:url" content="https://example.com/albums/greatest-hits" />
            <meta property="og:image" content="https://example.com/album-cover.jpg" />
            <meta property="og:description" content="A collection of greatest hits" />
            <meta property="og:music:song" content="Song 1" />
            <meta property="og:music:song" content="Song 2" />
            <meta property="og:music:song:disc" content="1" />
            <meta property="og:music:song:track" content="1" />
            <meta property="og:music:musician" content="Famous Musician" />
            <meta property="og:music:musician" content="Another Musician" />
            <meta property="og:music:release_date" content="2023-01-15T12:30:00Z" />
        </head>
        <body>
            <h1>Greatest Hits</h1>
        </body>
        </html>
    """.trimIndent()

    @Test
    fun `test parse with music album-specific tags`() {
        val openGraphData = parser.parse(musicAlbumHtml)

        // Verify basic properties
        assertEquals("Greatest Hits", openGraphData.title)
        assertEquals("music.album", openGraphData.type)
        assertUrlEquals("https://example.com/albums/greatest-hits", openGraphData.url)
        assertEquals("A collection of greatest hits", openGraphData.description)

        // Verify music.album-specific properties
        assertNotNull(openGraphData.musicAlbum)
        assertEquals(2, openGraphData.musicAlbum.songs.size)
        assertTrue(openGraphData.musicAlbum.songs.contains("Song 1"))
        assertTrue(openGraphData.musicAlbum.songs.contains("Song 2"))
        assertEquals(1, openGraphData.musicAlbum.songDisc)
        assertEquals(1, openGraphData.musicAlbum.songTrack)
        assertEquals(2, openGraphData.musicAlbum.musician.size)
        assertTrue(openGraphData.musicAlbum.musician.contains("Famous Musician"))
        assertTrue(openGraphData.musicAlbum.musician.contains("Another Musician"))

        // Verify releaseDate is correctly parsed as OffsetDateTime
        assertNotNull(openGraphData.musicAlbum.releaseDate)
        assertEquals(OffsetDateTime.parse("2023-01-15T12:30:00Z"), openGraphData.musicAlbum.releaseDate)
    }

    @Test
    fun `test parse with date-only release date`() {
        // Create a modified version of the music album HTML with a date-only release date
        val dateOnlyHtml = musicAlbumHtml.replace("2023-01-15T12:30:00Z", "2023-01-15")
        val openGraphData = parser.parse(dateOnlyHtml)

        // Verify releaseDate is correctly parsed as OffsetDateTime with default time
        assertNotNull(openGraphData.musicAlbum)
        assertNotNull(openGraphData.musicAlbum.releaseDate)
        assertEquals(OffsetDateTime.parse("2023-01-15T00:00:00Z"), openGraphData.musicAlbum.releaseDate)
    }

    @Test
    fun `test getType method returns correct enum values`() {
        // Test video.movie type
        val videoMovieData = parser.parse(videoMovieHtml)
        assertEquals(Type.VIDEO_MOVIE, videoMovieData.getType())

        // Test article type
        val articleData = parser.parse(articleHtml)
        assertEquals(Type.ARTICLE, articleData.getType())

        // Test profile type
        val profileData = parser.parse(profileHtml)
        assertEquals(Type.PROFILE, profileData.getType())

        // Test book type
        val bookData = parser.parse(bookHtml)
        assertEquals(Type.BOOK, bookData.getType())

        // Test music.album type
        val musicAlbumData = parser.parse(musicAlbumHtml)
        assertEquals(Type.MUSIC_ALBUM, musicAlbumData.getType())

        // Test website type (should return UNKNOWN as it's not in our enum)
        val websiteData = parser.parse(multipleImagesHtml)
        assertEquals(Type.WEBSITE, websiteData.getType())

        // Test no type defaults to Website
        val noTypeData = parser.parse(noTypeHtml)
        assertEquals(Type.WEBSITE, noTypeData.getType())

        // Test unrecognized type is Unknown
        val unkwownData = parser.parse(unknownTypeHtml)
        assertEquals(Type.UNKNOWN, unkwownData.getType())
    }

    // Sample HTML with music.song-specific tags
    private val musicSongHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Music Song Example</title>
            <meta property="og:title" content="Awesome Song" />
            <meta property="og:type" content="music.song" />
            <meta property="og:url" content="https://example.com/songs/awesome-song" />
            <meta property="og:image" content="https://example.com/song-cover.jpg" />
            <meta property="og:description" content="An awesome song" />
            <meta property="og:music:duration" content="240" />
            <meta property="og:music:album" content="Awesome Album" />
            <meta property="og:music:album:disc" content="1" />
            <meta property="og:music:album:track" content="3" />
            <meta property="og:music:musician" content="Awesome Artist" />
            <meta property="og:music:musician" content="Featured Artist" />
        </head>
        <body>
            <h1>Awesome Song</h1>
        </body>
        </html>
    """.trimIndent()

    @Test
    fun `test parse with music song-specific tags`() {
        val openGraphData = parser.parse(musicSongHtml)

        // Verify basic properties
        assertEquals("Awesome Song", openGraphData.title)
        assertEquals("music.song", openGraphData.type)
        assertUrlEquals("https://example.com/songs/awesome-song", openGraphData.url)
        assertEquals("An awesome song", openGraphData.description)

        // Verify music.song-specific properties
        assertNotNull(openGraphData.musicSong)
        assertEquals(240, openGraphData.musicSong.duration)
        assertEquals("Awesome Album", openGraphData.musicSong.album)
        assertEquals(1, openGraphData.musicSong.albumDisc)
        assertEquals(3, openGraphData.musicSong.albumTrack)
        assertEquals(2, openGraphData.musicSong.musician.size)
        assertTrue(openGraphData.musicSong.musician.contains("Awesome Artist"))
        assertTrue(openGraphData.musicSong.musician.contains("Featured Artist"))
    }

    // Sample HTML with music.playlist-specific tags
    private val musicPlaylistHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Music Playlist Example</title>
            <meta property="og:title" content="Awesome Playlist" />
            <meta property="og:type" content="music.playlist" />
            <meta property="og:url" content="https://example.com/playlists/awesome-playlist" />
            <meta property="og:image" content="https://example.com/playlist-cover.jpg" />
            <meta property="og:description" content="An awesome playlist" />
            <meta property="og:music:song" content="Song 1" />
            <meta property="og:music:song" content="Song 2" />
            <meta property="og:music:song" content="Song 3" />
            <meta property="og:music:song:disc" content="1" />
            <meta property="og:music:song:track" content="1" />
            <meta property="og:music:creator" content="Playlist Creator" />
        </head>
        <body>
            <h1>Awesome Playlist</h1>
        </body>
        </html>
    """.trimIndent()

    @Test
    fun `test parse with music playlist-specific tags`() {
        val openGraphData = parser.parse(musicPlaylistHtml)

        // Verify basic properties
        assertEquals("Awesome Playlist", openGraphData.title)
        assertEquals("music.playlist", openGraphData.type)
        assertUrlEquals("https://example.com/playlists/awesome-playlist", openGraphData.url)
        assertEquals("An awesome playlist", openGraphData.description)

        // Verify music.playlist-specific properties
        assertNotNull(openGraphData.musicPlaylist)
        assertEquals(3, openGraphData.musicPlaylist.songs.size)
        assertTrue(openGraphData.musicPlaylist.songs.contains("Song 1"))
        assertTrue(openGraphData.musicPlaylist.songs.contains("Song 2"))
        assertTrue(openGraphData.musicPlaylist.songs.contains("Song 3"))
        assertEquals(1, openGraphData.musicPlaylist.songDisc)
        assertEquals(1, openGraphData.musicPlaylist.songTrack)
        assertEquals("Playlist Creator", openGraphData.musicPlaylist.creator)
    }

    // Sample HTML with music.radio_station-specific tags
    private val musicRadioStationHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Music Radio Station Example</title>
            <meta property="og:title" content="Awesome Radio" />
            <meta property="og:type" content="music.radio_station" />
            <meta property="og:url" content="https://example.com/radio/awesome-radio" />
            <meta property="og:image" content="https://example.com/radio-logo.jpg" />
            <meta property="og:description" content="An awesome radio station" />
            <meta property="og:music:creator" content="Radio Creator" />
        </head>
        <body>
            <h1>Awesome Radio</h1>
        </body>
        </html>
    """.trimIndent()

    @Test
    fun `test parse with music radio station-specific tags`() {
        val openGraphData = parser.parse(musicRadioStationHtml)

        // Verify basic properties
        assertEquals("Awesome Radio", openGraphData.title)
        assertEquals("music.radio_station", openGraphData.type)
        assertUrlEquals("https://example.com/radio/awesome-radio", openGraphData.url)
        assertEquals("An awesome radio station", openGraphData.description)

        // Verify music.radio_station-specific properties
        assertNotNull(openGraphData.musicRadioStation)
        assertEquals("Radio Creator", openGraphData.musicRadioStation.creator)
    }

    // Sample HTML with video.episode-specific tags
    private val videoEpisodeHtml = """
        <!DOCTYPE html>
        <html>
        <head>
            <title>Video Episode Example</title>
            <meta property="og:title" content="Awesome Episode" />
            <meta property="og:type" content="video.episode" />
            <meta property="og:url" content="https://example.com/episodes/awesome-episode" />
            <meta property="og:image" content="https://example.com/episode-thumbnail.jpg" />
            <meta property="og:description" content="An awesome episode" />
            <meta property="og:video:actor" content="Actor 1" />
            <meta property="og:video:actor" content="Actor 2" />
            <meta property="og:video:director" content="Director 1" />
            <meta property="og:video:writer" content="Writer 1" />
            <meta property="og:video:writer" content="Writer 2" />
            <meta property="og:video:duration" content="45" />
            <meta property="og:video:release_date" content="2023-05-15" />
            <meta property="og:video:tag" content="drama" />
            <meta property="og:video:tag" content="comedy" />
            <meta property="og:video:series" content="Awesome Series" />
        </head>
        <body>
            <h1>Awesome Episode</h1>
        </body>
        </html>
    """.trimIndent()

    @Test
    fun `test parse with video episode-specific tags`() {
        val openGraphData = parser.parse(videoEpisodeHtml)

        // Verify basic properties
        assertEquals("Awesome Episode", openGraphData.title)
        assertEquals("video.episode", openGraphData.type)
        assertUrlEquals("https://example.com/episodes/awesome-episode", openGraphData.url)
        assertEquals("An awesome episode", openGraphData.description)

        // Verify video.episode-specific properties
        assertNotNull(openGraphData.videoEpisode)
        assertEquals(2, openGraphData.videoEpisode.actors.size)
        assertTrue(openGraphData.videoEpisode.actors.contains("Actor 1"))
        assertTrue(openGraphData.videoEpisode.actors.contains("Actor 2"))
        assertEquals(1, openGraphData.videoEpisode.director.size)
        assertTrue(openGraphData.videoEpisode.director.contains("Director 1"))
        assertEquals(2, openGraphData.videoEpisode.writer.size)
        assertTrue(openGraphData.videoEpisode.writer.contains("Writer 1"))
        assertTrue(openGraphData.videoEpisode.writer.contains("Writer 2"))
        assertEquals(45, openGraphData.videoEpisode.duration)
        assertNotNull(openGraphData.videoEpisode.releaseDate)
        assertEquals(OffsetDateTime.parse("2023-05-15T00:00:00Z"), openGraphData.videoEpisode.releaseDate)
        assertEquals(2, openGraphData.videoEpisode.tags.size)
        assertTrue(openGraphData.videoEpisode.tags.contains("drama"))
        assertTrue(openGraphData.videoEpisode.tags.contains("comedy"))
        assertEquals("Awesome Series", openGraphData.videoEpisode.series)
    }

    @Test
    fun `test Gender enum toString method`() {
        // Test that the toString method returns the lowercase name of the enum value
        assertEquals("male", Gender.MALE.toString())
        assertEquals("female", Gender.FEMALE.toString())

        // Test that the fromString method correctly converts a string to the enum value
        assertEquals(Gender.MALE, Gender.fromString("MALE"))
        assertEquals(Gender.FEMALE, Gender.fromString("FEMALE"))
    }
}
