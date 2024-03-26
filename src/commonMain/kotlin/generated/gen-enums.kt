package kotlinx.html

import kotlinx.html.*

/*******************************************************************************
    DO NOT EDIT
    This file was generated by module generate
*******************************************************************************/

@Suppress("unused")
enum class Dir(override val realValue : String) : AttributeEnum {
    ltr("ltr"),
    rtl("rtl")
}

internal val dirValues : Map<String, Dir> = Dir.values().associateBy { it.realValue }
@Suppress("unused")
enum class Draggable(override val realValue : String) : AttributeEnum {
    htmlTrue("true"),
    htmlFalse("false"),
    auto("auto")
}

internal val draggableValues : Map<String, Draggable> = Draggable.values().associateBy { it.realValue }
@Suppress("unused")
enum class RunAt(override val realValue : String) : AttributeEnum {
    server("server")
}

internal val runAtValues : Map<String, RunAt> = RunAt.values().associateBy { it.realValue }
@Suppress("unused")
object ATarget {
    val blank : String = "_blank"
    val parent : String = "_parent"
    val self : String = "_self"
    val top : String = "_top"

    val values : List<String> = listOf("blank", "parent", "self", "top")
}

@Suppress("unused")
object ARel {
    val alternate : String = "Alternate"
    val appEndIx : String = "Appendix"
    val bookmark : String = "Bookmark"
    val chapter : String = "Chapter"
    val contentS : String = "Contents"
    val copyright : String = "Copyright"
    val glossary : String = "Glossary"
    val help : String = "Help"
    val index : String = "Index"
    val next : String = "Next"
    val prev : String = "Prev"
    val section : String = "Section"
    val start : String = "Start"
    val stylesheet : String = "Stylesheet"
    val subsection : String = "Subsection"

    val values : List<String> = listOf("alternate", "appEndIx", "bookmark", "chapter", "contentS", "copyright", "glossary", "help", "index", "next", "prev", "section", "start", "stylesheet", "subsection")
}

@Suppress("unused")
object AType {
    val textAsp : String = "text/asp"
    val textAsa : String = "text/asa"
    val textCss : String = "text/css"
    val textHtml : String = "text/html"
    val textJavaScript : String = "text/javascript"
    val textPlain : String = "text/plain"
    val textScriptLet : String = "text/scriptlet"
    val textXComponent : String = "text/x-component"
    val textXHtmlInsertion : String = "text/x-html-insertion"
    val textXml : String = "text/xml"

    val values : List<String> = listOf("textAsp", "textAsa", "textCss", "textHtml", "textJavaScript", "textPlain", "textScriptLet", "textXComponent", "textXHtmlInsertion", "textXml")
}

@Suppress("unused")
enum class AreaShape(override val realValue : String) : AttributeEnum {
    rect("rect"),
    circle("circle"),
    poly("poly"),
    default("default")
}

internal val areaShapeValues : Map<String, AreaShape> = AreaShape.values().associateBy { it.realValue }
@Suppress("unused")
object AreaTarget {
    val blank : String = "_blank"
    val parent : String = "_parent"
    val self : String = "_self"
    val top : String = "_top"

    val values : List<String> = listOf("blank", "parent", "self", "top")
}

@Suppress("unused")
object AreaRel {
    val alternate : String = "Alternate"
    val appEndIx : String = "Appendix"
    val bookmark : String = "Bookmark"
    val chapter : String = "Chapter"
    val contentS : String = "Contents"
    val copyright : String = "Copyright"
    val glossary : String = "Glossary"
    val help : String = "Help"
    val index : String = "Index"
    val next : String = "Next"
    val prev : String = "Prev"
    val section : String = "Section"
    val start : String = "Start"
    val stylesheet : String = "Stylesheet"
    val subsection : String = "Subsection"

    val values : List<String> = listOf("alternate", "appEndIx", "bookmark", "chapter", "contentS", "copyright", "glossary", "help", "index", "next", "prev", "section", "start", "stylesheet", "subsection")
}

@Suppress("unused")
object BaseTarget {
    val blank : String = "_blank"
    val parent : String = "_parent"
    val self : String = "_self"
    val top : String = "_top"

    val values : List<String> = listOf("blank", "parent", "self", "top")
}

@Suppress("unused")
enum class ButtonFormEncType(override val realValue : String) : AttributeEnum {
    multipartFormData("multipart/form-data"),
    applicationXWwwFormUrlEncoded("application/x-www-form-urlencoded"),
    textPlain("text/plain")
}

internal val buttonFormEncTypeValues : Map<String, ButtonFormEncType> = ButtonFormEncType.values().associateBy { it.realValue }
@Suppress("unused")
enum class ButtonFormMethod(override val realValue : String) : AttributeEnum {
    get("get"),
    post("post"),
    @Deprecated("method is not allowed in browsers") put("put"),
    @Deprecated("method is not allowed in browsers") delete("delete"),
    @Deprecated("method is not allowed in browsers") patch("patch")
}

internal val buttonFormMethodValues : Map<String, ButtonFormMethod> = ButtonFormMethod.values().associateBy { it.realValue }
@Suppress("unused")
object ButtonFormTarget {
    val blank : String = "_blank"
    val parent : String = "_parent"
    val self : String = "_self"
    val top : String = "_top"

    val values : List<String> = listOf("blank", "parent", "self", "top")
}

@Suppress("unused")
enum class ButtonType(override val realValue : String) : AttributeEnum {
    button("button"),
    reset("reset"),
    submit("submit")
}

internal val buttonTypeValues : Map<String, ButtonType> = ButtonType.values().associateBy { it.realValue }
@Suppress("unused")
enum class CommandType(override val realValue : String) : AttributeEnum {
    command("command"),
    checkBox("checkbox"),
    radio("radio")
}

internal val commandTypeValues : Map<String, CommandType> = CommandType.values().associateBy { it.realValue }
@Suppress("unused")
enum class FormEncType(override val realValue : String) : AttributeEnum {
    multipartFormData("multipart/form-data"),
    applicationXWwwFormUrlEncoded("application/x-www-form-urlencoded"),
    textPlain("text/plain")
}

internal val formEncTypeValues : Map<String, FormEncType> = FormEncType.values().associateBy { it.realValue }
@Suppress("unused")
enum class FormMethod(override val realValue : String) : AttributeEnum {
    get("get"),
    post("post"),
    @Deprecated("method is not allowed in browsers") put("put"),
    @Deprecated("method is not allowed in browsers") delete("delete"),
    @Deprecated("method is not allowed in browsers") patch("patch")
}

internal val formMethodValues : Map<String, FormMethod> = FormMethod.values().associateBy { it.realValue }
@Suppress("unused")
object FormTarget {
    val blank : String = "_blank"
    val parent : String = "_parent"
    val self : String = "_self"
    val top : String = "_top"

    val values : List<String> = listOf("blank", "parent", "self", "top")
}

@Suppress("unused")
object IframeName {
    val blank : String = "_blank"
    val parent : String = "_parent"
    val self : String = "_self"
    val top : String = "_top"

    val values : List<String> = listOf("blank", "parent", "self", "top")
}

@Suppress("unused")
enum class IframeSandbox(override val realValue : String) : AttributeEnum {
    allowSameOrigin("allow-same-origin"),
    allowFormS("allow-forms"),
    allowScripts("allow-scripts")
}

internal val iframeSandboxValues : Map<String, IframeSandbox> = IframeSandbox.values().associateBy { it.realValue }
@Suppress("unused")
enum class ImgLoading(override val realValue : String) : AttributeEnum {
    eager("eager"),
    lazy("lazy")
}

internal val imgLoadingValues : Map<String, ImgLoading> = ImgLoading.values().associateBy { it.realValue }
@Suppress("unused")
enum class InputType(override val realValue : String) : AttributeEnum {
    button("button"),
    checkBox("checkbox"),
    color("color"),
    date("date"),
    dateTime("datetime"),
    dateTimeLocal("datetime-local"),
    email("email"),
    file("file"),
    hidden("hidden"),
    image("image"),
    month("month"),
    number("number"),
    password("password"),
    radio("radio"),
    range("range"),
    reset("reset"),
    search("search"),
    submit("submit"),
    text("text"),
    tel("tel"),
    time("time"),
    url("url"),
    week("week")
}

internal val inputTypeValues : Map<String, InputType> = InputType.values().associateBy { it.realValue }
@Suppress("unused")
enum class InputFormEncType(override val realValue : String) : AttributeEnum {
    multipartFormData("multipart/form-data"),
    applicationXWwwFormUrlEncoded("application/x-www-form-urlencoded"),
    textPlain("text/plain")
}

internal val inputFormEncTypeValues : Map<String, InputFormEncType> = InputFormEncType.values().associateBy { it.realValue }
@Suppress("unused")
enum class InputFormMethod(override val realValue : String) : AttributeEnum {
    get("get"),
    post("post"),
    @Deprecated("method is not allowed in browsers") put("put"),
    @Deprecated("method is not allowed in browsers") delete("delete"),
    @Deprecated("method is not allowed in browsers") patch("patch")
}

internal val inputFormMethodValues : Map<String, InputFormMethod> = InputFormMethod.values().associateBy { it.realValue }
@Suppress("unused")
object InputFormTarget {
    val blank : String = "_blank"
    val parent : String = "_parent"
    val self : String = "_self"
    val top : String = "_top"

    val values : List<String> = listOf("blank", "parent", "self", "top")
}

@Suppress("unused")
enum class KeyGenKeyType(override val realValue : String) : AttributeEnum {
    rsa("rsa")
}

internal val keyGenKeyTypeValues : Map<String, KeyGenKeyType> = KeyGenKeyType.values().associateBy { it.realValue }
@Suppress("unused")
object LinkRel {
    val alternate : String = "Alternate"
    val appEndIx : String = "Appendix"
    val bookmark : String = "Bookmark"
    val chapter : String = "Chapter"
    val contentS : String = "Contents"
    val copyright : String = "Copyright"
    val glossary : String = "Glossary"
    val help : String = "Help"
    val index : String = "Index"
    val next : String = "Next"
    val prev : String = "Prev"
    val section : String = "Section"
    val start : String = "Start"
    val stylesheet : String = "Stylesheet"
    val subsection : String = "Subsection"

    val values : List<String> = listOf("alternate", "appEndIx", "bookmark", "chapter", "contentS", "copyright", "glossary", "help", "index", "next", "prev", "section", "start", "stylesheet", "subsection")
}

@Suppress("unused")
object LinkMedia {
    val screen : String = "screen"
    val print : String = "print"
    val tty : String = "tty"
    val tv : String = "tv"
    val projection : String = "projection"
    val handheld : String = "handheld"
    val braille : String = "braille"
    val aural : String = "aural"
    val all : String = "all"

    val values : List<String> = listOf("screen", "print", "tty", "tv", "projection", "handheld", "braille", "aural", "all")
}

@Suppress("unused")
object LinkType {
    val textAsp : String = "text/asp"
    val textAsa : String = "text/asa"
    val textCss : String = "text/css"
    val textHtml : String = "text/html"
    val textJavaScript : String = "text/javascript"
    val textPlain : String = "text/plain"
    val textScriptLet : String = "text/scriptlet"
    val textXComponent : String = "text/x-component"
    val textXHtmlInsertion : String = "text/x-html-insertion"
    val textXml : String = "text/xml"

    val values : List<String> = listOf("textAsp", "textAsa", "textCss", "textHtml", "textJavaScript", "textPlain", "textScriptLet", "textXComponent", "textXHtmlInsertion", "textXml")
}

@Suppress("unused")
enum class LinkAs(override val realValue : String) : AttributeEnum {
    audio("audio"),
    document("document"),
    embed("embed"),
    fetch("fetch"),
    font("font"),
    image("image"),
    htmlObject("object"),
    script("script"),
    style("style"),
    track("track"),
    video("video"),
    worker("worker")
}

internal val linkAsValues : Map<String, LinkAs> = LinkAs.values().associateBy { it.realValue }
@Suppress("unused")
object MetaHttpEquiv {
    val contentLanguage : String = "content-language"
    val contentType : String = "content-type"
    val defaultStyle : String = "default-style"
    val refresh : String = "refresh"

    val values : List<String> = listOf("contentLanguage", "contentType", "defaultStyle", "refresh")
}

@Suppress("unused")
object ObjectName {
    val blank : String = "_blank"
    val parent : String = "_parent"
    val self : String = "_self"
    val top : String = "_top"

    val values : List<String> = listOf("blank", "parent", "self", "top")
}

@Suppress("unused")
object ScriptType {
    val textEcmaScript : String = "text/ecmascript"
    val textJavaScript : String = "text/javascript"
    val textJavaScript10 : String = "text/javascript1.0"
    val textJavaScript11 : String = "text/javascript1.1"
    val textJavaScript12 : String = "text/javascript1.2"
    val textJavaScript13 : String = "text/javascript1.3"
    val textJavaScript14 : String = "text/javascript1.4"
    val textJavaScript15 : String = "text/javascript1.5"
    val textJScript : String = "text/jscript"
    val textXJavaScript : String = "text/x-javascript"
    val textXEcmaScript : String = "text/x-ecmascript"
    val textVbScript : String = "text/vbscript"

    val values : List<String> = listOf("textEcmaScript", "textJavaScript", "textJavaScript10", "textJavaScript11", "textJavaScript12", "textJavaScript13", "textJavaScript14", "textJavaScript15", "textJScript", "textXJavaScript", "textXEcmaScript", "textVbScript")
}

@Suppress("unused")
enum class ScriptCrossorigin(override val realValue : String) : AttributeEnum {
    anonymous("anonymous"),
    useCredentials("use-credentials")
}

internal val scriptCrossoriginValues : Map<String, ScriptCrossorigin> = ScriptCrossorigin.values().associateBy { it.realValue }
@Suppress("unused")
object StyleType {
    val textCss : String = "text/css"

    val values : List<String> = listOf("textCss")
}

@Suppress("unused")
object StyleMedia {
    val screen : String = "screen"
    val print : String = "print"
    val tty : String = "tty"
    val tv : String = "tv"
    val projection : String = "projection"
    val handheld : String = "handheld"
    val braille : String = "braille"
    val aural : String = "aural"
    val all : String = "all"

    val values : List<String> = listOf("screen", "print", "tty", "tv", "projection", "handheld", "braille", "aural", "all")
}

@Suppress("unused")
enum class TextAreaWrap(override val realValue : String) : AttributeEnum {
    hard("hard"),
    soft("soft")
}

internal val textAreaWrapValues : Map<String, TextAreaWrap> = TextAreaWrap.values().associateBy { it.realValue }
@Suppress("unused")
enum class ThScope(override val realValue : String) : AttributeEnum {
    col("col"),
    colGroup("colgroup"),
    row("row"),
    rowGroup("rowgroup")
}

internal val thScopeValues : Map<String, ThScope> = ThScope.values().associateBy { it.realValue }
