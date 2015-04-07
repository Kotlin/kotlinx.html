package html4k

import kotlin.properties.ReadWriteProperty

trait AttributeEncoder<T> {
    fun encode(attributeName: String, value : T) : String
    fun decode(attributeName: String, value : String) : T
}

private abstract class Attribute<T>(val encoder : AttributeEncoder<T>) {
    fun get(thisRef: Tag, attributeName: String) : T =
            thisRef.attributes[attributeName]?.let {
                encoder.decode(attributeName, it)
            } ?: throw IllegalStateException("Attribute $attributeName is not yet defined for tag ${thisRef.tagName}")

    fun set(thisRef: Tag, attributeName: String, value : T) {
        thisRef.attributes[attributeName] = encoder.encode(attributeName, value)
    }
}

object StringEncoder : AttributeEncoder<String> {
    override fun encode(attributeName: String, value: String): String = value
    override fun decode(attributeName: String, value: String): String = value
}

public class StringAttributeShared : Attribute<String>(StringEncoder) {
}

public class StringAttribute(val name : String) : Attribute<String>(StringEncoder) {
}

//public class IntAttribute : Attribute<Int>() {
//    override fun encode(desc: PropertyMetadata, value: Int): String = value.toString()
//    override fun decode(desc: PropertyMetadata, value: String): Int = value.toInt()
//}

fun Boolean.booleanEncode() = toString()
public class BooleanEncoder(val trueValue: String = "true", val falseValue: String = "false") : AttributeEncoder<Boolean> {
    override fun encode(attributeName: String, value : Boolean): String = if (value) trueValue else falseValue
    override fun decode(attributeName: String, value: String): Boolean = when (value) {
        trueValue -> true
        falseValue -> false
        else -> throw IllegalArgumentException("Unknown value $value for $attributeName")
    }
}

public class BooleanAttributeShared : Attribute<Boolean>(BooleanEncoder()) {
}

public class BooleanAttribute(val name : String, trueValue: String = "true", falseValue: String = "false") : Attribute<Boolean>(BooleanEncoder(trueValue, falseValue)) {
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
    override fun encode(attributeName: String, value: T): String = value.realValue
    override fun decode(attributeName: String, value: String): T = valuesMap[value] ?: throw IllegalArgumentException("Unknown value $value for $attributeName")
}

fun <T : AttributeEnum> T.enumEncode() : String = realValue
public class EnumAttribute<T : AttributeEnum>(val name : String, val values : Map<String, T>) : Attribute<T>(EnumEncoder(values)) {
}
