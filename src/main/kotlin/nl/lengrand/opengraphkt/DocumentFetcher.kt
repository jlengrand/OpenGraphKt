package nl.lengrand.opengraphkt

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

    /**
     * Parses HTML from a file and returns a JSoup Document
     * @param file The file to parse
     * @param charsetName The charset to use for parsing (default is UTF-8)
     * @return A JSoup Document representing the parsed HTML
     */
    fun fromFile(file: java.io.File, charsetName: String = "UTF-8") : Document {
        return Jsoup.parse(file, charsetName)
    }
}
