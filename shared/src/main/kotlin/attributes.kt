package html4k

import kotlin.properties.ReadWriteProperty

trait AttributeEncoder<T> {
    fun encode(desc: PropertyMetadata, value : T) : String
    fun decode(desc: PropertyMetadata, value : String) : T
}

private abstract class Attribute<T>(val encoder : AttributeEncoder<T>) : ReadWriteProperty<Tag, T> {
    abstract fun name(desc: PropertyMetadata) : String

    override
    fun get(thisRef: Tag, desc: PropertyMetadata) : T =
            thisRef.attributes[name(desc)]?.let {
                encoder.decode(desc, it)
            } ?: throw IllegalStateException("Attribute ${name(desc)} is not yet defined for tag ${thisRef.tagName}")

    override
    fun set(thisRef: Tag, desc: PropertyMetadata, value : T) {
        thisRef.attributes[name(desc)] = encoder.encode(desc, value)
    }
}

object StringEncoder : AttributeEncoder<String> {
    override fun encode(desc: PropertyMetadata, value: String): String = value
    override fun decode(desc: PropertyMetadata, value: String): String = value
}

public class StringAttributeShared : Attribute<String>(StringEncoder) {
    override fun name(desc: PropertyMetadata): String = desc.name
}

public class StringAttribute(val name : String) : Attribute<String>(StringEncoder) {
    override fun name(desc: PropertyMetadata): String = name
}

//public class IntAttribute : Attribute<Int>() {
//    override fun encode(desc: PropertyMetadata, value: Int): String = value.toString()
//    override fun decode(desc: PropertyMetadata, value: String): Int = value.toInt()
//}

fun Boolean.booleanEncode() = toString()
public class BooleanEncoder(val trueValue: String = "true", val falseValue: String = "false") : AttributeEncoder<Boolean> {
    override fun encode(desc: PropertyMetadata, value : Boolean): String = if (value) trueValue else falseValue
    override fun decode(desc: PropertyMetadata, value: String): Boolean = when (value) {
        trueValue -> true
        falseValue -> false
        else -> throw IllegalArgumentException("Unknown value $value for ${desc.name}")
    }
}

public class BooleanAttributeShared : Attribute<Boolean>(BooleanEncoder()) {
    override fun name(desc: PropertyMetadata): String  = desc.name
}

public class BooleanAttribute(val name : String, trueValue: String = "true", falseValue: String = "false") : Attribute<Boolean>(BooleanEncoder(trueValue, falseValue)) {
    override fun name(desc: PropertyMetadata): String = name
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

class EnumEncoder<T : AttributeEnum>(val valuesMap : Map<String, T>) : AttributeEncoder<T> {
    override fun encode(desc: PropertyMetadata, value: T): String = value.realValue
    override fun decode(desc: PropertyMetadata, value: String): T = valuesMap[value] ?: throw IllegalArgumentException("Unknown value $value for ${desc.name}")
}

fun <T : AttributeEnum> T.enumEncode() : String = realValue
public class EnumAttribute<T : AttributeEnum>(val name : String, val values : Map<String, T>) : Attribute<T>(EnumEncoder(values)) {
    override fun name(desc: PropertyMetadata): String = name
}
