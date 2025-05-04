package nl.lengrand.opengraphkt

import org.jsoup.nodes.Document
import org.jsoup.select.Elements

data class OpenGraphTag(
    val property: String,
    val content: String,
)

data class OpenGraph(

    // Tags can have multiple values for the same property, so we cannot use a Map.
    val rawTags: Elements,
    val tags: List<OpenGraphTag>,

    // Minimal
    val title: String? = null,
    val type: String? = null,
    val image: String? = null, // Do we just take the first here? There might be several
    val url: String? = null,

    // Optional
    val audio: String? = null,
    val description: String? = null,
    val determiner: String? = null,
    val locale: String? = null,
//    val localeAlternate: List<String> = emptyList(),
    val siteName: String? = null,
    val video: String? = null,

    // TODO : Continue with more
){
    /**
     * The very minimum requirement for OpenGraph is a set of 4 properties
     */
    fun isValid(): Boolean {
        return tags.find { it.property == "title" } != null &&
                tags.find { it.property == "url" } != null &&
                tags.find { it.property == "image" } != null &&
                tags.find { it.property == "type" } != null
    }
}

class Parser {

    private fun getTagContent(tags: Elements, tag: String) : String? {
        return if (tags.select("meta[property=og:${tag}]").isEmpty()) null
        else tags.select("meta[property=og:${tag}]").attr("content")
    }

    /**
     * Extracts Open Graph tags from a JSoup Document
     * Open Graph tags are meta tags with property attributes starting with "og:"
     */
    fun extractOpenGraphTags(document: Document): OpenGraph {
        val tags = document.select("meta[property^=og:]")
        val cleanTags = tags.map {
            OpenGraphTag(it.attr("property")
                .drop(3), // Is that completely safe?
                it.attr("content")
            )
        }

        println(tags)
        println(cleanTags)

        // Minimal
        val title = getTagContent(tags, "title")
        val image = getTagContent(tags, "image")
        val url = getTagContent(tags, "url")
        val type = getTagContent(tags, "type")

        // Optional
        val audio = getTagContent(tags, "audio")
        val description = getTagContent(tags, "description")
        val determiner = getTagContent(tags, "determiner")
        val locale = getTagContent(tags, "locale")
        val siteName = getTagContent(tags, "site_name")
        val video = getTagContent(tags, "video")


        return OpenGraph(
            tags,
            cleanTags,
            title,
            type,
            image,
            url,
            audio,
            description,
            determiner,
            locale,
            siteName,
            video
        )
    }

}
