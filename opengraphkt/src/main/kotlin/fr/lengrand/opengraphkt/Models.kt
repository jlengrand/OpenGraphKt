package fr.lengrand.opengraphkt

/**
 * Enum representing the different types of Open Graph objects.
 */
enum class Type {
    ARTICLE,
    PROFILE,
    BOOK,
    MUSIC_SONG,
    MUSIC_ALBUM,
    MUSIC_PLAYLIST,
    MUSIC_RADIO_STATION,
    VIDEO_MOVIE,
    VIDEO_TV_SHOW,
    VIDEO_OTHER,
    VIDEO_EPISODE,
    WEBSITE,
    UNKNOWN;

    companion object {
        /**
         * Converts a string type to the corresponding enum value.
         *
         * @param type The string representation of the type
         * @return The corresponding Type enum value, or UNKNOWN if not recognized
         */
        fun fromString(type: String?): Type {
            return when (type) {
                "article" -> ARTICLE
                "profile" -> PROFILE
                "book" -> BOOK
                "music.song" -> MUSIC_SONG
                "music.album" -> MUSIC_ALBUM
                "music.playlist" -> MUSIC_PLAYLIST
                "music.radio_station" -> MUSIC_RADIO_STATION
                "video.movie" -> VIDEO_MOVIE
                "video.tv_show" -> VIDEO_TV_SHOW
                "video.other" -> VIDEO_OTHER
                "video.episode" -> VIDEO_EPISODE
                "website" -> WEBSITE
                null -> WEBSITE // No type tag defaults to Website
                else -> UNKNOWN // Another type we are not aware of
            }
        }
    }
}

data class Tag(
    val property: String,
    val content: String,
)

/**
 * Represents structured Open Graph data extracted from HTML.
 */
data class Data(
    val tags: List<Tag>,

    // Basic metadata
    val title: String?,
    val type: String?,
    val url: String?,
    val description: String?,

    // Other metadata
    val siteName: String?,
    val determiner: String?,
    val locale: String?,
    val localeAlternate: List<String>,

    val images: List<Image>,
    val videos: List<Video>,
    val audios: List<Audio>,

    // Optional type-specific metadata
    val article: Article?,
    val profile: Profile?,
    val book: Book?,
    val musicSong: MusicSong?,
    val musicAlbum: MusicAlbum?,
    val musicPlaylist: MusicPlaylist?,
    val musicRadioStation: MusicRadioStation?,
    val videoMovie: VideoMovie?,
    val videoEpisode: VideoEpisode?
) {
    /**
     * Checks if this Open Graph data contains the minimum required properties.
     *
     * According to the Open Graph protocol, the minimum required properties are:
     * - og:title
     * - og:type
     * - og:image
     * - og:url
     *
     * @return true if all required properties are present, false otherwise
     */
    fun isValid(): Boolean {
        return title != null && type != null && images.isNotEmpty() && url != null
    }

    /**
     * Returns the type of this Open Graph data as an enum value.
     *
     * @return The Type enum value corresponding to the type string, or UNKNOWN if the type is not recognized
     */
    fun getType(): Type {
        return Type.fromString(type)
    }
}

data class Image(
    val url: String?,
    val secureUrl: String?,
    val type: String?,
    val width: Int?,
    val height: Int?,
    val alt: String?
)

data class Video(
    val url: String?,
    val secureUrl: String?,
    val type: String?,
    val width: Int?,
    val height: Int?,
    val duration: Int?
)

data class Audio(
    val url: String?,
    val secureUrl: String?,
    val type: String?
)

/**
 * * video.tv_show - same as video.movie
 * * video.other - same as video.movie
 */
data class Article(
    val publishedTime: String?,
    val modifiedTime: String?,
    val expirationTime: String?,
    val section: String?,
    val authors: List<String>,
    val tags: List<String>
)

data class Profile(
    val firstName: String?,
    val lastName: String?,
    val username: String?,
    val gender: String?
)

data class Book(
    val authors: List<String>,
    val isbn: String?,
    val releaseDate: String?,
    val tags: List<String>
)

data class MusicSong(
    val duration: Int?,
    val album: String?,
    val albumDisc: Int?,
    val albumTrack: Int?,
    val musician: List<String>
)

data class MusicAlbum(
    val songs: List<String>,
    val musician: List<String>,
    val releaseDate: String?
)

data class MusicPlaylist(
    val songs: List<String>,
    val creator: String?
)

data class MusicRadioStation(
    val creator: String?
)

data class VideoMovie(
    val actors: List<String>,
    val director: List<String>,
    val writer: List<String>,
    val duration: Int?,
    val releaseDate: String?,
    val tags: List<String>
)

data class VideoEpisode(
    val actors: List<String>,
    val director: List<String>,
    val writer: List<String>,
    val duration: Int?,
    val releaseDate: String?,
    val tags: List<String>,
    val series: String?
)
