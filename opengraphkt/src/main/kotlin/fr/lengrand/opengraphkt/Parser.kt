package fr.lengrand.opengraphkt

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.File
import java.net.URI
import java.net.URL

/**
 * A comprehensive parser for Open Graph protocol tags.
 * 
 * The Open Graph protocol enables any web page to become a rich object in a social graph.
 * This parser extracts all Open Graph tags from an HTML document and organizes them into
 * a structured format according to the Open Graph protocol specification.
 * 
 * @see <a href="https://ogp.me/">Open Graph Protocol</a>
 */
class Parser {

    /**
     * Extracts all Open Graph tags from a JSoup Document and returns a structured Data object.
     * 
     * @param document The JSoup Document to parse
     * @return An Data object containing all extracted Open Graph data
     */
    fun parse(document: Document): Data {
        val tags = document.select("meta[property^=og:]")
        val openGraphTags = extractOpenGraphTags(tags)

        return buildOpenGraphData(openGraphTags)
    }

    /**
     * Extracts all Open Graph tags from a URL and returns a structured Data object.
     *
     * @param url The URL to be parsed for Open Graph information.
     * @return An Data object containing all extracted Open Graph data.
     */
    fun parse(url: URL) : Data {
        val doc = Jsoup.connect(url.toString()).get()
        return parse(doc)
    }

    /**
     * Extracts all Open Graph tags from a raw HTML String and returns a structured Data object.
     *
     * @param html The raw HTML String to be parsed for Open Graph information.
     * @return An Data object containing all extracted Open Graph data.
     */
    fun parse(html: String) : Data {
        val doc = Jsoup.parse(html)
        return parse(doc)
    }

    /**
     * Extracts all Open Graph tags from a raw HTML String and returns a structured Data object.
     *
     * @param file The file to parse
     * @param charset The charset to use for parsing (default is UTF-8)
     * @return An Data object containing all extracted Open Graph data.
     */
    fun parse(file: File, charset: String = "UTF-8") : Data {
        val doc = Jsoup.parse(file, charset)
        return parse(doc)
    }

    /**
     * Extracts Open Graph tags from JSoup Elements and converts them to Tag objects.
     * 
     * @param elements The JSoup Elements containing Open Graph meta tags
     * @return A list of Tag objects
     */
    private fun extractOpenGraphTags(elements: Elements): List<Tag> {
        return elements.map { element ->
            val fullProperty = element.attr("property")
            val property = fullProperty.substring(3) // Remove "og:" prefix
            val content = element.attr("content")

            Tag(property, content)
        }
    }

    /**
     * Builds an Data object from a list of Tag objects.
     * 
     * @param tags The list of Tag objects
     * @return An Data object containing structured Open Graph data
     */
    private fun buildOpenGraphData(tags: List<Tag>): Data {
        // Group tags by their namespace (before the first colon)
        val groupedTags = tags.groupBy { tag ->
            if (tag.property.contains(":")) {
                val parts = tag.property.split(":", limit = 2)
                parts[0]
            } else {
                tag.property
            }
        }

        // Build basic properties
        val title = getFirstTagContent(tags, "title")
        val type = getFirstTagContent(tags, "type")
        val urlString = getFirstTagContent(tags, "url")
        val url = urlString?.let{URI(urlString).toURL()}
        val description = getFirstTagContent(tags, "description")
        val siteName = getFirstTagContent(tags, "site_name")
        val determiner = getFirstTagContent(tags, "determiner")
        val locale = getFirstTagContent(tags, "locale")
        val localeAlternate = getTagsContent(tags, "locale:alternate")

        // Build structured properties
        val images = buildImages(groupedTags.getOrDefault("image", emptyList()))
        val videos = buildVideos(groupedTags.getOrDefault("video", emptyList()))
        val audios = buildAudios(groupedTags.getOrDefault("audio", emptyList()))

        // Build article-specific properties if the type is "article"
        val article = if (type == "article") buildArticle(groupedTags) else null

        // Build profile-specific properties if the type is "profile"
        val profile = if (type == "profile") buildProfile(groupedTags) else null

        // Build book-specific properties if the type is "book"
        val book = if (type == "book") buildBook(groupedTags) else null

        // Build music-specific properties based on type
        val musicSong = if (type == "music.song") buildMusicSong(groupedTags) else null
        val musicAlbum = if (type == "music.album") buildMusicAlbum(groupedTags) else null
        val musicPlaylist = if (type == "music.playlist") buildMusicPlaylist(groupedTags) else null
        val musicRadioStation = if (type == "music.radio_station") buildMusicRadioStation(groupedTags) else null

        // Build video-specific properties based on type
        val videoMovie = if (type == "video.movie" || type == "video.tv_show" || type == "video.other") buildVideoMovie(groupedTags) else null
        val videoEpisode = if (type == "video.episode") buildVideoEpisode(groupedTags) else null

        return Data(
            tags = tags,
            title = title,
            type = type,
            url = url,
            description = description,
            siteName = siteName,
            determiner = determiner,
            locale = locale,
            localeAlternate = localeAlternate,
            images = images,
            videos = videos,
            audios = audios,
            article = article,
            profile = profile,
            book = book,
            musicSong = musicSong,
            musicAlbum = musicAlbum,
            musicPlaylist = musicPlaylist,
            musicRadioStation = musicRadioStation,
            videoMovie = videoMovie,
            videoEpisode = videoEpisode
        )
    }

    /**
     * Gets the content of the first tag with the specified property.
     * 
     * @param tags The list of Tag objects
     * @param property The property to look for
     * @return The content of the first tag with the specified property, or null if not found
     */
    private fun getFirstTagContent(tags: List<Tag>, property: String): String? {
        return tags.firstOrNull { it.property == property }?.content
    }

    /**
     * Gets the content of all tags with the specified property.
     * 
     * @param tags The list of Tag objects
     * @param property The property to look for
     * @return A list of content values from all tags with the specified property
     */
    private fun getTagsContent(tags: List<Tag>, property: String): List<String> {
        return tags.filter { it.property == property }.map { it.content }
    }

    /**
     * Builds a list of Image objects from image tags.
     * 
     * @param imageTags The list of image-related Tag objects
     * @return A list of Image objects
     */
    private fun buildImages(imageTags: List<Tag>): List<Image> {
        val baseImageTags = imageTags.filter { 
            it.property == "image" || it.property == "image:url" 
        }

        // No images
        if (baseImageTags.isEmpty()) {
            return emptyList()
        }

        val images = mutableListOf<Image>()

        // For each base image tag, create an image object and find its attributes
        baseImageTags.forEach { baseTag ->
            val baseIndex = imageTags.indexOf(baseTag)

            val nextBaseIndex = imageTags.subList(baseIndex + 1, imageTags.size)
                .indexOfFirst { it.property == "image" || it.property == "image:url" }
            val endIndex = if (nextBaseIndex == -1) imageTags.size else baseIndex + 1 + nextBaseIndex

            val attributeTags = imageTags.subList(baseIndex + 1, endIndex)
                .filter { it.property.startsWith("image:") }

            val secureUrl = attributeTags.firstOrNull { it.property == "image:secure_url" }?.content
            val type = attributeTags.firstOrNull { it.property == "image:type" }?.content
            val width = attributeTags.firstOrNull { it.property == "image:width" }?.content?.toIntOrNull()
            val height = attributeTags.firstOrNull { it.property == "image:height" }?.content?.toIntOrNull()
            val alt = attributeTags.firstOrNull { it.property == "image:alt" }?.content

            images.add(Image(
                url = baseTag.content,
                secureUrl = secureUrl,
                type = type,
                width = width,
                height = height,
                alt = alt
            ))
        }

        return images.toList()
    }

    /**
     * Builds a list of Video objects from video tags.
     * 
     * @param videoTags The list of video-related Tag objects
     * @return A list of Video objects
     */
    private fun buildVideos(videoTags: List<Tag>): List<Video> {
        val baseVideoTags = videoTags.filter {
            it.property == "video" || it.property == "video:url" 
        }

        // No videos
        if (baseVideoTags.isEmpty()) {
            return emptyList()
        }

        val videos = mutableListOf<Video>()

        // For each base video tag, create a video object and find its attributes
        baseVideoTags.forEach { baseTag ->
            val baseIndex = videoTags.indexOf(baseTag)

            val nextBaseIndex = videoTags.subList(baseIndex + 1, videoTags.size)
                .indexOfFirst { it.property == "video" || it.property == "video:url" }

            val endIndex = if (nextBaseIndex == -1) videoTags.size else baseIndex + 1 + nextBaseIndex
            val attributeTags = videoTags.subList(baseIndex + 1, endIndex)
                .filter { it.property.startsWith("video:") }

            val secureUrl = attributeTags.firstOrNull { it.property == "video:secure_url" }?.content
            val type = attributeTags.firstOrNull { it.property == "video:type" }?.content
            val width = attributeTags.firstOrNull { it.property == "video:width" }?.content?.toIntOrNull()
            val height = attributeTags.firstOrNull { it.property == "video:height" }?.content?.toIntOrNull()
            val duration = attributeTags.firstOrNull { it.property == "video:duration" }?.content?.toIntOrNull()

            videos.add(Video(
                url = baseTag.content,
                secureUrl = secureUrl,
                type = type,
                width = width,
                height = height,
                duration = duration
            ))
        }

        return videos.toList()
    }

    /**
     * Builds a list of Audio objects from audio tags.
     * 
     * @param audioTags The list of audio-related Tag objects
     * @return A list of Audio objects
     */
    private fun buildAudios(audioTags: List<Tag>): List<Audio> {
        val baseAudioTags = audioTags.filter {
            it.property == "audio" || it.property == "audio:url" 
        }

        // No audio
        if (baseAudioTags.isEmpty()) {
            return emptyList()
        }

        val audios = mutableListOf<Audio>()

        // For each base audio tag, create an audio object and find its attributes
        baseAudioTags.forEach { baseTag ->
            val baseIndex = audioTags.indexOf(baseTag)

            val nextBaseIndex = audioTags.subList(baseIndex + 1, audioTags.size)
                .indexOfFirst { it.property == "audio" || it.property == "audio:url" }

            val endIndex = if (nextBaseIndex == -1) audioTags.size else baseIndex + 1 + nextBaseIndex
            val attributeTags = audioTags.subList(baseIndex + 1, endIndex)
                .filter { it.property.startsWith("audio:") }

            val secureUrl = attributeTags.firstOrNull { it.property == "audio:secure_url" }?.content
            val type = attributeTags.firstOrNull { it.property == "audio:type" }?.content

            audios.add(Audio(
                url = baseTag.content,
                secureUrl = secureUrl,
                type = type
            ))
        }

        return audios.toList()
    }

    /**
     * Builds an Article object from article-related tags.
     * 
     * @param groupedTags The map of grouped Tag objects
     * @return An Article object, or null if no article tags are found
     */
    private fun buildArticle(groupedTags: Map<String, List<Tag>>): Article? {
        val articleTags = groupedTags.getOrDefault("article", emptyList())

        if (articleTags.isEmpty()) {
            return null
        }

        val publishedTime = articleTags.firstOrNull { it.property == "article:published_time" }?.content
        val modifiedTime = articleTags.firstOrNull { it.property == "article:modified_time" }?.content
        val expirationTime = articleTags.firstOrNull { it.property == "article:expiration_time" }?.content
        val section = articleTags.firstOrNull { it.property == "article:section" }?.content
        val authors = articleTags.filter { it.property == "article:author" }.map { it.content }
        val tags = articleTags.filter { it.property == "article:tag" }.map { it.content }

        return Article(
            publishedTime = publishedTime,
            modifiedTime = modifiedTime,
            expirationTime = expirationTime,
            section = section,
            authors = authors,
            tags = tags
        )
    }

    /**
     * Builds an Profile object from profile-related tags.
     * 
     * @param groupedTags The map of grouped Tag objects
     * @return A Profile object, or null if no profile tags are found
     */
    private fun buildProfile(groupedTags: Map<String, List<Tag>>): Profile? {
        val profileTags = groupedTags.getOrDefault("profile", emptyList())

        if (profileTags.isEmpty()) {
            return null
        }

        val firstName = profileTags.firstOrNull { it.property == "profile:first_name" }?.content
        val lastName = profileTags.firstOrNull { it.property == "profile:last_name" }?.content
        val username = profileTags.firstOrNull { it.property == "profile:username" }?.content
        val genderString = profileTags.firstOrNull { it.property == "profile:gender" }?.content
        val gender = genderString?.let(Gender::fromString)

        return Profile(
            firstName = firstName,
            lastName = lastName,
            username = username,
            gender = gender
        )
    }

    /**
     * Builds an Book object from book-related tags.
     * 
     * @param groupedTags The map of grouped Tag objects
     * @return An Book object, or null if no book tags are found
     */
    private fun buildBook(groupedTags: Map<String, List<Tag>>): Book? {
        val bookTags = groupedTags.getOrDefault("book", emptyList())

        if (bookTags.isEmpty()) {
            return null
        }

        val authors = bookTags.filter { it.property == "book:author" }.map { it.content }
        val isbn = bookTags.firstOrNull { it.property == "book:isbn" }?.content
        val releaseDate = bookTags.firstOrNull { it.property == "book:release_date" }?.content
        val tags = bookTags.filter { it.property == "book:tag" }.map { it.content }

        return Book(
            authors = authors,
            isbn = isbn,
            releaseDate = releaseDate,
            tags = tags
        )
    }

    /**
     * Builds an MusicSong object from music.song-related tags.
     * 
     * @param groupedTags The map of grouped Tag objects
     * @return An MusicSong object, or null if no music.song tags are found
     */
    private fun buildMusicSong(groupedTags: Map<String, List<Tag>>): MusicSong? {
        val musicTags = groupedTags.getOrDefault("music", emptyList())

        if (musicTags.isEmpty()) {
            return null
        }

        val duration = musicTags.firstOrNull { it.property == "music:duration" }?.content?.toIntOrNull()
        val album = musicTags.firstOrNull { it.property == "music:album" }?.content
        val albumDisc = musicTags.firstOrNull { it.property == "music:album:disc" }?.content?.toIntOrNull()
        val albumTrack = musicTags.firstOrNull { it.property == "music:album:track" }?.content?.toIntOrNull()
        val musicians = musicTags.filter { it.property == "music:musician" }.map { it.content }

        return MusicSong(
            duration = duration,
            album = album,
            albumDisc = albumDisc,
            albumTrack = albumTrack,
            musician = musicians
        )
    }

    /**
     * Builds an MusicAlbum object from music.album-related tags.
     * 
     * @param groupedTags The map of grouped Tag objects
     * @return An MusicAlbum object, or null if no music.album tags are found
     */
    private fun buildMusicAlbum(groupedTags: Map<String, List<Tag>>): MusicAlbum? {
        val musicTags = groupedTags.getOrDefault("music", emptyList())

        if (musicTags.isEmpty()) {
            return null
        }

        val songs = musicTags.filter { it.property == "music:song" }.map { it.content }
        val songDisc = musicTags.firstOrNull { it.property == "music:song:disc" }?.content?.toIntOrNull()
        val songTrack = musicTags.firstOrNull { it.property == "music:song:track" }?.content?.toIntOrNull()
        val musicians = musicTags.filter { it.property == "music:musician" }.map { it.content }
        val releaseDate = musicTags.firstOrNull { it.property == "music:release_date" }?.content

        return MusicAlbum(
            songs = songs,
            songDisc =  songDisc,
            songTrack = songTrack,
            musician = musicians,
            releaseDate = releaseDate
        )
    }

    /**
     * Builds a MusicPlaylist object from music.playlist-related tags.
     * 
     * @param groupedTags The map of grouped Tag objects
     * @return An MusicPlaylist object, or null if no music.playlist tags are found
     */
    private fun buildMusicPlaylist(groupedTags: Map<String, List<Tag>>): MusicPlaylist? {
        val musicTags = groupedTags.getOrDefault("music", emptyList())

        if (musicTags.isEmpty()) {
            return null
        }

        val songs = musicTags.filter { it.property == "music:song" }.map { it.content }
        val songDisc = musicTags.firstOrNull { it.property == "music:song:disc" }?.content?.toIntOrNull()
        val songTrack = musicTags.firstOrNull { it.property == "music:song:track" }?.content?.toIntOrNull()
        val creator = musicTags.firstOrNull { it.property == "music:creator" }?.content

        return MusicPlaylist(
            songs = songs,
            songDisc =  songDisc,
            songTrack = songTrack,
            creator = creator
        )
    }

    /**
     * Builds a MusicRadioStation object from music.radio_station-related tags.
     * 
     * @param groupedTags The map of grouped Tag objects
     * @return An MusicRadioStation object, or null if no music.radio_station tags are found
     */
    private fun buildMusicRadioStation(groupedTags: Map<String, List<Tag>>): MusicRadioStation? {
        val musicTags = groupedTags.getOrDefault("music", emptyList())

        if (musicTags.isEmpty()) {
            return null
        }

        val creator = musicTags.firstOrNull { it.property == "music:creator" }?.content

        return MusicRadioStation(
            creator = creator
        )
    }

    /**
     * Builds an VideoMovie object from video.movie-related tags.
     * 
     * @param groupedTags The map of grouped Tag objects
     * @return An VideoMovie object, or null if no video.movie tags are found
     */
    private fun buildVideoMovie(groupedTags: Map<String, List<Tag>>): VideoMovie? {
        val videoTags = groupedTags.getOrDefault("video", emptyList())

        if (videoTags.isEmpty()) {
            return null
        }

        val actors = videoTags.filter { it.property == "video:actor" }.map { it.content }
        val directors = videoTags.filter { it.property == "video:director" }.map { it.content }
        val writers = videoTags.filter { it.property == "video:writer" }.map { it.content }
        val duration = videoTags.firstOrNull { it.property == "video:duration" }?.content?.toIntOrNull()
        val releaseDate = videoTags.firstOrNull { it.property == "video:release_date" }?.content
        val tags = videoTags.filter { it.property == "video:tag" }.map { it.content }

        return VideoMovie(
            actors = actors,
            director = directors,
            writer = writers,
            duration = duration,
            releaseDate = releaseDate,
            tags = tags
        )
    }

    /**
     * Builds an VideoEpisode object from video.episode-related tags.
     * 
     * @param groupedTags The map of grouped Tag objects
     * @return An VideoEpisode object, or null if no video.episode tags are found
     */
    private fun buildVideoEpisode(groupedTags: Map<String, List<Tag>>): VideoEpisode? {
        val videoTags = groupedTags.getOrDefault("video", emptyList())

        if (videoTags.isEmpty()) {
            return null
        }

        val actors = videoTags.filter { it.property == "video:actor" }.map { it.content }
        val directors = videoTags.filter { it.property == "video:director" }.map { it.content }
        val writers = videoTags.filter { it.property == "video:writer" }.map { it.content }
        val duration = videoTags.firstOrNull { it.property == "video:duration" }?.content?.toIntOrNull()
        val releaseDate = videoTags.firstOrNull { it.property == "video:release_date" }?.content
        val tags = videoTags.filter { it.property == "video:tag" }.map { it.content }
        val series = videoTags.firstOrNull { it.property == "video:series" }?.content

        return VideoEpisode(
            actors = actors,
            director = directors,
            writer = writers,
            duration = duration,
            releaseDate = releaseDate,
            tags = tags,
            series = series
        )
    }
}
