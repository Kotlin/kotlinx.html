package kotlinx.html.generate

import java.util.regex.*

val globalSuggestedAttributes = listOf(
        "a" to "href",
        "a" to "target",
        "img" to "src",
        "script" to "type",
        "script" to "src",
        "div" to "class",
        "span" to "class",
        "meta" to "name",
        "meta" to "content",
        "meta" to "charset",
        "i" to "class",
        "input" to "type",
        "input" to "name",
        "button" to "name",
        "link" to "rel",
        "link" to "href",
        "link" to "type",
        "style" to "type",
        "head" to "-class",
        "html" to "-class",
        "link" to "-class",
        "script" to "-class",
        "style" to "-class",
        "meta" to "-class",
        "title" to "-class"
).groupBy { it.first }.mapValues { it.value.map { it.second } }

val tagNamespaces = mapOf(
        "svg" to "http://www.w3.org/2000/svg"
)

val tagsWithCustomizableNamespace = setOf("html")

val renames = mapOf(
        "CommonAttributeGroupFacadePhrasingContent" to "HtmlInlineTag",
        "CommonAttributeGroupFacadeFlowContent" to "HtmlBlockTag",
        "CommonAttributeGroupFacadeMetaDataContent" to "HtmlHeadTag",
        "CommonAttributeGroupFacadeFlowPhrasingContent" to "HtmlBlockInlineTag"
)

val tagIgnoreList = setOf(
    "menu", "menuitem"
)

fun Iterable<TagInfo>.filterIgnored() = filter { it.name.lowercase() !in tagIgnoreList }

val globalSuggestedAttributeNames = setOf("class")

val specialTypes = listOf(
        "*.class" to AttributeType.STRING_SET
).groupBy { it.first }.mapValues { it.value.single().second }

fun specialTypeFor(tagName: String, attributeName: String): AttributeType? =
        specialTypes["$tagName.$attributeName"] ?: specialTypes["*.$attributeName"]

val wellKnownWords = listOf(
        "span",
        "class",
        "enabled?",
        "edit(able)?",
        "^on",
        "encoded?",
        "form",
        "type",
        "run",
        "href",
        "drag(gable)?",
        "over",
        "mouse",
        "start(ed)?",
        "legend",
        "end(ed)?",
        "stop",
        "key",
        "load(ed)?",
        "check(ed)?",
        "time",
        "ready",
        "content",
        "changed?",
        "click",
        "play(ing)?",
        "context",
        "rows?",
        "cols?",
        "group(ed)?",
        "auto",
        "list",
        "field",
        "data",
        "block",
        "scripts?",
        "item",
        "area",
        "length",
        "colors?",
        "suspend",
        "focus",
        "touch",
        "loading",
        "referrer",
).map { it.toRegex(RegexOption.IGNORE_CASE) }

val excludeAttributes = listOf("^item$").map { Pattern.compile(it, Pattern.CASE_INSENSITIVE) }
fun isAttributeExcluded(name: String) = excludeAttributes.any { it.matcher(name).find() }

val excludedEnums = listOf("Lang$").map { it.toRegex(RegexOption.IGNORE_CASE) }
fun isEnumExcluded(name: String) = excludedEnums.any { it.containsMatchIn(name) }

val contentlessTags = setOf("html", "head", "script", "style")

val deprecated = listOf(
        ".*FormMethod#(put|patch|delete)" to "method is not allowed in browsers",
        ".*TextAreaWrap#(virtual|physical|off)" to "value only supported in IE"
).map { it.first.toRegex(RegexOption.IGNORE_CASE) to it.second }

fun findEnumDeprecation(attribute: AttributeInfo, value: AttributeEnumValue): String? {
    return deprecated.firstOrNull { p -> p.first.matches("""${attribute.enumTypeName}#${value.realName}""") }?.second
}

val knownTagClasses = """
HTMLElement
HTMLUnknownElement
HTMLHtmlElement
HTMLHeadElement
HTMLTitleElement
HTMLBaseElement
HTMLLinkElement
HTMLMetaElement
HTMLStyleElement
HTMLBodyElement
HTMLHeadingElement
HTMLParagraphElement
HTMLHRElement
HTMLPreElement
HTMLQuoteElement
HTMLOListElement
HTMLUListElement
HTMLLIElement
HTMLDListElement
HTMLDivElement
HTMLAnchorElement
HTMLDataElement
HTMLTimeElement
HTMLSpanElement
HTMLBRElement
HTMLModElement
HTMLIFrameElement
HTMLEmbedElement
HTMLObjectElement
HTMLParamElement
HTMLVideoElement
HTMLAudioElement
HTMLSourceElement
HTMLTrackElement
HTMLMediaElement
HTMLMapElement
HTMLAreaElement
HTMLTableElement
HTMLTableCaptionElement
HTMLTableColElement
HTMLTableSectionElement
HTMLTableRowElement
HTMLTableDataCellElement
HTMLTableHeaderCellElement
HTMLTableCellElement
HTMLFormElement
HTMLLabelElement
HTMLInputElement
HTMLButtonElement
HTMLSelectElement
HTMLDataListElement
HTMLOptGroupElement
HTMLOptionElement
HTMLTextAreaElement
HTMLKeygenElement
HTMLOutputElement
HTMLProgressElement
HTMLMeterElement
HTMLFieldSetElement
HTMLLegendElement
HTMLDetailsElement
HTMLMenuElement
HTMLMenuItemElement
HTMLDialogElement
HTMLScriptElement
HTMLTemplateElement
HTMLCanvasElement
HTMLAppletElement
HTMLMarqueeElement
HTMLFrameSetElement
HTMLFrameElement
HTMLAnchorElement
HTMLAreaElement
HTMLBodyElement
HTMLBRElement
HTMLTableCaptionElement
HTMLTableColElement
HTMLDirectoryElement
HTMLDivElement
HTMLDListElement
HTMLEmbedElement
HTMLFontElement
HTMLHeadingElement
HTMLHRElement
HTMLHtmlElement
HTMLIFrameElement
HTMLImageElement
HTMLInputElement
HTMLLegendElement
HTMLLIElement
HTMLLinkElement
HTMLMenuElement
HTMLMetaElement
HTMLObjectElement
HTMLOListElement
HTMLParagraphElement
HTMLParamElement
HTMLPreElement
HTMLScriptElement
HTMLTableElement
HTMLTableSectionElement
HTMLTableCellElement
HTMLTableDataCellElement
HTMLTableRowElement
HTMLUListElement
HTMLElement
HTMLUnknownElement
HTMLHtmlElement
HTMLHeadElement
HTMLTitleElement
HTMLBaseElement
HTMLLinkElement
HTMLMetaElement
HTMLStyleElement
HTMLBodyElement
HTMLHeadingElement
HTMLParagraphElement
HTMLHRElement
HTMLPreElement
HTMLQuoteElement
HTMLOListElement
HTMLUListElement
HTMLLIElement
HTMLDListElement
HTMLDivElement
HTMLAnchorElement
HTMLDataElement
HTMLTimeElement
HTMLSpanElement
HTMLBRElement
HTMLModElement
HTMLPictureElement
HTMLSourceElement
HTMLImageElement
HTMLIFrameElement
HTMLEmbedElement
HTMLObjectElement
HTMLParamElement
HTMLVideoElement
HTMLAudioElement
HTMLSourceElement
HTMLTrackElement
HTMLMediaElement
HTMLMapElement
HTMLAreaElement
HTMLTableElement
HTMLTableCaptionElement
HTMLTableColElement
HTMLTableSectionElement
HTMLTableRowElement
HTMLTableDataCellElement
HTMLTableHeaderCellElement
HTMLTableCellElement
HTMLFormElement
HTMLLabelElement
HTMLInputElement
HTMLButtonElement
HTMLSelectElement
HTMLDataListElement
HTMLOptGroupElement
HTMLOptionElement
HTMLTextAreaElement
HTMLKeygenElement
HTMLOutputElement
HTMLProgressElement
HTMLMeterElement
HTMLFieldSetElement
HTMLLegendElement
HTMLDetailsElement
HTMLMenuElement
HTMLMenuItemElement
HTMLDialogElement
HTMLScriptElement
HTMLTemplateElement
HTMLCanvasElement
HTMLAppletElement
HTMLMarqueeElement
HTMLFrameSetElement
HTMLFrameElement
HTMLAnchorElement
HTMLAreaElement
HTMLBodyElement
HTMLBRElement
HTMLTableCaptionElement
HTMLTableColElement
HTMLDirectoryElement
HTMLDivElement
HTMLDListElement
HTMLEmbedElement
HTMLFontElement
HTMLHeadingElement
HTMLHRElement
HTMLHtmlElement
HTMLIFrameElement
HTMLImageElement
HTMLInputElement
HTMLLegendElement
HTMLLIElement
HTMLLinkElement
HTMLMenuElement
HTMLMetaElement
HTMLObjectElement
HTMLOListElement
HTMLParagraphElement
HTMLParamElement
HTMLPreElement
HTMLScriptElement
HTMLTableElement
HTMLTableSectionElement
HTMLTableCellElement
HTMLTableDataCellElement
HTMLTableRowElement
HTMLUListElement
""".split("\\s+".toRegex()).toSet()

val tagReplacements = listOf(
        "img" to "image",
        "h\\d" to "heading",
        "p" to "paragraph",
        "a" to "anchor",
        "blockquote" to "quote",
        "td" to "TableCell",
        "tr" to "TableRow",
        "th" to "TableCell",
        "col" to "TableCol",
        "colGroup" to "TableCol",
        "thead" to "TableSection",
        "tbody" to "TableSection",
        "tfoot" to "TableSection"
)

val attributeReplacements = listOf(
        "class" to "classes"
).map { Pair(it.first.toRegex(), it.second) }