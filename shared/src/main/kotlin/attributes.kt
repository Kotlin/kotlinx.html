package html4k

import kotlin.properties.ReadWriteProperty


private abstract class Attribute<T> : ReadWriteProperty<Tag, T> {
    protected abstract fun encode(desc: PropertyMetadata, value : T) : String
    protected abstract fun decode(desc: PropertyMetadata, value : String) : T

    override
    fun get(thisRef: Tag, desc: PropertyMetadata) : T =
            thisRef.attributes[desc.name]?.let {
                decode(desc, it)
            } ?: throw IllegalStateException("Attribute ${desc.name} is not yet defined for tag ${thisRef.name}")

    override
    fun set(thisRef: Tag, desc: PropertyMetadata, value : T) {
        thisRef.attributes[desc.name] = encode(desc, value)
    }
}

public class StringAttribute : Attribute<String>() {
    override fun encode(desc: PropertyMetadata, value: String): String = value
    override fun decode(desc: PropertyMetadata, value: String): String = value
}

//public class IntAttribute : Attribute<Int>() {
//    override fun encode(desc: PropertyMetadata, value: Int): String = value.toString()
//    override fun decode(desc: PropertyMetadata, value: String): Int = value.toInt()
//}

public open class BooleanAttribute(val trueValue: String = "true", val falseValue: String = "false") : Attribute<Boolean>() {
    public override fun encode(desc: PropertyMetadata, value : Boolean): String = if (value) trueValue else falseValue
    public override fun decode(desc: PropertyMetadata, value: String): Boolean = when (value) {
        trueValue -> true
        falseValue -> false
        else -> throw IllegalArgumentException("Unknown value $value for ${desc.name}")
    }
}

public class TickerAttribute : Attribute<Boolean>() {
    override fun encode(desc: PropertyMetadata, value: Boolean) = if (value) desc.name else ""
    override fun decode(desc: PropertyMetadata, value: String): Boolean = when (value) {
        desc.name -> true
        "" -> false
        else -> throw IllegalArgumentException("Unknown value $value for ${desc.name}")
    }
}

public class EnumAttribute<T : Enum<T>>(val values : Map<String, T>) : Attribute<T>() {
    override fun encode(desc: PropertyMetadata, value: T): String = value.name()
    override fun decode(desc: PropertyMetadata, value: String): T = values[value] ?: throw IllegalArgumentException("Unknown value $value for ${desc.name}")
}
