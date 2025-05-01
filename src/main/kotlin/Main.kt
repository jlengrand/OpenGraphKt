package nl.lengrand

import org.jsoup.Jsoup

data class OpenGraph(val title: String, val image: String)

fun main() {
    val doc = Jsoup.connect("https://en.wikipedia.org/").get()
    println(doc.title());

}