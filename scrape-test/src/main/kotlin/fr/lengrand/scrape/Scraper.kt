package fr.lengrand.scrape

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.BrowserUserAgent
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

data class Website(
    val id: Int,
    val url: String,
    val description: String,
    val category: String,
)

// Process starts in main repo folder, not the module itself
val dataFile = "./scrape-test/data/website_classification.csv"
val siteDirectory = "./scrape-test/data/web"

/**
 * Bulk webpage scraper using Ktor to efficiently download HTML from multiple URLs in parallel
 * and save the content to individual files.
 */
@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class WebScraper(
    private val outputDirectory: String = "scrape-test/data/web",
    private val concurrencyLevel: Int = 20,
    private val requestTimeoutMillis: Long = 30000
){
    private val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = this@WebScraper.requestTimeoutMillis
        }
        BrowserUserAgent()
    }

    private val completedCount = AtomicInteger(0)
    private val failedCount = AtomicInteger(0)
    private val totalCount = AtomicInteger(0)

    init {
        File(outputDirectory).mkdirs()
    }

    /**
     * Scrapes a single webpage and saves it to a file.
     *
     * @param url The URL to scrape
     * @param outputFilePath The file path to save the HTML content
     */
    private suspend fun scrapeWebpage(url: String, outputFilePath: String) {
        try {
            val response: HttpResponse = client.get(url)
            val htmlContent = response.bodyAsText()
            File(outputFilePath).writeText(htmlContent)

            println("[${completedCount.incrementAndGet()}/${totalCount.get()}] Successfully scraped: $url")
        } catch (e: Exception) {
            println("[${failedCount.incrementAndGet()}/${totalCount.get()}] Failed to scrape $url: ${e.message}")
        }
    }

    /**
     * Scrapes multiple webpages in parallel and saves them to files.
     *
     * @param urls List of URLs to scrape
     */
    suspend fun scrapeWebpages(urls: List<String>) {
        totalCount.set(urls.size)
        completedCount.set(0)
        failedCount.set(0)

        println("Starting to scrape ${urls.size} URLs with concurrency level: $concurrencyLevel")

        // Create a coroutine dispatcher with a fixed thread pool
        val dispatcher = Dispatchers.IO.limitedParallelism(concurrencyLevel)

        withContext(dispatcher) {
            urls.mapIndexed { index, url ->
                val filename = sanitizeURls(url)
                val outputPath = "$outputDirectory/$filename"

                // Launch a coroutine for each URL
                launch {
                    scrapeWebpage(url, outputPath)
                }
            }.joinAll() // Wait for all coroutines to complete
        }

        println("Scraping completed. Total: ${urls.size}, Successful: ${completedCount.get()}, Failed: ${failedCount.get()}")
    }

    /**
     * Generates a safe filename from a URL.
     */
    fun sanitizeURls(url: String): String {
        val sanitizedUrl = url
            .replace(Regex("^https?://"), "")
            .replace(Regex("[^a-zA-Z0-9.-]"), "_")

        return sanitizedUrl
    }

    /**
     * Closes the HTTP client and releases resources.
     */
    fun close() {
        client.close()
    }

}

suspend fun main(){

    val stream = File(dataFile).inputStream()
    val reader = stream.bufferedReader()
    reader.readLine() // Skips headers

    val websites = reader.lineSequence()
        .map {
            val (id, url, description, category) = it.split(",")
            Website(
                id.toInt(),
                url,
                description,
                category
            )
        }.toList()

    val urls = websites.map {  it.url }
    val scraper = WebScraper(
        outputDirectory = siteDirectory,
        concurrencyLevel = 20,
        requestTimeoutMillis = 30000
    )

    try { scraper.scrapeWebpages(urls) } finally { scraper.close() }
}