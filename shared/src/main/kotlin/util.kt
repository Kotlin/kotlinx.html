package html4k

import java.util.LinkedHashSet

public fun <T> Set<T>.plus(value : T) : Set<T> = with(LinkedHashSet(this)) { add(value); this }