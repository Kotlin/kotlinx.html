import kotlinx.html.generate.TagInfo
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

private val HTML_TABLE_PATH = "generate/src/main/resources/htmltable.xml"
private val HTML5_TABLE_PATH = "generate/src/main/resources/html5table.xml"

object KdocRepository {
    lateinit var tags: Map<String, KDocInfo>
}

fun fillKdocRepositoryExtension() {
    KdocRepository.tags = parseDocInfos()
}

val TagInfo.kdoc: KDocInfo? get() = KdocRepository.tags[this.name.toLowerCase()]

private fun parseDocInfos(): Map<String, KDocInfo> {
    val html = parseDocInfo(HTML_TABLE_PATH)
    val html5 = parseDocInfo(HTML5_TABLE_PATH)

    return (html + html5).map { it.name to it }.toMap()
}

private fun parseDocInfo(xmlPath: String): List<KDocInfo> {
    val xml = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(File(xmlPath))
    xml.documentElement.normalize()

    return xml.getElementsByTagName("tag").asList().map { node ->
        KDocInfo(
            name = node.getAttributeString("name").toLowerCase(),
            description = node.getAttributeString("description").capitalize(),
            helpref = node.getAttributeString("helpref")
        )
    }
}

data class KDocInfo(
    val name: String,
    val description: String,
    val helpref: String
)

private fun NodeList.asList() = (0 until length).map { item(it) }
private fun Node.getAttributeString(key: String) = attributes.getNamedItem(key).textContent
