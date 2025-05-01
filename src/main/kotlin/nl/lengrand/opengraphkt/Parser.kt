package nl.lengrand.opengraphkt

import org.jsoup.nodes.Document

data class OpenGraph(
    val title: String,
    val image: String,
    val description: String? = null,
    val url: String? = null,
    val type: String? = null
)

class Parser {

    /**
     * Extracts Open Graph tags from a JSoup Document
     * Open Graph tags are meta tags with property attributes starting with "og:"
     */
    fun extractOpenGraphTags(document: Document): OpenGraph {
        val ogTags = document.select("meta[property^=og:]")

        println(ogTags)

        // Extract the basic required Open Graph properties
        val title = ogTags.select("meta[property=og:title]").attr("content")
        val image = ogTags.select("meta[property=og:image]").attr("content")
        val description = ogTags.select("meta[property=og:description]").attr("content").takeIf { it.isNotEmpty() }
        val url = ogTags.select("meta[property=og:url]").attr("content").takeIf { it.isNotEmpty() }
        val type = ogTags.select("meta[property=og:type]").attr("content").takeIf { it.isNotEmpty() }

        return OpenGraph(title, image, description, url, type)
    }

}