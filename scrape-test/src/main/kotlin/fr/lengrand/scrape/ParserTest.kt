package fr.lengrand.scrape

import fr.lengrand.opengraphkt.Parser
import java.net.URI
import java.nio.file.Files
import java.nio.file.Paths

fun main() {

    val parser = Parser()

    var total = 0
    var success = 0
    var error = 0
    var valid = 0

    val websiteFolder = "./scrape-test/data/web"
    val path = Paths.get(websiteFolder)

    Files.walk(path)
        .forEach {
            println("filename: $it")
            total++
            try{
                val openGraphData = parser.parse(URI("https://www.imdb.com/title/tt0068646/").toURL())
                success++

                if(openGraphData.isValid()) {
                    valid++
                }

            }catch (e: Exception) {
                println("Error parsing URL: ${e.message}")
                error++
            }
        }

    println("Total: $total")
    println("Success: $success")
    println("Valid: $valid")
    println("Error: $error")
}
