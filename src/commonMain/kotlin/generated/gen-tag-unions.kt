package kotlinx.html

import kotlinx.html.*
import kotlinx.html.impl.*
import kotlinx.html.attributes.*

/*******************************************************************************
    DO NOT EDIT
    This file was generated by module generate
*******************************************************************************/

interface FlowOrMetaDataOrPhrasingContent : Tag {
}

interface FlowOrHeadingContent : Tag {
}

interface FlowOrMetaDataContent : FlowOrMetaDataOrPhrasingContent, Tag {
}

interface FlowOrInteractiveContent : FlowOrInteractiveOrPhrasingContent, Tag {
}

interface FlowOrPhrasingContent : FlowOrInteractiveOrPhrasingContent, FlowOrMetaDataOrPhrasingContent, Tag {
}

interface SectioningOrFlowContent : Tag {
}

interface FlowOrInteractiveOrPhrasingContent : Tag {
}



@HtmlTagMarker
inline fun FlowOrMetaDataOrPhrasingContent.command(type : CommandType? = null, classes : String? = null, crossinline block : COMMAND.() -> Unit = {}) : Unit = COMMAND(attributesMapOf("type", type?.enumEncode(),"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrMetaDataOrPhrasingContent.commandCommand(classes : String? = null, crossinline block : COMMAND.() -> Unit = {}) : Unit = COMMAND(attributesMapOf("type", CommandType.command.realValue,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrMetaDataOrPhrasingContent.checkBoxCommand(classes : String? = null, crossinline block : COMMAND.() -> Unit = {}) : Unit = COMMAND(attributesMapOf("type", CommandType.checkBox.realValue,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrMetaDataOrPhrasingContent.radioCommand(classes : String? = null, crossinline block : COMMAND.() -> Unit = {}) : Unit = COMMAND(attributesMapOf("type", CommandType.radio.realValue,"class", classes), consumer).visit(block)

/**
 * A media-independent link
 */
@HtmlTagMarker
inline fun FlowOrMetaDataOrPhrasingContent.link(href : String? = null, rel : String? = null, type : String? = null, crossinline block : LINK.() -> Unit = {}) : Unit = LINK(attributesMapOf("href", href,"rel", rel,"type", type), consumer).visit(block)

/**
 * Generic metainformation
 */
@HtmlTagMarker
inline fun FlowOrMetaDataOrPhrasingContent.meta(name : String? = null, content : String? = null, charset : String? = null, crossinline block : META.() -> Unit = {}) : Unit = META(attributesMapOf("name", name,"content", content,"charset", charset), consumer).visit(block)

/**
 * Generic metainformation
 */
@HtmlTagMarker
inline fun FlowOrMetaDataOrPhrasingContent.noScript(classes : String? = null, crossinline block : NOSCRIPT.() -> Unit = {}) : Unit = NOSCRIPT(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Script statements
 */
@HtmlTagMarker
inline fun FlowOrMetaDataOrPhrasingContent.script(type : String? = null, src : String? = null, crossorigin : ScriptCrossorigin? = null, crossinline block : SCRIPT.() -> Unit = {}) : Unit = SCRIPT(attributesMapOf("type", type,"src", src,"crossorigin", crossorigin?.enumEncode()), consumer).visit(block)
@Deprecated("This tag doesn't support content or requires unsafe (try unsafe {})")
@Suppress("DEPRECATION")
/**
 * Script statements
 */
@HtmlTagMarker
fun FlowOrMetaDataOrPhrasingContent.script(type : String? = null, src : String? = null, crossorigin : ScriptCrossorigin? = null, content : String = "") : Unit = SCRIPT(attributesMapOf("type", type,"src", src,"crossorigin", crossorigin?.enumEncode()), consumer).visit({+content})
@HtmlTagMarker
inline fun FlowOrMetaDataOrPhrasingContent.anonymousScript(type : String? = null, src : String? = null, crossinline block : SCRIPT.() -> Unit = {}) : Unit = SCRIPT(attributesMapOf("type", type,"src", src,"crossorigin", ScriptCrossorigin.anonymous.realValue), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrMetaDataOrPhrasingContent.useCredentialsScript(type : String? = null, src : String? = null, crossinline block : SCRIPT.() -> Unit = {}) : Unit = SCRIPT(attributesMapOf("type", type,"src", src,"crossorigin", ScriptCrossorigin.useCredentials.realValue), consumer).visit(block)
@Deprecated("This tag doesn't support content or requires unsafe (try unsafe {})")
@Suppress("DEPRECATION")
@HtmlTagMarker
fun FlowOrMetaDataOrPhrasingContent.anonymousScript(type : String? = null, src : String? = null, content : String = "") : Unit = SCRIPT(attributesMapOf("type", type,"src", src,"crossorigin", ScriptCrossorigin.anonymous.realValue), consumer).visit({+content})
@HtmlTagMarker
fun FlowOrMetaDataOrPhrasingContent.useCredentialsScript(type : String? = null, src : String? = null, content : String = "") : Unit = SCRIPT(attributesMapOf("type", type,"src", src,"crossorigin", ScriptCrossorigin.useCredentials.realValue), consumer).visit({+content})


/**
 * Heading
 */
@HtmlTagMarker
inline fun FlowOrHeadingContent.h1(classes : String? = null, crossinline block : H1.() -> Unit = {}) : Unit = H1(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Heading
 */
@HtmlTagMarker
inline fun FlowOrHeadingContent.h2(classes : String? = null, crossinline block : H2.() -> Unit = {}) : Unit = H2(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Heading
 */
@HtmlTagMarker
inline fun FlowOrHeadingContent.h3(classes : String? = null, crossinline block : H3.() -> Unit = {}) : Unit = H3(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Heading
 */
@HtmlTagMarker
inline fun FlowOrHeadingContent.h4(classes : String? = null, crossinline block : H4.() -> Unit = {}) : Unit = H4(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Heading
 */
@HtmlTagMarker
inline fun FlowOrHeadingContent.h5(classes : String? = null, crossinline block : H5.() -> Unit = {}) : Unit = H5(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Heading
 */
@HtmlTagMarker
inline fun FlowOrHeadingContent.h6(classes : String? = null, crossinline block : H6.() -> Unit = {}) : Unit = H6(attributesMapOf("class", classes), consumer).visit(block)

@HtmlTagMarker
inline fun FlowOrHeadingContent.hGroup(classes : String? = null, crossinline block : HGROUP.() -> Unit = {}) : Unit = HGROUP(attributesMapOf("class", classes), consumer).visit(block)


/**
 * Style info
 */
@HtmlTagMarker
inline fun FlowOrMetaDataContent.style(type : String? = null, crossinline block : STYLE.() -> Unit = {}) : Unit = STYLE(attributesMapOf("type", type), consumer).visit(block)
@Deprecated("This tag doesn't support content or requires unsafe (try unsafe {})")
@Suppress("DEPRECATION")
/**
 * Style info
 */
@HtmlTagMarker
fun FlowOrMetaDataContent.style(type : String? = null, content : String = "") : Unit = STYLE(attributesMapOf("type", type), consumer).visit({+content})


/**
 * Disclosure control for hiding details
 */
@HtmlTagMarker
inline fun FlowOrInteractiveContent.details(classes : String? = null, crossinline block : DETAILS.() -> Unit = {}) : Unit = DETAILS(attributesMapOf("class", classes), consumer).visit(block)


/**
 * Abbreviated form (e.g., WWW, HTTP,etc.)
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.abbr(classes : String? = null, crossinline block : ABBR.() -> Unit = {}) : Unit = ABBR(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Client-side image map area
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.area(shape : AreaShape? = null, alt : String? = null, classes : String? = null, crossinline block : AREA.() -> Unit = {}) : Unit = AREA(attributesMapOf("Shape", shape?.enumEncode(),"alt", alt,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrPhrasingContent.rectArea(alt : String? = null, classes : String? = null, crossinline block : AREA.() -> Unit = {}) : Unit = AREA(attributesMapOf("Shape", AreaShape.rect.realValue,"alt", alt,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrPhrasingContent.circleArea(alt : String? = null, classes : String? = null, crossinline block : AREA.() -> Unit = {}) : Unit = AREA(attributesMapOf("Shape", AreaShape.circle.realValue,"alt", alt,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrPhrasingContent.polyArea(alt : String? = null, classes : String? = null, crossinline block : AREA.() -> Unit = {}) : Unit = AREA(attributesMapOf("Shape", AreaShape.poly.realValue,"alt", alt,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrPhrasingContent.defaultArea(alt : String? = null, classes : String? = null, crossinline block : AREA.() -> Unit = {}) : Unit = AREA(attributesMapOf("Shape", AreaShape.default.realValue,"alt", alt,"class", classes), consumer).visit(block)

/**
 * Bold text style
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.b(classes : String? = null, crossinline block : B.() -> Unit = {}) : Unit = B(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Text directionality isolation
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.bdi(classes : String? = null, crossinline block : BDI.() -> Unit = {}) : Unit = BDI(attributesMapOf("class", classes), consumer).visit(block)

/**
 * I18N BiDi over-ride
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.bdo(classes : String? = null, crossinline block : BDO.() -> Unit = {}) : Unit = BDO(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Forced line break
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.br(classes : String? = null, crossinline block : BR.() -> Unit = {}) : Unit = BR(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Scriptable bitmap canvas
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.canvas(classes : String? = null, crossinline block : CANVAS.() -> Unit = {}) : Unit = CANVAS(attributesMapOf("class", classes), consumer).visit(block)
/**
 * Scriptable bitmap canvas
 */
@HtmlTagMarker
fun FlowOrPhrasingContent.canvas(classes : String? = null, content : String = "") : Unit = CANVAS(attributesMapOf("class", classes), consumer).visit({+content})

/**
 * Citation
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.cite(classes : String? = null, crossinline block : CITE.() -> Unit = {}) : Unit = CITE(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Computer code fragment
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.code(classes : String? = null, crossinline block : CODE.() -> Unit = {}) : Unit = CODE(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Container for options for 
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.dataList(classes : String? = null, crossinline block : DATALIST.() -> Unit = {}) : Unit = DATALIST(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Deleted text
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.del(classes : String? = null, crossinline block : DEL.() -> Unit = {}) : Unit = DEL(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Instance definition
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.dfn(classes : String? = null, crossinline block : DFN.() -> Unit = {}) : Unit = DFN(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Emphasis
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.em(classes : String? = null, crossinline block : EM.() -> Unit = {}) : Unit = EM(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Italic text style
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.i(classes : String? = null, crossinline block : I.() -> Unit = {}) : Unit = I(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Inserted text
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.ins(classes : String? = null, crossinline block : INS.() -> Unit = {}) : Unit = INS(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Text to be entered by the user
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.kbd(classes : String? = null, crossinline block : KBD.() -> Unit = {}) : Unit = KBD(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Client-side image map
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.map(name : String? = null, classes : String? = null, crossinline block : MAP.() -> Unit = {}) : Unit = MAP(attributesMapOf("name", name,"class", classes), consumer).visit(block)

/**
 * Highlight
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.mark(classes : String? = null, crossinline block : MARK.() -> Unit = {}) : Unit = MARK(attributesMapOf("class", classes), consumer).visit(block)

@HtmlTagMarker
inline fun FlowOrPhrasingContent.math(classes : String? = null, crossinline block : MATH.() -> Unit = {}) : Unit = MATH(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Gauge
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.meter(classes : String? = null, crossinline block : METER.() -> Unit = {}) : Unit = METER(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Calculated output value
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.output(classes : String? = null, crossinline block : OUTPUT.() -> Unit = {}) : Unit = OUTPUT(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Progress bar
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.progress(classes : String? = null, crossinline block : PROGRESS.() -> Unit = {}) : Unit = PROGRESS(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Short inline quotation
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.q(classes : String? = null, crossinline block : Q.() -> Unit = {}) : Unit = Q(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Ruby annotation(s)
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.ruby(classes : String? = null, crossinline block : RUBY.() -> Unit = {}) : Unit = RUBY(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Computer output text style
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.samp(classes : String? = null, crossinline block : SAMP.() -> Unit = {}) : Unit = SAMP(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Small text style
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.small(classes : String? = null, crossinline block : SMALL.() -> Unit = {}) : Unit = SMALL(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Generic language/style container
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.span(classes : String? = null, crossinline block : SPAN.() -> Unit = {}) : Unit = SPAN(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Strong emphasis
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.strong(classes : String? = null, crossinline block : STRONG.() -> Unit = {}) : Unit = STRONG(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Subscript
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.sub(classes : String? = null, crossinline block : SUB.() -> Unit = {}) : Unit = SUB(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Superscript
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.sup(classes : String? = null, crossinline block : SUP.() -> Unit = {}) : Unit = SUP(attributesMapOf("class", classes), consumer).visit(block)

@HtmlTagMarker
inline fun FlowOrPhrasingContent.svg(classes : String? = null, crossinline block : SVG.() -> Unit = {}) : Unit = SVG(attributesMapOf("class", classes), consumer).visit(block)
@HtmlTagMarker
fun FlowOrPhrasingContent.svg(classes : String? = null, content : String = "") : Unit = SVG(attributesMapOf("class", classes), consumer).visit({+content})

/**
 * Machine-readable equivalent of date- or time-related data
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.time(classes : String? = null, crossinline block : TIME.() -> Unit = {}) : Unit = TIME(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Unordered list
 */
@HtmlTagMarker
inline fun FlowOrPhrasingContent.htmlVar(classes : String? = null, crossinline block : VAR.() -> Unit = {}) : Unit = VAR(attributesMapOf("class", classes), consumer).visit(block)


/**
 * Self-contained syndicatable or reusable composition
 */
@HtmlTagMarker
inline fun SectioningOrFlowContent.article(classes : String? = null, crossinline block : ARTICLE.() -> Unit = {}) : Unit = ARTICLE(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Sidebar for tangentially related content
 */
@HtmlTagMarker
inline fun SectioningOrFlowContent.aside(classes : String? = null, crossinline block : ASIDE.() -> Unit = {}) : Unit = ASIDE(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Container for the dominant contents of another element
 */
@HtmlTagMarker
inline fun SectioningOrFlowContent.main(classes : String? = null, crossinline block : MAIN.() -> Unit = {}) : Unit = MAIN(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Section with navigational links
 */
@HtmlTagMarker
inline fun SectioningOrFlowContent.nav(classes : String? = null, crossinline block : NAV.() -> Unit = {}) : Unit = NAV(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Generic document or application section
 */
@HtmlTagMarker
inline fun SectioningOrFlowContent.section(classes : String? = null, crossinline block : SECTION.() -> Unit = {}) : Unit = SECTION(attributesMapOf("class", classes), consumer).visit(block)


/**
 * Anchor
 */
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.a(href : String? = null, target : String? = null, classes : String? = null, crossinline block : A.() -> Unit = {}) : Unit = A(attributesMapOf("href", href,"target", target,"class", classes), consumer).visit(block)

/**
 * Audio player
 */
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.audio(classes : String? = null, crossinline block : AUDIO.() -> Unit = {}) : Unit = AUDIO(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Push button
 */
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.button(formEncType : ButtonFormEncType? = null, formMethod : ButtonFormMethod? = null, name : String? = null, type : ButtonType? = null, classes : String? = null, crossinline block : BUTTON.() -> Unit = {}) : Unit = BUTTON(attributesMapOf("formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"type", type?.enumEncode(),"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.getButton(formEncType : ButtonFormEncType? = null, name : String? = null, type : ButtonType? = null, classes : String? = null, crossinline block : BUTTON.() -> Unit = {}) : Unit = BUTTON(attributesMapOf("formenctype", formEncType?.enumEncode(),"formmethod", ButtonFormMethod.get.realValue,"name", name,"type", type?.enumEncode(),"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.postButton(formEncType : ButtonFormEncType? = null, name : String? = null, type : ButtonType? = null, classes : String? = null, crossinline block : BUTTON.() -> Unit = {}) : Unit = BUTTON(attributesMapOf("formenctype", formEncType?.enumEncode(),"formmethod", ButtonFormMethod.post.realValue,"name", name,"type", type?.enumEncode(),"class", classes), consumer).visit(block)
@Suppress("DEPRECATION")
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.putButton(formEncType : ButtonFormEncType? = null, name : String? = null, type : ButtonType? = null, classes : String? = null, crossinline block : BUTTON.() -> Unit = {}) : Unit = BUTTON(attributesMapOf("formenctype", formEncType?.enumEncode(),"formmethod", ButtonFormMethod.put.realValue,"name", name,"type", type?.enumEncode(),"class", classes), consumer).visit(block)
@Suppress("DEPRECATION")
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.deleteButton(formEncType : ButtonFormEncType? = null, name : String? = null, type : ButtonType? = null, classes : String? = null, crossinline block : BUTTON.() -> Unit = {}) : Unit = BUTTON(attributesMapOf("formenctype", formEncType?.enumEncode(),"formmethod", ButtonFormMethod.delete.realValue,"name", name,"type", type?.enumEncode(),"class", classes), consumer).visit(block)
@Suppress("DEPRECATION")
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.patchButton(formEncType : ButtonFormEncType? = null, name : String? = null, type : ButtonType? = null, classes : String? = null, crossinline block : BUTTON.() -> Unit = {}) : Unit = BUTTON(attributesMapOf("formenctype", formEncType?.enumEncode(),"formmethod", ButtonFormMethod.patch.realValue,"name", name,"type", type?.enumEncode(),"class", classes), consumer).visit(block)

/**
 * Plugin
 */
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.embed(classes : String? = null, crossinline block : EMBED.() -> Unit = {}) : Unit = EMBED(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Inline subwindow
 */
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.iframe(sandbox : IframeSandbox? = null, classes : String? = null, crossinline block : IFRAME.() -> Unit = {}) : Unit = IFRAME(attributesMapOf("sandbox", sandbox?.enumEncode(),"class", classes), consumer).visit(block)
/**
 * Inline subwindow
 */
@HtmlTagMarker
fun FlowOrInteractiveOrPhrasingContent.iframe(sandbox : IframeSandbox? = null, classes : String? = null, content : String = "") : Unit = IFRAME(attributesMapOf("sandbox", sandbox?.enumEncode(),"class", classes), consumer).visit({+content})
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.allowSameOriginIframe(classes : String? = null, crossinline block : IFRAME.() -> Unit = {}) : Unit = IFRAME(attributesMapOf("sandbox", IframeSandbox.allowSameOrigin.realValue,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.allowFormSIframe(classes : String? = null, crossinline block : IFRAME.() -> Unit = {}) : Unit = IFRAME(attributesMapOf("sandbox", IframeSandbox.allowFormS.realValue,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.allowScriptsIframe(classes : String? = null, crossinline block : IFRAME.() -> Unit = {}) : Unit = IFRAME(attributesMapOf("sandbox", IframeSandbox.allowScripts.realValue,"class", classes), consumer).visit(block)
@HtmlTagMarker
fun FlowOrInteractiveOrPhrasingContent.allowSameOriginIframe(classes : String? = null, content : String = "") : Unit = IFRAME(attributesMapOf("sandbox", IframeSandbox.allowSameOrigin.realValue,"class", classes), consumer).visit({+content})
@HtmlTagMarker
fun FlowOrInteractiveOrPhrasingContent.allowFormSIframe(classes : String? = null, content : String = "") : Unit = IFRAME(attributesMapOf("sandbox", IframeSandbox.allowFormS.realValue,"class", classes), consumer).visit({+content})
@HtmlTagMarker
fun FlowOrInteractiveOrPhrasingContent.allowScriptsIframe(classes : String? = null, content : String = "") : Unit = IFRAME(attributesMapOf("sandbox", IframeSandbox.allowScripts.realValue,"class", classes), consumer).visit({+content})

/**
 * Embedded image
 */
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.img(alt : String? = null, src : String? = null, loading : ImgLoading? = null, classes : String? = null, crossinline block : IMG.() -> Unit = {}) : Unit = IMG(attributesMapOf("alt", alt,"src", src,"loading", loading?.enumEncode(),"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.eagerImg(alt : String? = null, src : String? = null, classes : String? = null, crossinline block : IMG.() -> Unit = {}) : Unit = IMG(attributesMapOf("alt", alt,"src", src,"loading", ImgLoading.eager.realValue,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.lazyImg(alt : String? = null, src : String? = null, classes : String? = null, crossinline block : IMG.() -> Unit = {}) : Unit = IMG(attributesMapOf("alt", alt,"src", src,"loading", ImgLoading.lazy.realValue,"class", classes), consumer).visit(block)

/**
 * Pictures container
 */
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.picture(crossinline block : PICTURE.() -> Unit = {}) : Unit = PICTURE(emptyMap, consumer).visit(block)

/**
 * Form control
 */
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.input(type : InputType? = null, formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", type?.enumEncode(),"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.buttonInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.button.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.checkBoxInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.checkBox.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.colorInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.color.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.dateInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.date.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.dateTimeInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.dateTime.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.dateTimeLocalInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.dateTimeLocal.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.emailInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.email.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.fileInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.file.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.hiddenInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.hidden.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.imageInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.image.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.monthInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.month.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.numberInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.number.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.passwordInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.password.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.radioInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.radio.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.rangeInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.range.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.resetInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.reset.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.searchInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.search.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.submitInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.submit.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.textInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.text.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.telInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.tel.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.timeInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.time.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.urlInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.url.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.weekInput(formEncType : InputFormEncType? = null, formMethod : InputFormMethod? = null, name : String? = null, classes : String? = null, crossinline block : INPUT.() -> Unit = {}) : Unit = INPUT(attributesMapOf("type", InputType.week.realValue,"formenctype", formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes), consumer).visit(block)

/**
 * Cryptographic key-pair generator form control
 */
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.keyGen(keyType : KeyGenKeyType? = null, classes : String? = null, crossinline block : KEYGEN.() -> Unit = {}) : Unit = KEYGEN(attributesMapOf("keytype", keyType?.enumEncode(),"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.rsaKeyGen(classes : String? = null, crossinline block : KEYGEN.() -> Unit = {}) : Unit = KEYGEN(attributesMapOf("keytype", KeyGenKeyType.rsa.realValue,"class", classes), consumer).visit(block)

/**
 * Form field label text
 */
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.label(classes : String? = null, crossinline block : LABEL.() -> Unit = {}) : Unit = LABEL(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Generic embedded object
 */
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.htmlObject(classes : String? = null, crossinline block : OBJECT.() -> Unit = {}) : Unit = OBJECT(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Option selector
 */
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.select(classes : String? = null, crossinline block : SELECT.() -> Unit = {}) : Unit = SELECT(attributesMapOf("class", classes), consumer).visit(block)

/**
 * Multi-line text field
 */
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.textArea(rows : String? = null, cols : String? = null, wrap : TextAreaWrap? = null, classes : String? = null, crossinline block : TEXTAREA.() -> Unit = {}) : Unit = TEXTAREA(attributesMapOf("rows", rows,"cols", cols,"wrap", wrap?.enumEncode(),"class", classes), consumer).visit(block)
/**
 * Multi-line text field
 */
@HtmlTagMarker
fun FlowOrInteractiveOrPhrasingContent.textArea(rows : String? = null, cols : String? = null, wrap : TextAreaWrap? = null, classes : String? = null, content : String = "") : Unit = TEXTAREA(attributesMapOf("rows", rows,"cols", cols,"wrap", wrap?.enumEncode(),"class", classes), consumer).visit({+content})
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.hardTextArea(rows : String? = null, cols : String? = null, classes : String? = null, crossinline block : TEXTAREA.() -> Unit = {}) : Unit = TEXTAREA(attributesMapOf("rows", rows,"cols", cols,"wrap", TextAreaWrap.hard.realValue,"class", classes), consumer).visit(block)
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.softTextArea(rows : String? = null, cols : String? = null, classes : String? = null, crossinline block : TEXTAREA.() -> Unit = {}) : Unit = TEXTAREA(attributesMapOf("rows", rows,"cols", cols,"wrap", TextAreaWrap.soft.realValue,"class", classes), consumer).visit(block)
@HtmlTagMarker
fun FlowOrInteractiveOrPhrasingContent.hardTextArea(rows : String? = null, cols : String? = null, classes : String? = null, content : String = "") : Unit = TEXTAREA(attributesMapOf("rows", rows,"cols", cols,"wrap", TextAreaWrap.hard.realValue,"class", classes), consumer).visit({+content})
@HtmlTagMarker
fun FlowOrInteractiveOrPhrasingContent.softTextArea(rows : String? = null, cols : String? = null, classes : String? = null, content : String = "") : Unit = TEXTAREA(attributesMapOf("rows", rows,"cols", cols,"wrap", TextAreaWrap.soft.realValue,"class", classes), consumer).visit({+content})

/**
 * Video player
 */
@HtmlTagMarker
inline fun FlowOrInteractiveOrPhrasingContent.video(classes : String? = null, crossinline block : VIDEO.() -> Unit = {}) : Unit = VIDEO(attributesMapOf("class", classes), consumer).visit(block)


