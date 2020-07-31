package kotlinx.html

import kotlinx.wasm.jsinterop.*

actual open class Event(arena: Arena, index: Object) : JsValue(arena, index) {
  actual fun preventDefault() {
    js_preventDefault(arena, index)
  }
  
  actual fun initEvent(type: String, bubbles: Boolean, cancelable: Boolean) {
    js_initEvent(
      arena, index,
      stringPointer(type), stringLengthBytes(type),
      bubbles.compareTo(true), cancelable.compareTo(true)
    )
  }
  
  actual fun stopPropagation() {
    js_stopPropagation(arena, index)
  }
}

@SymbolName("kotlinx_Event_preventDefault")
private external fun js_preventDefault(
  arena: Arena,
  index: Object,
)

@SymbolName("kotlinx_Event_stopPropagation")
private external fun js_stopPropagation(
  arena: Arena,
  index: Object,
)

@SymbolName("kotlinx_Event_initEvent")
private external fun js_initEvent(
  arena: Arena,
  index: Object,
  typePtr: Pointer,
  typeLen: Int,
  bubbles: Int,
  cancelable: Int,
)
