package nl.lengrand

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

data class OpenGraph(val title: String, val image: String, val description: String? = null, val url: String? = null, val type: String? = null)

/**
 * Extracts Open Graph tags from a JSoup Document
 * Open Graph tags are meta tags with property attributes starting with "og:"
 */
fun extractOpenGraphTags(document: Document): OpenGraph {
    // Select all meta tags with property attributes starting with "og:"
    val ogTags = document.select("meta[property^=og:]")

    // Extract the basic required Open Graph properties
    val title = ogTags.select("meta[property=og:title]").attr("content")
    val image = ogTags.select("meta[property=og:image]").attr("content")
    val description = ogTags.select("meta[property=og:description]").attr("content").takeIf { it.isNotEmpty() }
    val url = ogTags.select("meta[property=og:url]").attr("content").takeIf { it.isNotEmpty() }
    val type = ogTags.select("meta[property=og:type]").attr("content").takeIf { it.isNotEmpty() }

    return OpenGraph(title, image, description, url, type)
}

/**
 * Prints all Open Graph tags found in a document
 */
fun printAllOpenGraphTags(document: Document) {
    val ogTags = document.select("meta[property^=og:]")
    println("Found ${ogTags.size} Open Graph tags:")

    ogTags.forEach { tag ->
        val property = tag.attr("property")
        val content = tag.attr("content")
        println("$property: $content")
    }
}

fun main() {
    // Wikipedia doesn't have many Open Graph tags, so let's try a site that likely has them
    val doc = Jsoup.connect("https://www.imdb.com/title/tt0068646/").get() // The Godfather movie page
    println("Page title: ${doc.title()}")

    // Print all Open Graph tags
    printAllOpenGraphTags(doc)

    // Extract Open Graph data into our data class
    try {
        val ogData = extractOpenGraphTags(doc)
        println("\nExtracted Open Graph data:")
        println("Title: ${ogData.title}")
        println("Image: ${ogData.image}")
        println("Description: ${ogData.description}")
        println("URL: ${ogData.url}")
        println("Type: ${ogData.type}")
    } catch (e: Exception) {
        println("Error extracting Open Graph data: ${e.message}")
    }
}
