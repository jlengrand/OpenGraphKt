package fr.lengrand.opengraphkt

import java.time.OffsetDateTime

/**
 * A generator for Open Graph protocol HTML meta tags.
 * 
 * This class converts an OpenGraph Data object into HTML meta tags according to the Open Graph protocol specification.
 * It can be used to generate the appropriate meta tags for embedding in HTML documents.
 * 
 * @see <a href="https://ogp.me/">Open Graph Protocol</a>
 */
class Generator {

    /**
     * Generates HTML meta tags from an OpenGraph Data object.
     * 
     * @param data The OpenGraph Data object to convert to HTML meta tags
     * @return A string containing the HTML meta tags
     */
    fun generate(data: Data): String {
        val tags = mutableListOf<String>()

        // Add basic metadata tags
        addBasicMetaTags(data, tags)

        // Add image tags
        addImageTags(data.images, tags)

        // Add video tags
        addVideoTags(data.videos, tags)

        // Add audio tags
        addAudioTags(data.audios, tags)

        // Add type-specific tags
        when (data.getType()) {
            Type.ARTICLE -> addArticleTags(data.article, tags)
            Type.PROFILE -> addProfileTags(data.profile, tags)
            Type.BOOK -> addBookTags(data.book, tags)
            Type.MUSIC_SONG -> addMusicSongTags(data.musicSong, tags)
            Type.MUSIC_ALBUM -> addMusicAlbumTags(data.musicAlbum, tags)
            Type.MUSIC_PLAYLIST -> addMusicPlaylistTags(data.musicPlaylist, tags)
            Type.MUSIC_RADIO_STATION -> addMusicRadioStationTags(data.musicRadioStation, tags)
            Type.VIDEO_MOVIE, Type.VIDEO_TV_SHOW, Type.VIDEO_OTHER -> addVideoMovieTags(data.videoMovie, tags)
            Type.VIDEO_EPISODE -> addVideoEpisodeTags(data.videoEpisode, tags)
            else -> { /* No additional tags for other types */ }
        }

        return tags.joinToString("\n")
    }

    /**
     * Adds basic Open Graph meta tags to the list.
     * 
     * @param data The OpenGraph Data object
     * @param tags The list to add the tags to
     */
    private fun addBasicMetaTags(data: Data, tags: MutableList<String>) {
        // Required properties
        data.title?.let { tags.add(createMetaTag("og:title", it)) }
        data.type?.let { tags.add(createMetaTag("og:type", it)) }
        data.url?.let { tags.add(createMetaTag("og:url", it.toString())) }

        // Optional properties
        data.description?.let { tags.add(createMetaTag("og:description", it)) }
        data.siteName?.let { tags.add(createMetaTag("og:site_name", it)) }
        data.determiner?.let { tags.add(createMetaTag("og:determiner", it)) }
        data.locale?.let { tags.add(createMetaTag("og:locale", it)) }

        // Locale alternates
        data.localeAlternate.forEach { locale ->
            tags.add(createMetaTag("og:locale:alternate", locale))
        }
    }

    /**
     * Adds image meta tags to the list.
     * 
     * @param images The list of Image objects
     * @param tags The list to add the tags to
     */
    private fun addImageTags(images: List<Image>, tags: MutableList<String>) {
        images.forEach { image ->
            image.url?.let { tags.add(createMetaTag("og:image", it)) }
            image.secureUrl?.let { tags.add(createMetaTag("og:image:secure_url", it)) }
            image.type?.let { tags.add(createMetaTag("og:image:type", it)) }
            image.width?.let { tags.add(createMetaTag("og:image:width", it.toString())) }
            image.height?.let { tags.add(createMetaTag("og:image:height", it.toString())) }
            image.alt?.let { tags.add(createMetaTag("og:image:alt", it)) }
        }
    }

    /**
     * Adds video meta tags to the list.
     * 
     * @param videos The list of Video objects
     * @param tags The list to add the tags to
     */
    private fun addVideoTags(videos: List<Video>, tags: MutableList<String>) {
        videos.forEach { video ->
            video.url?.let { tags.add(createMetaTag("og:video", it)) }
            video.secureUrl?.let { tags.add(createMetaTag("og:video:secure_url", it)) }
            video.type?.let { tags.add(createMetaTag("og:video:type", it)) }
            video.width?.let { tags.add(createMetaTag("og:video:width", it.toString())) }
            video.height?.let { tags.add(createMetaTag("og:video:height", it.toString())) }
            video.duration?.let { tags.add(createMetaTag("og:video:duration", it.toString())) }
        }
    }

    /**
     * Adds audio meta tags to the list.
     * 
     * @param audios The list of Audio objects
     * @param tags The list to add the tags to
     */
    private fun addAudioTags(audios: List<Audio>, tags: MutableList<String>) {
        audios.forEach { audio ->
            audio.url?.let { tags.add(createMetaTag("og:audio", it)) }
            audio.secureUrl?.let { tags.add(createMetaTag("og:audio:secure_url", it)) }
            audio.type?.let { tags.add(createMetaTag("og:audio:type", it)) }
        }
    }

    /**
     * Adds article-specific meta tags to the list.
     * 
     * @param article The Article object
     * @param tags The list to add the tags to
     */
    private fun addArticleTags(article: Article?, tags: MutableList<String>) {
        if (article == null) return

        article.publishedTime?.let { tags.add(createMetaTag("og:article:published_time", formatDateTime(it))) }
        article.modifiedTime?.let { tags.add(createMetaTag("og:article:modified_time", formatDateTime(it))) }
        article.expirationTime?.let { tags.add(createMetaTag("og:article:expiration_time", formatDateTime(it))) }
        article.section?.let { tags.add(createMetaTag("og:article:section", it)) }

        article.authors.forEach { author ->
            tags.add(createMetaTag("og:article:author", author))
        }

        article.tags.forEach { tag ->
            tags.add(createMetaTag("og:article:tag", tag))
        }
    }

    /**
     * Adds profile-specific meta tags to the list.
     * 
     * @param profile The Profile object
     * @param tags The list to add the tags to
     */
    private fun addProfileTags(profile: Profile?, tags: MutableList<String>) {
        if (profile == null) return

        profile.firstName?.let { tags.add(createMetaTag("og:profile:first_name", it)) }
        profile.lastName?.let { tags.add(createMetaTag("og:profile:last_name", it)) }
        profile.username?.let { tags.add(createMetaTag("og:profile:username", it)) }
        profile.gender?.let { tags.add(createMetaTag("og:profile:gender", it.toString())) }
    }

    /**
     * Adds book-specific meta tags to the list.
     * 
     * @param book The Book object
     * @param tags The list to add the tags to
     */
    private fun addBookTags(book: Book?, tags: MutableList<String>) {
        if (book == null) return

        book.authors.forEach { author ->
            tags.add(createMetaTag("og:book:author", author))
        }

        book.isbn?.let { tags.add(createMetaTag("og:book:isbn", it)) }
        book.releaseDate?.let { tags.add(createMetaTag("og:book:release_date", formatDateTime(it))) }

        book.tags.forEach { tag ->
            tags.add(createMetaTag("og:book:tag", tag))
        }
    }

    /**
     * Adds music.song-specific meta tags to the list.
     * 
     * @param musicSong The MusicSong object
     * @param tags The list to add the tags to
     */
    private fun addMusicSongTags(musicSong: MusicSong?, tags: MutableList<String>) {
        if (musicSong == null) return

        musicSong.duration?.let { tags.add(createMetaTag("og:music:duration", it.toString())) }
        musicSong.album?.let { tags.add(createMetaTag("og:music:album", it)) }
        musicSong.albumDisc?.let { tags.add(createMetaTag("og:music:album:disc", it.toString())) }
        musicSong.albumTrack?.let { tags.add(createMetaTag("og:music:album:track", it.toString())) }

        musicSong.musician.forEach { musician ->
            tags.add(createMetaTag("og:music:musician", musician))
        }
    }

    /**
     * Adds music.album-specific meta tags to the list.
     * 
     * @param musicAlbum The MusicAlbum object
     * @param tags The list to add the tags to
     */
    private fun addMusicAlbumTags(musicAlbum: MusicAlbum?, tags: MutableList<String>) {
        if (musicAlbum == null) return

        musicAlbum.songs.forEach { song ->
            tags.add(createMetaTag("og:music:song", song))
        }

        musicAlbum.songDisc?.let { tags.add(createMetaTag("og:music:song:disc", it.toString())) }
        musicAlbum.songTrack?.let { tags.add(createMetaTag("og:music:song:track", it.toString())) }

        musicAlbum.musician.forEach { musician ->
            tags.add(createMetaTag("og:music:musician", musician))
        }

        musicAlbum.releaseDate?.let { tags.add(createMetaTag("og:music:release_date", formatDateTime(it))) }
    }

    /**
     * Adds music.playlist-specific meta tags to the list.
     * 
     * @param musicPlaylist The MusicPlaylist object
     * @param tags The list to add the tags to
     */
    private fun addMusicPlaylistTags(musicPlaylist: MusicPlaylist?, tags: MutableList<String>) {
        if (musicPlaylist == null) return

        musicPlaylist.songs.forEach { song ->
            tags.add(createMetaTag("og:music:song", song))
        }

        musicPlaylist.songDisc?.let { tags.add(createMetaTag("og:music:song:disc", it.toString())) }
        musicPlaylist.songTrack?.let { tags.add(createMetaTag("og:music:song:track", it.toString())) }
        musicPlaylist.creator?.let { tags.add(createMetaTag("og:music:creator", it)) }
    }

    /**
     * Adds music.radio_station-specific meta tags to the list.
     * 
     * @param musicRadioStation The MusicRadioStation object
     * @param tags The list to add the tags to
     */
    private fun addMusicRadioStationTags(musicRadioStation: MusicRadioStation?, tags: MutableList<String>) {
        if (musicRadioStation == null) return

        musicRadioStation.creator?.let { tags.add(createMetaTag("og:music:creator", it)) }
    }

    /**
     * Adds video.movie-specific meta tags to the list.
     * 
     * @param videoMovie The VideoMovie object
     * @param tags The list to add the tags to
     */
    private fun addVideoMovieTags(videoMovie: VideoMovie?, tags: MutableList<String>) {
        if (videoMovie == null) return

        videoMovie.actors.forEach { actor ->
            tags.add(createMetaTag("og:video:actor", actor))
        }

        videoMovie.director.forEach { director ->
            tags.add(createMetaTag("og:video:director", director))
        }

        videoMovie.writer.forEach { writer ->
            tags.add(createMetaTag("og:video:writer", writer))
        }

        videoMovie.duration?.let { tags.add(createMetaTag("og:video:duration", it.toString())) }
        videoMovie.releaseDate?.let { tags.add(createMetaTag("og:video:release_date", formatDateTime(it))) }

        videoMovie.tags.forEach { tag ->
            tags.add(createMetaTag("og:video:tag", tag))
        }
    }

    /**
     * Adds video.episode-specific meta tags to the list.
     * 
     * @param videoEpisode The VideoEpisode object
     * @param tags The list to add the tags to
     */
    private fun addVideoEpisodeTags(videoEpisode: VideoEpisode?, tags: MutableList<String>) {
        if (videoEpisode == null) return

        videoEpisode.actors.forEach { actor ->
            tags.add(createMetaTag("og:video:actor", actor))
        }

        videoEpisode.director.forEach { director ->
            tags.add(createMetaTag("og:video:director", director))
        }

        videoEpisode.writer.forEach { writer ->
            tags.add(createMetaTag("og:video:writer", writer))
        }

        videoEpisode.duration?.let { tags.add(createMetaTag("og:video:duration", it.toString())) }
        videoEpisode.releaseDate?.let { tags.add(createMetaTag("og:video:release_date", formatDateTime(it))) }

        videoEpisode.tags.forEach { tag ->
            tags.add(createMetaTag("og:video:tag", tag))
        }

        videoEpisode.series?.let { tags.add(createMetaTag("og:video:series", it)) }
    }

    /**
     * Creates an HTML meta tag with the given property and content.
     * 
     * @param property The property attribute value
     * @param content The content attribute value
     * @return The HTML meta tag string
     */
    private fun createMetaTag(property: String, content: String): String {
        val escapedContent = content.replace("\"", "&quot;")
        return "<meta property=\"$property\" content=\"$escapedContent\" />"
    }

    /**
     * Formats an OffsetDateTime to a string suitable for OpenGraph tags.
     * 
     * @param dateTime The OffsetDateTime to format
     * @return The formatted date string in ISO-8601 format with 'Z' timezone indicator
     */
    private fun formatDateTime(dateTime: OffsetDateTime): String {
        return dateTime.toInstant().toString()
    }
}
