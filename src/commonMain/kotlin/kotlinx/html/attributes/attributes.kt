package kotlinx.html.attributes

import kotlinx.html.AttributeEnum
import kotlinx.html.Tag

interface AttributeEncoder<T> {
    fun encode(attributeName: String, value: T): String
    fun decode(attributeName: String, value: String): T
    fun empty(attributeName: String, tag: Tag<*>): T =
        throw IllegalStateException("Attribute $attributeName is not yet defined for tag ${tag.tagName}")
}

abstract class Attribute<T>(val encoder: AttributeEncoder<T>) {
    open operator fun get(thisRef: Tag<*>, attributeName: String): T =
        thisRef.attributes[attributeName]?.let {
            encoder.decode(attributeName, it)
        } ?: encoder.empty(attributeName, thisRef)
    
    open operator fun set(thisRef: Tag<*>, attributeName: String, value: T) {
        thisRef.attributes[attributeName] = encoder.encode(attributeName, value)
    }
}

object StringEncoder : AttributeEncoder<String> {
    override fun encode(attributeName: String, value: String): String = value
    override fun decode(attributeName: String, value: String): String = value
}

class StringAttribute : Attribute<String>(StringEncoder)

//public class IntAttribute : Attribute<Int>() {
//    override fun encode(desc: PropertyMetadata, value: Int): String = value.toString()
//    override fun decode(desc: PropertyMetadata, value: String): Int = value.toInt()
//}

fun Boolean.booleanEncode() = toString()
class BooleanEncoder(val trueValue: String = "true", val falseValue: String = "false") : AttributeEncoder<Boolean> {
    override fun encode(attributeName: String, value: Boolean): String = if (value) trueValue else falseValue
    override fun decode(attributeName: String, value: String): Boolean = when (value) {
        trueValue -> true
        falseValue -> false
        else -> throw IllegalArgumentException("Unknown value $value for $attributeName")
    }
}

class BooleanAttribute(trueValue: String = "true", falseValue: String = "false") :
    Attribute<Boolean>(BooleanEncoder(trueValue, falseValue))

fun Boolean.tickerEncode(attributeName: String): String = if (this) attributeName else ""

object TickerEncoder : AttributeEncoder<Boolean> {
    override fun encode(attributeName: String, value: Boolean): String = value.tickerEncode(attributeName)
    override fun decode(attributeName: String, value: String): Boolean = value == attributeName
}

class TickerAttribute : Attribute<Boolean>(TickerEncoder) {
    override fun set(thisRef: Tag<*>, attributeName: String, value: Boolean) {
        if (value) {
            thisRef.attributes[attributeName] = attributeName
        } else {
            thisRef.attributes.remove(attributeName)
        }
    }
}

class EnumEncoder<T : AttributeEnum>(val valuesMap: Map<String, T>) : AttributeEncoder<T> {
    override fun encode(attributeName: String, value: T): String = value.realValue
    override fun decode(attributeName: String, value: String): T =
        valuesMap[value] ?: throw IllegalArgumentException("Unknown value $value for $attributeName")
}

fun AttributeEnum.enumEncode(): String = realValue
class EnumAttribute<T : AttributeEnum>(val values: Map<String, T>) : Attribute<T>(EnumEncoder(values))

fun stringSetDecode(value: String?): Set<String>? = value?.split("\\s+".toRegex())?.filterNot { it.isEmpty() }?.toSet()
fun Set<String>.stringSetEncode() = joinToString(" ")

object StringSetEncoder : AttributeEncoder<Set<String>> {
    override fun encode(attributeName: String, value: Set<String>): String = value.joinToString(" ")
    override fun decode(attributeName: String, value: String): Set<String> = stringSetDecode(value)!!
    override fun empty(attributeName: String, tag: Tag<*>) = emptySet<String>()
}

class StringSetAttribute : Attribute<Set<String>>(StringSetEncoder)
