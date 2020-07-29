package kotlinx.html

import javax.naming.OperationNotSupportedException

actual open class Event {
  actual fun preventDefault() {
    throw OperationNotSupportedException("Event marker is not meant to be used on JVM")
  }

  actual fun initEvent(type: String, bubbles: Boolean, cancelable: Boolean) {
    throw OperationNotSupportedException("Event marker is not meant to be used on JVM")
  }

  actual fun stopPropagation() {
    throw OperationNotSupportedException("Event marker is not meant to be used on JVM")
  }
}
