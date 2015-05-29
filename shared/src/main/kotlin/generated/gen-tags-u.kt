package html4k

import html4k.*
import html4k.impl.*
import html4k.attributes.*

/*******************************************************************************
    DO NOT EDIT
    This file was generated by module generate
*******************************************************************************/

public open class UL(initialAttributes : Map<String, String>, override val consumer : TagConsumer<*>) : HTMLTag("ul", consumer, initialAttributes), CommonAttributeGroupFacade {

}
fun UL.li(classes : String? = null, block : LI.() -> Unit = {}) : Unit = buildLI(listOf("class" to stringSetDecode(classes)?.stringSetEncode()).toAttributesMap(), consumer, block)


