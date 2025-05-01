package nl.lengrand.opengraphkt.nl.lengrand.opengraphkt

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * DocumentFetcher's job is to take any type of input and transform it into a JSoup document for the Parser to then do its job
 */
class DocumentFetcher {

    fun fromUrl(url: String): Document {
       return Jsoup.connect(url).get()
    }

    fun fromString(html: String): Document {
        return Jsoup.parse(html)
    }

    fun fromFile() : Document {
        TODO()
    }

}