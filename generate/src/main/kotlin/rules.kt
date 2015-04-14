package html4k.generate

import java.util.regex.Pattern

val globalSuggestedAttributes = listOf(
        "a" to "href",
        "a" to "target",
        "img" to "src",
        "script" to "type",
        "script" to "src",
        "div" to "class",
        "input" to "type",
        "input" to "name"
).groupBy { it.first }.mapValues { it.getValue().map {it.second} }

val specialTypes = listOf(
        "*.class" to AttributeType.STRING_SET
).groupBy { it.first }.mapValues { it.getValue().single().second }

fun specialTypeFor(tagName : String, attributeName : String) : AttributeType? =
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

val excludeAttributes = listOf("lang$").map { Pattern.compile(it, Pattern.CASE_INSENSITIVE) }
fun isAtrributeExcluded(name : String) = excludeAttributes.any { it.matcher(name).find() }

val knownTagClasses = """
        HTMLCollection
HTMLOptionsCollection
HTMLDocument
HTMLElement
HTMLHtmlElement
HTMLHeadElement
HTMLLinkElement
HTMLTitleElement
HTMLMetaElement
HTMLBaseElement
HTMLIsIndexElement
HTMLStyleElement
HTMLBodyElement
HTMLFormElement
HTMLSelectElement
HTMLOptGroupElement
HTMLOptionElement
HTMLInputElement
HTMLTextAreaElement
HTMLButtonElement
HTMLLabelElement
HTMLFieldSetElement
HTMLLegendElement
HTMLUListElement
HTMLOListElement
HTMLDListElement
HTMLDirectoryElement
HTMLMenuElement
HTMLLIElement
HTMLDivElement
HTMLParagraphElement
HTMLHeadingElement
HTMLQuoteElement
HTMLPreElement
HTMLBRElement
HTMLBaseFontElement
HTMLFontElement
HTMLHRElement
HTMLModElement
HTMLAnchorElement
HTMLImageElement
HTMLObjectElement
HTMLParamElement
HTMLAppletElement
HTMLMapElement
HTMLAreaElement
HTMLScriptElement
HTMLTableElement
HTMLTableCaptionElement
HTMLTableColElement
HTMLTableSectionElement
HTMLTableRowElement
HTMLTableCellElement
HTMLFrameSetElement
HTMLFrameElement
HTMLIFrameElement
HTMLCanvasElement
""".split("\\s+").toSet()

val replacements = listOf("img" to "image", "h\\d" to "heading", "p" to "paragraph", "a" to "anchor", "blockquote" to "quote", "td" to "TableCell", "tr" to "TableRow", "th" to "TableCol")