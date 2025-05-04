package nl.lengrand.opengraphkt

import org.jsoup.nodes.Document
import org.jsoup.select.Elements

/**
 * A comprehensive parser for Open Graph protocol tags.
 * 
 * The Open Graph protocol enables any web page to become a rich object in a social graph.
 * This parser extracts all Open Graph tags from an HTML document and organizes them into
 * a structured format according to the Open Graph protocol specification.
 * 
 * @see <a href="https://ogp.me/">Open Graph Protocol</a>
 */
class OpenGraphParser {

    /**
     * Extracts all Open Graph tags from a JSoup Document and returns a structured OpenGraphData object.
     * 
     * @param document The JSoup Document to parse
     * @return An OpenGraphData object containing all extracted Open Graph data
     */
    fun parse(document: Document): OpenGraphData {
        val tags = document.select("meta[property^=og:]")
        val openGraphTags = extractOpenGraphTags(tags)

        return buildOpenGraphData(openGraphTags)
    }

    /**
     * Extracts Open Graph tags from JSoup Elements and converts them to OpenGraphTag objects.
     * 
     * @param elements The JSoup Elements containing Open Graph meta tags
     * @return A list of OpenGraphTag objects
     */
    private fun extractOpenGraphTags(elements: Elements): List<OpenGraphTag> {
        return elements.map { element ->
            val fullProperty = element.attr("property")
            val property = fullProperty.substring(3) // Remove "og:" prefix
            val content = element.attr("content")

            OpenGraphTag(property, content)
        }
    }

    /**
     * Builds an OpenGraphData object from a list of OpenGraphTag objects.
     * 
     * @param tags The list of OpenGraphTag objects
     * @return An OpenGraphData object containing structured Open Graph data
     */
    private fun buildOpenGraphData(tags: List<OpenGraphTag>): OpenGraphData {
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
        val url = getFirstTagContent(tags, "url")
        val description = getFirstTagContent(tags, "description")
        val siteName = getFirstTagContent(tags, "site_name")
        val determiner = getFirstTagContent(tags, "determiner")
        val locale = getFirstTagContent(tags, "locale")
        val localeAlternate = getTagsContent(tags, "locale:alternate")

        // Build structured properties
        val images = buildImages(groupedTags.getOrDefault("image", emptyList()))
        val videos = buildVideos(groupedTags.getOrDefault("video", emptyList()))
        val audios = buildAudios(groupedTags.getOrDefault("audio", emptyList()))

        // Build article specific properties if type is "article"
        val article = if (type == "article") buildArticle(groupedTags) else null

        // Build profile specific properties if type is "profile"
        val profile = if (type == "profile") buildProfile(groupedTags) else null

        // Build book specific properties if type is "book"
        val book = if (type == "book") buildBook(groupedTags) else null

        return OpenGraphData(
            rawTags = tags,
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
            book = book
        )
    }

    /**
     * Gets the content of the first tag with the specified property.
     * 
     * @param tags The list of OpenGraphTag objects
     * @param property The property to look for
     * @return The content of the first tag with the specified property, or null if not found
     */
    private fun getFirstTagContent(tags: List<OpenGraphTag>, property: String): String? {
        return tags.firstOrNull { it.property == property }?.content
    }

    /**
     * Gets the content of all tags with the specified property.
     * 
     * @param tags The list of OpenGraphTag objects
     * @param property The property to look for
     * @return A list of content values from all tags with the specified property
     */
    private fun getTagsContent(tags: List<OpenGraphTag>, property: String): List<String> {
        return tags.filter { it.property == property }.map { it.content }
    }

    /**
     * Builds a list of OpenGraphImage objects from image tags.
     * 
     * @param imageTags The list of image-related OpenGraphTag objects
     * @return A list of OpenGraphImage objects
     */
    private fun buildImages(imageTags: List<OpenGraphTag>): List<OpenGraphImage> {
        // For multiple images, we need a different approach
        // First, find all base image tags (those with property "image" or "image:url")
        val baseImageTags = imageTags.filter { 
            it.property == "image" || it.property == "image:url" 
        }

        // If we have no base image tags, return an empty list
        if (baseImageTags.isEmpty()) {
            return emptyList()
        }

        // Create a list to hold our image objects
        val images = mutableListOf<OpenGraphImage>()

        // For each base image tag, create an image object and find its attributes
        baseImageTags.forEach { baseTag ->
            // Find the index of this base tag in the original list
            val baseIndex = imageTags.indexOf(baseTag)

            // Find all attribute tags that come after this base tag and before the next base tag
            val nextBaseIndex = imageTags.subList(baseIndex + 1, imageTags.size)
                .indexOfFirst { it.property == "image" || it.property == "image:url" }

            val endIndex = if (nextBaseIndex == -1) imageTags.size else baseIndex + 1 + nextBaseIndex
            val attributeTags = imageTags.subList(baseIndex + 1, endIndex)
                .filter { it.property.startsWith("image:") }

            // Extract attributes
            val secureUrl = attributeTags.firstOrNull { it.property == "image:secure_url" }?.content
            val type = attributeTags.firstOrNull { it.property == "image:type" }?.content
            val width = attributeTags.firstOrNull { it.property == "image:width" }?.content?.toIntOrNull()
            val height = attributeTags.firstOrNull { it.property == "image:height" }?.content?.toIntOrNull()
            val alt = attributeTags.firstOrNull { it.property == "image:alt" }?.content

            // Create the image object
            images.add(OpenGraphImage(
                url = baseTag.content,
                secureUrl = secureUrl,
                type = type,
                width = width,
                height = height,
                alt = alt
            ))
        }

        return images
    }

    /**
     * Builds a list of OpenGraphVideo objects from video tags.
     * 
     * @param videoTags The list of video-related OpenGraphTag objects
     * @return A list of OpenGraphVideo objects
     */
    private fun buildVideos(videoTags: List<OpenGraphTag>): List<OpenGraphVideo> {
        // For multiple videos, we need a different approach
        // First, find all base video tags (those with property "video" or "video:url")
        val baseVideoTags = videoTags.filter { 
            it.property == "video" || it.property == "video:url" 
        }

        // If we have no base video tags, return an empty list
        if (baseVideoTags.isEmpty()) {
            return emptyList()
        }

        // Create a list to hold our video objects
        val videos = mutableListOf<OpenGraphVideo>()

        // For each base video tag, create a video object and find its attributes
        baseVideoTags.forEach { baseTag ->
            // Find the index of this base tag in the original list
            val baseIndex = videoTags.indexOf(baseTag)

            // Find all attribute tags that come after this base tag and before the next base tag
            val nextBaseIndex = videoTags.subList(baseIndex + 1, videoTags.size)
                .indexOfFirst { it.property == "video" || it.property == "video:url" }

            val endIndex = if (nextBaseIndex == -1) videoTags.size else baseIndex + 1 + nextBaseIndex
            val attributeTags = videoTags.subList(baseIndex + 1, endIndex)
                .filter { it.property.startsWith("video:") }

            // Extract attributes
            val secureUrl = attributeTags.firstOrNull { it.property == "video:secure_url" }?.content
            val type = attributeTags.firstOrNull { it.property == "video:type" }?.content
            val width = attributeTags.firstOrNull { it.property == "video:width" }?.content?.toIntOrNull()
            val height = attributeTags.firstOrNull { it.property == "video:height" }?.content?.toIntOrNull()
            val duration = attributeTags.firstOrNull { it.property == "video:duration" }?.content?.toIntOrNull()

            // Create the video object
            videos.add(OpenGraphVideo(
                url = baseTag.content,
                secureUrl = secureUrl,
                type = type,
                width = width,
                height = height,
                duration = duration
            ))
        }

        return videos
    }

    /**
     * Builds a list of OpenGraphAudio objects from audio tags.
     * 
     * @param audioTags The list of audio-related OpenGraphTag objects
     * @return A list of OpenGraphAudio objects
     */
    private fun buildAudios(audioTags: List<OpenGraphTag>): List<OpenGraphAudio> {
        // For multiple audios, we need a different approach
        // First, find all base audio tags (those with property "audio" or "audio:url")
        val baseAudioTags = audioTags.filter { 
            it.property == "audio" || it.property == "audio:url" 
        }

        // If we have no base audio tags, return an empty list
        if (baseAudioTags.isEmpty()) {
            return emptyList()
        }

        // Create a list to hold our audio objects
        val audios = mutableListOf<OpenGraphAudio>()

        // For each base audio tag, create an audio object and find its attributes
        baseAudioTags.forEach { baseTag ->
            // Find the index of this base tag in the original list
            val baseIndex = audioTags.indexOf(baseTag)

            // Find all attribute tags that come after this base tag and before the next base tag
            val nextBaseIndex = audioTags.subList(baseIndex + 1, audioTags.size)
                .indexOfFirst { it.property == "audio" || it.property == "audio:url" }

            val endIndex = if (nextBaseIndex == -1) audioTags.size else baseIndex + 1 + nextBaseIndex
            val attributeTags = audioTags.subList(baseIndex + 1, endIndex)
                .filter { it.property.startsWith("audio:") }

            // Extract attributes
            val secureUrl = attributeTags.firstOrNull { it.property == "audio:secure_url" }?.content
            val type = attributeTags.firstOrNull { it.property == "audio:type" }?.content

            // Create the audio object
            audios.add(OpenGraphAudio(
                url = baseTag.content,
                secureUrl = secureUrl,
                type = type
            ))
        }

        return audios
    }

    /**
     * Builds an OpenGraphArticle object from article-related tags.
     * 
     * @param groupedTags The map of grouped OpenGraphTag objects
     * @return An OpenGraphArticle object, or null if no article tags are found
     */
    private fun buildArticle(groupedTags: Map<String, List<OpenGraphTag>>): OpenGraphArticle? {
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

        return OpenGraphArticle(
            publishedTime = publishedTime,
            modifiedTime = modifiedTime,
            expirationTime = expirationTime,
            section = section,
            authors = authors,
            tags = tags
        )
    }

    /**
     * Builds an OpenGraphProfile object from profile-related tags.
     * 
     * @param groupedTags The map of grouped OpenGraphTag objects
     * @return An OpenGraphProfile object, or null if no profile tags are found
     */
    private fun buildProfile(groupedTags: Map<String, List<OpenGraphTag>>): OpenGraphProfile? {
        val profileTags = groupedTags.getOrDefault("profile", emptyList())

        if (profileTags.isEmpty()) {
            return null
        }

        val firstName = profileTags.firstOrNull { it.property == "profile:first_name" }?.content
        val lastName = profileTags.firstOrNull { it.property == "profile:last_name" }?.content
        val username = profileTags.firstOrNull { it.property == "profile:username" }?.content
        val gender = profileTags.firstOrNull { it.property == "profile:gender" }?.content

        return OpenGraphProfile(
            firstName = firstName,
            lastName = lastName,
            username = username,
            gender = gender
        )
    }

    /**
     * Builds an OpenGraphBook object from book-related tags.
     * 
     * @param groupedTags The map of grouped OpenGraphTag objects
     * @return An OpenGraphBook object, or null if no book tags are found
     */
    private fun buildBook(groupedTags: Map<String, List<OpenGraphTag>>): OpenGraphBook? {
        val bookTags = groupedTags.getOrDefault("book", emptyList())

        if (bookTags.isEmpty()) {
            return null
        }

        val authors = bookTags.filter { it.property == "book:author" }.map { it.content }
        val isbn = bookTags.firstOrNull { it.property == "book:isbn" }?.content
        val releaseDate = bookTags.firstOrNull { it.property == "book:release_date" }?.content
        val tags = bookTags.filter { it.property == "book:tag" }.map { it.content }

        return OpenGraphBook(
            authors = authors,
            isbn = isbn,
            releaseDate = releaseDate,
            tags = tags
        )
    }

    /**
     * Groups structured tags (like image:width, image:height) by their index.
     * 
     * @param tags The list of structured OpenGraphTag objects
     * @return A map of index to list of tags
     */
    private fun groupStructuredTags(tags: List<OpenGraphTag>): Map<Int, List<OpenGraphTag>> {
        // If there are no tags, return an empty map
        if (tags.isEmpty()) {
            return emptyMap()
        }

        // If there's only one item with no index, return it as index 0
        if (tags.size == 1 && !tags[0].property.contains(":")) {
            return mapOf(0 to tags)
        }

        // For multiple images/videos/audios, we need to handle them differently
        // First, identify the base properties (image, video, audio) without any additional attributes
        val baseTags = tags.filter { 
            !it.property.contains(":") || 
            it.property.endsWith(":url") 
        }

        // If we have multiple base tags, we need to create separate groups for each
        if (baseTags.size > 1) {
            val result = mutableMapOf<Int, MutableList<OpenGraphTag>>()

            // Add each base tag as a separate group
            baseTags.forEachIndexed { index, baseTag ->
                result[index] = mutableListOf(baseTag)
            }

            // Now distribute the attribute tags to the appropriate base tag
            // For simplicity, we'll assign attributes to the nearest preceding base tag
            val attributeTags = tags.filter { 
                it.property.contains(":") && 
                !it.property.endsWith(":url") 
            }

            // Group attribute tags by their base property (before the first colon)
            val groupedAttributeTags = attributeTags.groupBy { tag ->
                tag.property.split(":", limit = 2)[0]
            }

            // For each base property, find all its attributes and distribute them
            groupedAttributeTags.forEach { (baseProperty, attributes) ->
                // Find all base tags with this property
                val baseIndices = baseTags.mapIndexedNotNull { index, tag -> 
                    if (tag.property == baseProperty || tag.property == "$baseProperty:url") index else null 
                }

                // If we have explicit indices in the attributes, use them
                val indexedAttributes = attributes.filter { it.property.matches(Regex(".*:\\d+:.*")) }
                    .groupBy { tag ->
                        val regex = Regex(".*:(\\d+):.*")
                        val matchResult = regex.find(tag.property)
                        matchResult?.groupValues?.get(1)?.toIntOrNull() ?: 0
                    }

                // Add indexed attributes to the appropriate base tag
                indexedAttributes.forEach { (attrIndex, attrs) ->
                    if (attrIndex < baseIndices.size) {
                        result[baseIndices[attrIndex]]?.addAll(attrs) ?: run {
                            result[baseIndices[attrIndex]] = attrs.toMutableList()
                        }
                    }
                }

                // Handle non-indexed attributes
                val nonIndexedAttributes = attributes.filter { !it.property.matches(Regex(".*:\\d+:.*")) }

                // Distribute non-indexed attributes to all base tags of this type
                // For width, height, etc. that should apply to a specific image, this is not ideal,
                // but without explicit indices, we can't know which attribute belongs to which base tag
                baseIndices.forEachIndexed { i, baseIndex ->
                    // For the first base tag, add all non-indexed attributes
                    // For subsequent base tags, only add attributes that make sense to duplicate
                    if (i == 0 || nonIndexedAttributes.none { it.property.contains("width") || it.property.contains("height") }) {
                        result[baseIndex]?.addAll(nonIndexedAttributes) ?: run {
                            result[baseIndex] = nonIndexedAttributes.toMutableList()
                        }
                    }
                }
            }

            return result
        }

        // If we only have one base tag or no base tags, fall back to the original logic
        // Group tags by their explicit index if available
        val indexedTags = tags.filter { it.property.matches(Regex(".*:\\d+:.*")) }
            .groupBy { tag ->
                val regex = Regex(".*:(\\d+):.*")
                val matchResult = regex.find(tag.property)
                matchResult?.groupValues?.get(1)?.toIntOrNull() ?: 0
            }

        // Handle tags without explicit index
        val nonIndexedTags = tags.filter { !it.property.matches(Regex(".*:\\d+:.*")) }

        // If we have indexed tags, merge non-indexed tags with index 0
        if (indexedTags.isNotEmpty()) {
            val result = indexedTags.toMutableMap()
            if (nonIndexedTags.isNotEmpty()) {
                result[0] = (result[0] ?: emptyList()) + nonIndexedTags
            }
            return result
        }

        // If we only have non-indexed tags, treat them as a single item
        return mapOf(0 to nonIndexedTags)
    }
}

// Using the existing OpenGraphTag class from Parser.kt

/**
 * Represents structured Open Graph data extracted from HTML.
 */
data class OpenGraphData(
    val rawTags: List<OpenGraphTag>,

    // Basic metadata
    val title: String?,
    val type: String?,
    val url: String?,
    val description: String?,
    val siteName: String?,
    val determiner: String?,
    val locale: String?,
    val localeAlternate: List<String>,

    // Structured properties
    val images: List<OpenGraphImage>,
    val videos: List<OpenGraphVideo>,
    val audios: List<OpenGraphAudio>,

    // Optional type-specific metadata
    val article: OpenGraphArticle?,
    val profile: OpenGraphProfile?,
    val book: OpenGraphBook?
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
     * Gets the first image URL, or null if no images are present.
     * 
     * @return The URL of the first image, or null
     */
    fun getFirstImageUrl(): String? {
        return images.firstOrNull()?.url
    }
}

/**
 * Represents an Open Graph image.
 */
data class OpenGraphImage(
    val url: String?,
    val secureUrl: String?,
    val type: String?,
    val width: Int?,
    val height: Int?,
    val alt: String?
)

/**
 * Represents an Open Graph video.
 */
data class OpenGraphVideo(
    val url: String?,
    val secureUrl: String?,
    val type: String?,
    val width: Int?,
    val height: Int?,
    val duration: Int?
)

/**
 * Represents an Open Graph audio.
 */
data class OpenGraphAudio(
    val url: String?,
    val secureUrl: String?,
    val type: String?
)

/**
 * Represents Open Graph article metadata.
 */
data class OpenGraphArticle(
    val publishedTime: String?,
    val modifiedTime: String?,
    val expirationTime: String?,
    val section: String?,
    val authors: List<String>,
    val tags: List<String>
)

/**
 * Represents Open Graph profile metadata.
 */
data class OpenGraphProfile(
    val firstName: String?,
    val lastName: String?,
    val username: String?,
    val gender: String?
)

/**
 * Represents Open Graph book metadata.
 */
data class OpenGraphBook(
    val authors: List<String>,
    val isbn: String?,
    val releaseDate: String?,
    val tags: List<String>
)
