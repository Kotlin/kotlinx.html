package kotlinx.html.generate

import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Node
import org.w3c.dom.NodeList

private val HTML_TABLE_URL = "htmltable.xml".asResourceUrl()
private val HTML5_TABLE_URL = "html5table.xml".asResourceUrl()

object KdocRepository {
    lateinit var tags: Map<String, KDocInfo>
}

private var kdocRepositoryFilled = false

fun fillKdocRepositoryExtension() {
    if (kdocRepositoryFilled) return
    kdocRepositoryFilled = true
    KdocRepository.tags = parseDocInfos()
}

val TagInfo.kdoc: KDocInfo? get() = KdocRepository.tags[this.name.lowercase()]

private fun parseDocInfos(): Map<String, KDocInfo> {
    val html = parseDocInfo(HTML_TABLE_URL)
    val html5 = parseDocInfo(HTML5_TABLE_URL)

    return (html + html5).map { it.name to it }.toMap()
}

private fun parseDocInfo(xmlPath: URL): List<KDocInfo> {
    val xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(xmlPath.openStream())
    xml.documentElement.normalize()

    return xml.getElementsByTagName("tag").asList().map { node ->
        KDocInfo(
            name = node.getAttributeString("name").lowercase(),
            description = node.getAttributeString("description").capitalize(),
            helpref = node.getAttributeString("helpref")
        )
    }
}

data class KDocInfo(
    val name: String,
    val description: String,
    val helpref: String,
)

private fun NodeList.asList() = (0 until length).map { item(it) }
private fun Node.getAttributeString(key: String) = attributes.getNamedItem(key).textContent
