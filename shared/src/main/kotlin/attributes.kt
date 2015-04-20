package html4k

import kotlin.properties.ReadWriteProperty

trait AttributeEncoder<T> {
    fun encode(attributeName: String, value : T) : String
    fun decode(attributeName: String, value : String) : T
    fun empty(attributeName: String, tag: Tag) : T = throw IllegalStateException("Attribute $attributeName is not yet defined for tag ${tag.tagName}")
}

private abstract class Attribute<T>(val encoder : AttributeEncoder<T>) {
    fun get(thisRef: Tag, attributeName: String) : T =
            thisRef.attributes[attributeName]?.let {
                encoder.decode(attributeName, it)
            } ?: encoder.empty(attributeName, thisRef)

    fun set(thisRef: Tag, attributeName: String, value : T) {
        thisRef.attributes[attributeName] = encoder.encode(attributeName, value)
    }
}

object StringEncoder : AttributeEncoder<String> {
    override fun encode(attributeName: String, value: String): String = value
    override fun decode(attributeName: String, value: String): String = value
}

data class StringAttribute : Attribute<String>(StringEncoder)

//public class IntAttribute : Attribute<Int>() {
//    override fun encode(desc: PropertyMetadata, value: Int): String = value.toString()
//    override fun decode(desc: PropertyMetadata, value: String): Int = value.toInt()
//}

fun Boolean.booleanEncode() = toString()
class BooleanEncoder(val trueValue: String = "true", val falseValue: String = "false") : AttributeEncoder<Boolean> {
    override fun encode(attributeName: String, value : Boolean): String = if (value) trueValue else falseValue
    override fun decode(attributeName: String, value: String): Boolean = when (value) {
        trueValue -> true
        falseValue -> false
        else -> throw IllegalArgumentException("Unknown value $value for $attributeName")
    }
}

data class BooleanAttribute(trueValue: String = "true", falseValue: String = "false") : Attribute<Boolean>(BooleanEncoder(trueValue, falseValue))

fun Boolean.tickerEncode(attributeName: String) : String = if (this) attributeName else ""
object TickerEncoder : AttributeEncoder<Boolean> {
    override fun encode(attributeName: String, value: Boolean): String = value.tickerEncode(attributeName)
    override fun decode(attributeName: String, value: String): Boolean = value == attributeName
}

public data class TickerAttribute : Attribute<Boolean>(TickerEncoder)

class EnumEncoder<T : AttributeEnum>(val valuesMap : Map<String, T>) : AttributeEncoder<T> {
    override fun encode(attributeName: String, value: T): String = value.realValue
    override fun decode(attributeName: String, value: String): T = valuesMap[value] ?: throw IllegalArgumentException("Unknown value $value for $attributeName")
}

fun <T : AttributeEnum> T.enumEncode() : String = realValue
data class EnumAttribute<T : AttributeEnum>(val values : Map<String, T>) : Attribute<T>(EnumEncoder(values))

fun Set<String>.stringSetEncode() = this.join(" ")
object StringSetEncoder : AttributeEncoder<Set<String>> {
    override fun encode(attributeName: String, value: Set<String>): String = value.join(" ")
    override fun decode(attributeName: String, value: String): Set<String> = value.split("\\s+").filterNot {it.isEmpty()}.toSet()
    override fun empty(attributeName: String, tag: Tag) = emptySet<String>()
}
data class StringSetAttribute : Attribute<Set<String>>(StringSetEncoder)