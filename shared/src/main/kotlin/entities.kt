package html4k

import kotlin.properties.Delegates

// TODO to be generated

enum class Entities {
    nbsp lt gt

    val x by Delegates.lazy {  }
    val text = "&" + this.toString() + ";"
}