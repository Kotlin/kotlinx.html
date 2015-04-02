package html4k

enum class Targets {
    _blank
    _self
    _parent
    _top
}

val targetsValues = Targets.values().toMap { it.name() }