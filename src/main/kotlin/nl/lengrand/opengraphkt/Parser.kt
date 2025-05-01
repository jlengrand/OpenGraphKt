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

    val title: String? = null,
    val type: String? = null,
    val image: String? = null, // Do we just take the first here? There might be several
    val url: String? = null,

    // TODO : Continue with more
)

class Parser {

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

        // Extract the basic required Open Graph properties
        val title = tags.select("meta[property=og:title]").attr("content")
        val image = tags.select("meta[property=og:image]").attr("content")
        val url = tags.select("meta[property=og:url]").attr("content")
        val type = tags.select("meta[property=og:type]").attr("content")

        return OpenGraph(
            tags,
            cleanTags,
            title,
            type,
            image,
            url )
    }

}