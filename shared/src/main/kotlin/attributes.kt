package html4k

import kotlin.properties.ReadWriteProperty


private abstract class Attribute<T>(val name : String) : ReadWriteProperty<Tag, T> {
    protected abstract fun encode(desc: PropertyMetadata, value : T) : String
    protected abstract fun decode(desc: PropertyMetadata, value : String) : T

    override
    fun get(thisRef: Tag, desc: PropertyMetadata) : T =
            thisRef.attributes[name]?.let {
                decode(desc, it)
            } ?: throw IllegalStateException("Attribute ${name} is not yet defined for tag ${thisRef.tagName}")

    override
    fun set(thisRef: Tag, desc: PropertyMetadata, value : T) {
        thisRef.attributes[name] = encode(desc, value)
    }
}

public class StringAttribute(name : String) : Attribute<String>(name) {
    override fun encode(desc: PropertyMetadata, value: String): String = value
    override fun decode(desc: PropertyMetadata, value: String): String = value
}

//public class IntAttribute : Attribute<Int>() {
//    override fun encode(desc: PropertyMetadata, value: Int): String = value.toString()
//    override fun decode(desc: PropertyMetadata, value: String): Int = value.toInt()
//}

fun Boolean.booleanEncode() = toString()
public open class BooleanAttribute(name : String, val trueValue: String = "true", val falseValue: String = "false") : Attribute<Boolean>(name) {
    public override fun encode(desc: PropertyMetadata, value : Boolean): String = if (value) trueValue else falseValue
    public override fun decode(desc: PropertyMetadata, value: String): Boolean = when (value) {
        trueValue -> true
        falseValue -> false
        else -> throw IllegalArgumentException("Unknown value $value for ${desc.name}")
    }
}

//private fun Boolean.tickerEncode(desc: PropertyMetadata) : String = if (this) desc.name else ""
//public class TickerAttribute(name : String) : Attribute<Boolean>(name) {
//    override fun encode(desc: PropertyMetadata, value: Boolean) = value.tickerEncode(desc)
//    override fun decode(desc: PropertyMetadata, value: String): Boolean = when (value) {
//        desc.name -> true
//        "" -> false
//        else -> throw IllegalArgumentException("Unknown value $value for ${desc.name}")
//    }
//}

fun <T : AttributeEnum> T.enumEncode() : String = realValue
public class EnumAttribute<T : AttributeEnum>(name : String, val values : Map<String, T>) : Attribute<T>(name) {
    override fun encode(desc: PropertyMetadata, value: T): String = value.enumEncode()
    override fun decode(desc: PropertyMetadata, value: String): T = values[value] ?: throw IllegalArgumentException("Unknown value $value for ${desc.name}")
}
