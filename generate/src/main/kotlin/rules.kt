package kotlinx.html.generate

import java.util.regex.Pattern

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
        "i" to "class",
        "input" to "type",
        "input" to "name",
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

val renames = mapOf(
        "CommonAttributeGroupFacadePhrasingContent" to "AbstractPhrasingContent",
        "CommonAttributeGroupFacadeFlowContent" to "AbstractFlowContent",
        "CommonAttributeGroupFacadeMetaDataContent" to "AbstractMetaDataContent",
        "CommonAttributeGroupFacadeFlowContentPhrasingContent" to "AbstractFlowAndPhrasingContent"
)

val globalSuggestedAttributeNames = setOf("class")

val specialTypes = listOf(
        "*.class" to AttributeType.STRING_SET
).groupBy { it.first }.mapValues { it.value.single().second }

fun specialTypeFor(tagName: String, attributeName: String): AttributeType? =
        specialTypes[tagName + "." + attributeName] ?: specialTypes["*." + attributeName]

val wellKnownWords = listOf("span", "class", "enabled?", "edit(able)?",
        "^on", "encoded?", "form", "type",
        "run", "href", "drag(gable)?",
        "over", "mouse",
        "start(ed)?", "end(ed)?", "stop", "key", "load(ed)?", "check(ed)?",
        "time", "ready", "content", "changed?",
        "click", "play(ing)?", "context",
        "rows?", "cols?", "group(ed)?", "auto",
        "list", "field", "data", "block", "scripts?",
        "item", "area", "length", "colors?"
).map { Pattern.compile(it, Pattern.CASE_INSENSITIVE) }

val excludeAttributes = listOf("lang$", "^item$").map { Pattern.compile(it, Pattern.CASE_INSENSITIVE) }
fun isAttributeExcluded(name: String) = excludeAttributes.any { it.matcher(name).find() }

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

val replacements = listOf(
        "img" to "image",
        "h\\d" to "heading",
        "p" to "paragraph",
        "a" to "anchor",
        "blockquote" to "quote",
        "td" to "TableCell",
        "tr" to "TableRow",
        "th" to "TableCol",
        "thead" to "TableSection",
        "tbody" to "TableSection",
        "tfoot" to "TableSection"
)