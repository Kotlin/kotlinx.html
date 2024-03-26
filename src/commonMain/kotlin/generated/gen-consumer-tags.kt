/*******************************************************************************
    DO NOT EDIT
    This file was generated by module generate
*******************************************************************************/
package kotlinx.html

import kotlinx.html.*
import kotlinx.html.attributes.*

import kotlin.Deprecated
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlinx.html.A
import kotlinx.html.ABBR
import kotlinx.html.ADDRESS
import kotlinx.html.AREA
import kotlinx.html.ARTICLE
import kotlinx.html.ASIDE
import kotlinx.html.AUDIO
import kotlinx.html.AreaShape
import kotlinx.html.B
import kotlinx.html.BASE
import kotlinx.html.BDI
import kotlinx.html.BDO
import kotlinx.html.BLOCKQUOTE
import kotlinx.html.BODY
import kotlinx.html.BR
import kotlinx.html.BUTTON
import kotlinx.html.ButtonFormEncType
import kotlinx.html.ButtonFormMethod
import kotlinx.html.ButtonType
import kotlinx.html.CANVAS
import kotlinx.html.CAPTION
import kotlinx.html.CITE
import kotlinx.html.CODE
import kotlinx.html.COL
import kotlinx.html.COLGROUP
import kotlinx.html.COMMAND
import kotlinx.html.CommandType
import kotlinx.html.DATALIST
import kotlinx.html.DD
import kotlinx.html.DEL
import kotlinx.html.DETAILS
import kotlinx.html.DFN
import kotlinx.html.DIALOG
import kotlinx.html.DIV
import kotlinx.html.DL
import kotlinx.html.DT
import kotlinx.html.EM
import kotlinx.html.EMBED
import kotlinx.html.FIELDSET
import kotlinx.html.FIGCAPTION
import kotlinx.html.FIGURE
import kotlinx.html.FOOTER
import kotlinx.html.FORM
import kotlinx.html.FormEncType
import kotlinx.html.FormMethod
import kotlinx.html.H1
import kotlinx.html.H2
import kotlinx.html.H3
import kotlinx.html.H4
import kotlinx.html.H5
import kotlinx.html.H6
import kotlinx.html.HEAD
import kotlinx.html.HEADER
import kotlinx.html.HGROUP
import kotlinx.html.HR
import kotlinx.html.HTML
import kotlinx.html.HtmlTagMarker
import kotlinx.html.I
import kotlinx.html.IFRAME
import kotlinx.html.IMG
import kotlinx.html.INPUT
import kotlinx.html.INS
import kotlinx.html.IframeSandbox
import kotlinx.html.ImgLoading
import kotlinx.html.InputFormEncType
import kotlinx.html.InputFormMethod
import kotlinx.html.InputType
import kotlinx.html.KBD
import kotlinx.html.KEYGEN
import kotlinx.html.KeyGenKeyType
import kotlinx.html.LABEL
import kotlinx.html.LEGEND
import kotlinx.html.LI
import kotlinx.html.LINK
import kotlinx.html.LinkAs
import kotlinx.html.MAIN
import kotlinx.html.MAP
import kotlinx.html.MARK
import kotlinx.html.MATH
import kotlinx.html.MATHML
import kotlinx.html.META
import kotlinx.html.METER
import kotlinx.html.NAV
import kotlinx.html.NOSCRIPT
import kotlinx.html.OBJECT
import kotlinx.html.OL
import kotlinx.html.OPTGROUP
import kotlinx.html.OPTION
import kotlinx.html.OUTPUT
import kotlinx.html.P
import kotlinx.html.PARAM
import kotlinx.html.PICTURE
import kotlinx.html.PRE
import kotlinx.html.PROGRESS
import kotlinx.html.Q
import kotlinx.html.RP
import kotlinx.html.RT
import kotlinx.html.RUBY
import kotlinx.html.S
import kotlinx.html.SAMP
import kotlinx.html.SCRIPT
import kotlinx.html.SECTION
import kotlinx.html.SELECT
import kotlinx.html.SLOT
import kotlinx.html.SMALL
import kotlinx.html.SOURCE
import kotlinx.html.SPAN
import kotlinx.html.STRONG
import kotlinx.html.STYLE
import kotlinx.html.SUB
import kotlinx.html.SUMMARY
import kotlinx.html.SUP
import kotlinx.html.SVG
import kotlinx.html.ScriptCrossorigin
import kotlinx.html.TABLE
import kotlinx.html.TBODY
import kotlinx.html.TD
import kotlinx.html.TEMPLATE
import kotlinx.html.TEXTAREA
import kotlinx.html.TFOOT
import kotlinx.html.TH
import kotlinx.html.THEAD
import kotlinx.html.TIME
import kotlinx.html.TITLE
import kotlinx.html.TR
import kotlinx.html.TagConsumer
import kotlinx.html.TextAreaWrap
import kotlinx.html.ThScope
import kotlinx.html.U
import kotlinx.html.UL
import kotlinx.html.VAR
import kotlinx.html.VIDEO

/**
 * Anchor
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.a(
  href: String? = null,
  target: String? = null,
  classes: String? = null,
  crossinline block: A.() -> Unit = {},
): T = A(attributesMapOf("href", href,"target", target,"class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Abbreviated form (e.g., WWW, HTTP,etc.)
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.abbr(classes: String? = null, crossinline
    block: ABBR.() -> Unit = {}): T = ABBR(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Information on author
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.address(classes: String? = null, crossinline
    block: ADDRESS.() -> Unit = {}): T = ADDRESS(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Client-side image map area
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.area(
  shape: AreaShape? = null,
  alt: String? = null,
  classes: String? = null,
  crossinline block: AREA.() -> Unit = {},
): T = AREA(attributesMapOf("Shape", shape?.enumEncode(),"alt", alt,"class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Self-contained syndicatable or reusable composition
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.article(classes: String? = null, crossinline
    block: ARTICLE.() -> Unit = {}): T = ARTICLE(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Sidebar for tangentially related content
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.aside(classes: String? = null, crossinline
    block: ASIDE.() -> Unit = {}): T = ASIDE(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Audio player
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.audio(classes: String? = null, crossinline
    block: AUDIO.() -> Unit = {}): T = AUDIO(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Bold text style
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.b(classes: String? = null, crossinline
    block: B.() -> Unit = {}): T = B(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Document base URI
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.base(classes: String? = null, crossinline
    block: BASE.() -> Unit = {}): T = BASE(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Text directionality isolation
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.bdi(classes: String? = null, crossinline
    block: BDI.() -> Unit = {}): T = BDI(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * I18N BiDi over-ride
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.bdo(classes: String? = null, crossinline
    block: BDO.() -> Unit = {}): T = BDO(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Long quotation
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.blockQuote(classes: String? = null, crossinline
    block: BLOCKQUOTE.() -> Unit = {}): T = BLOCKQUOTE(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Document body
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.body(classes: String? = null, crossinline
    block: BODY.() -> Unit = {}): T = BODY(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Forced line break
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.br(classes: String? = null, crossinline
    block: BR.() -> Unit = {}): T = BR(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Push button
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.button(
  formEncType: ButtonFormEncType? = null,
  formMethod: ButtonFormMethod? = null,
  name: String? = null,
  type: ButtonType? = null,
  classes: String? = null,
  crossinline block: BUTTON.() -> Unit = {},
): T = BUTTON(attributesMapOf("formenctype", formEncType?.enumEncode(),"formmethod",
    formMethod?.enumEncode(),"name", name,"type", type?.enumEncode(),"class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Scriptable bitmap canvas
 */
@HtmlTagMarker
public fun <T, C : TagConsumer<T>> C.canvas(classes: String? = null, content: String = ""): T =
    CANVAS(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, {+content})

/**
 * Scriptable bitmap canvas
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.canvas(classes: String? = null, crossinline
    block: CANVAS.() -> Unit = {}): T = CANVAS(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Table caption
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.caption(classes: String? = null, crossinline
    block: CAPTION.() -> Unit = {}): T = CAPTION(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Citation
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.cite(classes: String? = null, crossinline
    block: CITE.() -> Unit = {}): T = CITE(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Computer code fragment
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.code(classes: String? = null, crossinline
    block: CODE.() -> Unit = {}): T = CODE(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Table column
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.col(classes: String? = null, crossinline
    block: COL.() -> Unit = {}): T = COL(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Table column group
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.colGroup(classes: String? = null, crossinline
    block: COLGROUP.() -> Unit = {}): T = COLGROUP(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.command(
  type: CommandType? = null,
  classes: String? = null,
  crossinline block: COMMAND.() -> Unit = {},
): T = COMMAND(attributesMapOf("type", type?.enumEncode(),"class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Container for options for 
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.dataList(classes: String? = null, crossinline
    block: DATALIST.() -> Unit = {}): T = DATALIST(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Definition description
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.dd(classes: String? = null, crossinline
    block: DD.() -> Unit = {}): T = DD(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Deleted text
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.del(classes: String? = null, crossinline
    block: DEL.() -> Unit = {}): T = DEL(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Disclosure control for hiding details
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.details(classes: String? = null, crossinline
    block: DETAILS.() -> Unit = {}): T = DETAILS(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Instance definition
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.dfn(classes: String? = null, crossinline
    block: DFN.() -> Unit = {}): T = DFN(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Dialog box or window
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.dialog(classes: String? = null, crossinline
    block: DIALOG.() -> Unit = {}): T = DIALOG(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Generic language/style container
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.div(classes: String? = null, crossinline
    block: DIV.() -> Unit = {}): T = DIV(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Definition list
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.dl(classes: String? = null, crossinline
    block: DL.() -> Unit = {}): T = DL(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Definition term
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.dt(classes: String? = null, crossinline
    block: DT.() -> Unit = {}): T = DT(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Emphasis
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.em(classes: String? = null, crossinline
    block: EM.() -> Unit = {}): T = EM(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Plugin
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.embed(classes: String? = null, crossinline
    block: EMBED.() -> Unit = {}): T = EMBED(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Form control group
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.fieldSet(classes: String? = null, crossinline
    block: FIELDSET.() -> Unit = {}): T = FIELDSET(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Caption for 
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.figcaption(classes: String? = null, crossinline
    block: FIGCAPTION.() -> Unit = {}): T = FIGCAPTION(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Figure with optional caption
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.figure(classes: String? = null, crossinline
    block: FIGURE.() -> Unit = {}): T = FIGURE(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Footer for a page or section
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.footer(classes: String? = null, crossinline
    block: FOOTER.() -> Unit = {}): T = FOOTER(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Interactive form
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.form(
  action: String? = null,
  encType: FormEncType? = null,
  method: FormMethod? = null,
  classes: String? = null,
  crossinline block: FORM.() -> Unit = {},
): T = FORM(attributesMapOf("action", action,"enctype", encType?.enumEncode(),"method",
    method?.enumEncode(),"class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Heading
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.h1(classes: String? = null, crossinline
    block: H1.() -> Unit = {}): T = H1(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Heading
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.h2(classes: String? = null, crossinline
    block: H2.() -> Unit = {}): T = H2(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Heading
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.h3(classes: String? = null, crossinline
    block: H3.() -> Unit = {}): T = H3(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Heading
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.h4(classes: String? = null, crossinline
    block: H4.() -> Unit = {}): T = H4(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Heading
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.h5(classes: String? = null, crossinline
    block: H5.() -> Unit = {}): T = H5(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Heading
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.h6(classes: String? = null, crossinline
    block: H6.() -> Unit = {}): T = H6(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Document head
 */
@HtmlTagMarker
@Suppress("DEPRECATION")
@Deprecated("This tag doesn't support content or requires unsafe (try unsafe {})")
public fun <T, C : TagConsumer<T>> C.head(content: String = ""): T = HEAD(emptyMap, this)
    .visitAndFinalize(this, {+content})

/**
 * Document head
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.head(crossinline block: HEAD.() -> Unit = {}): T =
    HEAD(emptyMap, this)
    .visitAndFinalize(this, block)

/**
 * Introductory or navigational aids for a page or section
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.`header`(classes: String? = null, crossinline
    block: HEADER.() -> Unit = {}): T = HEADER(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.hGroup(classes: String? = null, crossinline
    block: HGROUP.() -> Unit = {}): T = HGROUP(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Horizontal rule
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.hr(classes: String? = null, crossinline
    block: HR.() -> Unit = {}): T = HR(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Document root element
 */
@HtmlTagMarker
@Suppress("DEPRECATION")
@Deprecated("This tag doesn't support content or requires unsafe (try unsafe {})")
public fun <T, C : TagConsumer<T>> C.html(content: String = "", namespace: String? = null): T =
    HTML(emptyMap, this, namespace)
    .visitAndFinalize(this, {+content})

/**
 * Document root element
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.html(namespace: String? = null, crossinline
    block: HTML.() -> Unit = {}): T = HTML(emptyMap, this, namespace)
    .visitAndFinalize(this, block)

/**
 * Italic text style
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.i(classes: String? = null, crossinline
    block: I.() -> Unit = {}): T = I(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Inline subwindow
 */
@HtmlTagMarker
public fun <T, C : TagConsumer<T>> C.iframe(
  sandbox: IframeSandbox? = null,
  classes: String? = null,
  content: String = "",
): T = IFRAME(attributesMapOf("sandbox", sandbox?.enumEncode(),"class", classes), this)
    .visitAndFinalize(this, {+content})

/**
 * Inline subwindow
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.iframe(
  sandbox: IframeSandbox? = null,
  classes: String? = null,
  crossinline block: IFRAME.() -> Unit = {},
): T = IFRAME(attributesMapOf("sandbox", sandbox?.enumEncode(),"class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Embedded image
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.img(
  alt: String? = null,
  src: String? = null,
  loading: ImgLoading? = null,
  classes: String? = null,
  crossinline block: IMG.() -> Unit = {},
): T = IMG(attributesMapOf("alt", alt,"src", src,"loading", loading?.enumEncode(),"class", classes),
    this)
    .visitAndFinalize(this, block)

/**
 * Form control
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.input(
  type: InputType? = null,
  formEncType: InputFormEncType? = null,
  formMethod: InputFormMethod? = null,
  name: String? = null,
  classes: String? = null,
  crossinline block: INPUT.() -> Unit = {},
): T = INPUT(attributesMapOf("type", type?.enumEncode(),"formenctype",
    formEncType?.enumEncode(),"formmethod", formMethod?.enumEncode(),"name", name,"class", classes),
    this)
    .visitAndFinalize(this, block)

/**
 * Inserted text
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.ins(classes: String? = null, crossinline
    block: INS.() -> Unit = {}): T = INS(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Text to be entered by the user
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.kbd(classes: String? = null, crossinline
    block: KBD.() -> Unit = {}): T = KBD(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Cryptographic key-pair generator form control
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.keyGen(
  keyType: KeyGenKeyType? = null,
  classes: String? = null,
  crossinline block: KEYGEN.() -> Unit = {},
): T = KEYGEN(attributesMapOf("keytype", keyType?.enumEncode(),"class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Form field label text
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.label(classes: String? = null, crossinline
    block: LABEL.() -> Unit = {}): T = LABEL(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Fieldset legend
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.legend(classes: String? = null, crossinline
    block: LEGEND.() -> Unit = {}): T = LEGEND(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * List item
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.li(classes: String? = null, crossinline
    block: LI.() -> Unit = {}): T = LI(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * A media-independent link
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.link(
  href: String? = null,
  rel: String? = null,
  type: String? = null,
  htmlAs: LinkAs? = null,
  crossinline block: LINK.() -> Unit = {},
): T = LINK(attributesMapOf("href", href,"rel", rel,"type", type,"as", htmlAs?.enumEncode()), this)
    .visitAndFinalize(this, block)

/**
 * Container for the dominant contents of another element
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.main(classes: String? = null, crossinline
    block: MAIN.() -> Unit = {}): T = MAIN(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Client-side image map
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.map(
  name: String? = null,
  classes: String? = null,
  crossinline block: MAP.() -> Unit = {},
): T = MAP(attributesMapOf("name", name,"class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Highlight
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.mark(classes: String? = null, crossinline
    block: MARK.() -> Unit = {}): T = MARK(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.math(classes: String? = null, crossinline
    block: MATH.() -> Unit = {}): T = MATH(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

@HtmlTagMarker
public fun <T, C : TagConsumer<T>> C.mathml(classes: String? = null, content: String = ""): T =
    MATHML(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, {+content})

@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.mathml(classes: String? = null, crossinline
    block: MATHML.() -> Unit = {}): T = MATHML(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Generic metainformation
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.meta(
  name: String? = null,
  content: String? = null,
  charset: String? = null,
  crossinline block: META.() -> Unit = {},
): T = META(attributesMapOf("name", name,"content", content,"charset", charset), this)
    .visitAndFinalize(this, block)

/**
 * Gauge
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.meter(classes: String? = null, crossinline
    block: METER.() -> Unit = {}): T = METER(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Section with navigational links
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.nav(classes: String? = null, crossinline
    block: NAV.() -> Unit = {}): T = NAV(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Generic metainformation
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.noScript(classes: String? = null, crossinline
    block: NOSCRIPT.() -> Unit = {}): T = NOSCRIPT(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Generic embedded object
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.htmlObject(classes: String? = null, crossinline
    block: OBJECT.() -> Unit = {}): T = OBJECT(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Ordered list
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.ol(classes: String? = null, crossinline
    block: OL.() -> Unit = {}): T = OL(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Option group
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.optGroup(
  label: String? = null,
  classes: String? = null,
  crossinline block: OPTGROUP.() -> Unit = {},
): T = OPTGROUP(attributesMapOf("label", label,"class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Selectable choice
 */
@HtmlTagMarker
public fun <T, C : TagConsumer<T>> C.option(classes: String? = null, content: String = ""): T =
    OPTION(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, {+content})

/**
 * Selectable choice
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.option(classes: String? = null, crossinline
    block: OPTION.() -> Unit = {}): T = OPTION(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Calculated output value
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.output(classes: String? = null, crossinline
    block: OUTPUT.() -> Unit = {}): T = OUTPUT(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Paragraph
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.p(classes: String? = null, crossinline
    block: P.() -> Unit = {}): T = P(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Named property value
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.`param`(
  name: String? = null,
  `value`: String? = null,
  crossinline block: PARAM.() -> Unit = {},
): T = PARAM(attributesMapOf("name", name,"value", value), this)
    .visitAndFinalize(this, block)

/**
 * Pictures container
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.picture(crossinline block: PICTURE.() -> Unit = {}): T =
    PICTURE(emptyMap, this)
    .visitAndFinalize(this, block)

/**
 * Preformatted text
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.pre(classes: String? = null, crossinline
    block: PRE.() -> Unit = {}): T = PRE(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Progress bar
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.progress(classes: String? = null, crossinline
    block: PROGRESS.() -> Unit = {}): T = PROGRESS(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Short inline quotation
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.q(classes: String? = null, crossinline
    block: Q.() -> Unit = {}): T = Q(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Parenthesis for ruby annotation text
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.rp(classes: String? = null, crossinline
    block: RP.() -> Unit = {}): T = RP(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Ruby annotation text
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.rt(classes: String? = null, crossinline
    block: RT.() -> Unit = {}): T = RT(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Ruby annotation(s)
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.ruby(classes: String? = null, crossinline
    block: RUBY.() -> Unit = {}): T = RUBY(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Strike-through text style
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.s(classes: String? = null, crossinline
    block: S.() -> Unit = {}): T = S(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Sample or quote text style
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.samp(classes: String? = null, crossinline
    block: SAMP.() -> Unit = {}): T = SAMP(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Script statements
 */
@HtmlTagMarker
@Suppress("DEPRECATION")
@Deprecated("This tag doesn't support content or requires unsafe (try unsafe {})")
public fun <T, C : TagConsumer<T>> C.script(
  type: String? = null,
  src: String? = null,
  crossorigin: ScriptCrossorigin? = null,
  content: String = "",
): T = SCRIPT(attributesMapOf("type", type,"src", src,"crossorigin", crossorigin?.enumEncode()),
    this)
    .visitAndFinalize(this, {+content})

/**
 * Script statements
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.script(
  type: String? = null,
  src: String? = null,
  crossorigin: ScriptCrossorigin? = null,
  crossinline block: SCRIPT.() -> Unit = {},
): T = SCRIPT(attributesMapOf("type", type,"src", src,"crossorigin", crossorigin?.enumEncode()),
    this)
    .visitAndFinalize(this, block)

/**
 * Generic document or application section
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.section(classes: String? = null, crossinline
    block: SECTION.() -> Unit = {}): T = SECTION(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Option selector
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.select(classes: String? = null, crossinline
    block: SELECT.() -> Unit = {}): T = SELECT(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.slot(classes: String? = null, crossinline
    block: SLOT.() -> Unit = {}): T = SLOT(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Small text style
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.small(classes: String? = null, crossinline
    block: SMALL.() -> Unit = {}): T = SMALL(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Media source for 
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.source(classes: String? = null, crossinline
    block: SOURCE.() -> Unit = {}): T = SOURCE(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Generic language/style container
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.span(classes: String? = null, crossinline
    block: SPAN.() -> Unit = {}): T = SPAN(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Strong emphasis
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.strong(classes: String? = null, crossinline
    block: STRONG.() -> Unit = {}): T = STRONG(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Style info
 */
@HtmlTagMarker
@Suppress("DEPRECATION")
@Deprecated("This tag doesn't support content or requires unsafe (try unsafe {})")
public fun <T, C : TagConsumer<T>> C.style(type: String? = null, content: String = ""): T =
    STYLE(attributesMapOf("type", type), this)
    .visitAndFinalize(this, {+content})

/**
 * Style info
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.style(type: String? = null, crossinline
    block: STYLE.() -> Unit = {}): T = STYLE(attributesMapOf("type", type), this)
    .visitAndFinalize(this, block)

/**
 * Subscript
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.sub(classes: String? = null, crossinline
    block: SUB.() -> Unit = {}): T = SUB(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Caption for 
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.summary(classes: String? = null, crossinline
    block: SUMMARY.() -> Unit = {}): T = SUMMARY(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Superscript
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.sup(classes: String? = null, crossinline
    block: SUP.() -> Unit = {}): T = SUP(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

@HtmlTagMarker
public fun <T, C : TagConsumer<T>> C.svg(classes: String? = null, content: String = ""): T =
    SVG(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, {+content})

@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.svg(classes: String? = null, crossinline
    block: SVG.() -> Unit = {}): T = SVG(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 *
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.table(classes: String? = null, crossinline
    block: TABLE.() -> Unit = {}): T = TABLE(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Table body
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.tbody(classes: String? = null, crossinline
    block: TBODY.() -> Unit = {}): T = TBODY(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Table data cell
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.td(classes: String? = null, crossinline
    block: TD.() -> Unit = {}): T = TD(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Template
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.template(classes: String? = null, crossinline
    block: TEMPLATE.() -> Unit = {}): T = TEMPLATE(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Multi-line text field
 */
@HtmlTagMarker
public fun <T, C : TagConsumer<T>> C.textArea(
  rows: String? = null,
  cols: String? = null,
  wrap: TextAreaWrap? = null,
  classes: String? = null,
  content: String = "",
): T = TEXTAREA(attributesMapOf("rows", rows,"cols", cols,"wrap", wrap?.enumEncode(),"class",
    classes), this)
    .visitAndFinalize(this, {+content})

/**
 * Multi-line text field
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.textArea(
  rows: String? = null,
  cols: String? = null,
  wrap: TextAreaWrap? = null,
  classes: String? = null,
  crossinline block: TEXTAREA.() -> Unit = {},
): T = TEXTAREA(attributesMapOf("rows", rows,"cols", cols,"wrap", wrap?.enumEncode(),"class",
    classes), this)
    .visitAndFinalize(this, block)

/**
 * Table footer
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.tfoot(classes: String? = null, crossinline
    block: TFOOT.() -> Unit = {}): T = TFOOT(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Table header cell
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.th(
  scope: ThScope? = null,
  classes: String? = null,
  crossinline block: TH.() -> Unit = {},
): T = TH(attributesMapOf("scope", scope?.enumEncode(),"class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Table header
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.thead(classes: String? = null, crossinline
    block: THEAD.() -> Unit = {}): T = THEAD(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Machine-readable equivalent of date- or time-related data
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.time(classes: String? = null, crossinline
    block: TIME.() -> Unit = {}): T = TIME(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Document title
 */
@HtmlTagMarker
public fun <T, C : TagConsumer<T>> C.title(content: String = ""): T = TITLE(emptyMap, this)
    .visitAndFinalize(this, {+content})

/**
 * Document title
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.title(crossinline block: TITLE.() -> Unit = {}): T =
    TITLE(emptyMap, this)
    .visitAndFinalize(this, block)

/**
 * Table row
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.tr(classes: String? = null, crossinline
    block: TR.() -> Unit = {}): T = TR(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Underlined text style
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.u(classes: String? = null, crossinline
    block: U.() -> Unit = {}): T = U(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Unordered list
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.ul(classes: String? = null, crossinline
    block: UL.() -> Unit = {}): T = UL(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Unordered list
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.htmlVar(classes: String? = null, crossinline
    block: VAR.() -> Unit = {}): T = VAR(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)

/**
 * Video player
 */
@HtmlTagMarker
public inline fun <T, C : TagConsumer<T>> C.video(classes: String? = null, crossinline
    block: VIDEO.() -> Unit = {}): T = VIDEO(attributesMapOf("class", classes), this)
    .visitAndFinalize(this, block)
