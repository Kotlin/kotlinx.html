package kotlinx.html.jsoup

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Document.OutputSettings
import org.jsoup.parser.Parser
import org.jsoup.safety.Whitelist
import java.io.File
import java.io.InputStream
import java.net.URL

fun String.parseBodyFragment(): Document
    = Jsoup.parseBodyFragment(this)

fun URL.parse(timeoutMillis: Int): Document
    = Jsoup.parse(this, timeoutMillis)

fun String.parse(): Document
    = Jsoup.parse(this)

fun String.parse(baseUri: String): Document
    = Jsoup.parse(this, baseUri)

fun String.parse(baseUri: String, parser: Parser): Document
    = Jsoup.parse(this, baseUri, parser)

fun File.parse(charsetName: String): Document
    = Jsoup.parse(this, charsetName)

fun File.parse(charsetName: String, baseUri: String): Document
    = Jsoup.parse(this, charsetName, baseUri)

fun InputStream.parse(charsetName: String, baseUri: String): Document
    = Jsoup.parse(this, charsetName, baseUri)

fun InputStream.parse(charsetName: String, baseUri: String, parser: Parser): Document
    = Jsoup.parse(this, charsetName, baseUri, parser)

fun String.isValid(whitelist: Whitelist): Boolean
    = Jsoup.isValid(this, whitelist)

fun String.clean(whitelist: Whitelist): String
    = Jsoup.clean(this, whitelist)

fun String.clean(baseUri: String, whitelist: Whitelist): String
    = Jsoup.clean(this, baseUri, whitelist)

fun String.clean(baseUri: String, whitelist: Whitelist, outputSettings: OutputSettings): String
    = Jsoup.clean(this, baseUri, whitelist, outputSettings)
