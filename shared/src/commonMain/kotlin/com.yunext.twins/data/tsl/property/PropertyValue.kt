package com.yunext.twins.data.tsl.property

import com.yunext.twins.data.tsl.TslException

sealed interface PropertyValue<KEY : PropertyKey> : DisplayValue<String> {
    val key: KEY
}

interface DisplayValue<T : Any> {
    val displayValue: T
}

class IntPropertyValue(override val key: IntPropertyKey, val value: Int?) :
    PropertyValue<IntPropertyKey> {
    override val displayValue: String
        get() = "${value ?: 0}"

    companion object {
        val EMPTY = IntPropertyValue(IntPropertyKey.EMPTY, null)
        fun createEmptyValue(source: Int) = IntPropertyValue(IntPropertyKey.EMPTY, source)
    }
}

class BoolPropertyValue(
    override val key: BooleanPropertyKey, val value: Boolean?
) :
    PropertyValue<BooleanPropertyKey> {
    override val displayValue: String
        get() = "${value ?: false}"

    data class KeyValue(val value: Int, val desc: String) {
        companion object {
            val EMPTY = KeyValue(-_NAN_, "")
        }
    }

    companion object {
        internal const val _TRUE_ = 1
        internal const val _FALSE_ = 0
        internal const val _NAN_ = -999
    }
}

val BoolPropertyValue.KeyValue.boolValue: Boolean
    get() = this.value == BoolPropertyValue._TRUE_

val BoolPropertyValue.keyValues
    get() = this.key.specs.map {
        BoolPropertyValue.KeyValue(it.first, it.second)
    }

fun BoolPropertyValue.from(source: Boolean?): BoolPropertyValue.KeyValue {
    // 默认0：失败 1：成功
    val specs = key.specs
    val trueInt = BoolPropertyValue._TRUE_
    val falseInt = BoolPropertyValue._FALSE_
    return when (source) {
        true -> BoolPropertyValue.KeyValue(trueInt, specs.singleOrNull { p ->
            p.first == trueInt
        }?.second ?: "")
        false -> BoolPropertyValue.KeyValue(falseInt, specs.singleOrNull { p ->
            p.first == falseInt
        }?.second ?: "")
        null -> BoolPropertyValue.KeyValue.EMPTY
    }
}

fun BoolPropertyValue.KeyValue.isEmpty(): Boolean {
    return this == BoolPropertyValue.KeyValue.EMPTY
}

class FloatPropertyValue(override val key: FloatPropertyKey, val value: Float?) :
    PropertyValue<FloatPropertyKey> {
    override val displayValue: String
        get() = "${value ?: 0f}"

    companion object {
        fun createEmptyValue(source: Float) = FloatPropertyValue(FloatPropertyKey.EMPTY, source)
    }
}

class DoublePropertyValue(override val key: DoublePropertyKey, val value: Double?) :
    PropertyValue<DoublePropertyKey> {
    override val displayValue: String
        get() = "${value ?: 0.0}"

    companion object {
        fun createEmptyValue(source: Double) = DoublePropertyValue(DoublePropertyKey.EMPTY, source)
    }
}

class TextPropertyValue(override val key: TextPropertyKey, val value: String?) :
    PropertyValue<TextPropertyKey> {
    override val displayValue: String
        get() = value ?: ""

    companion object {
        fun createEmptyValue(source: String) = TextPropertyValue(TextPropertyKey.EMPTY, source)
    }
}

class DatePropertyValue(override val key: DatePropertyKey, val value: String?) :
    PropertyValue<DatePropertyKey> {
    override val displayValue: String
        get() = value ?: ""
}

class IntEnumPropertyValue(override val key: IntEnumPropertyKey, val keyValue: KeyValue) :
    PropertyValue<IntEnumPropertyKey> {
    override val displayValue: String
        get() = if (isEmpty()) "" else "[${keyValue.value}]${keyValue.desc}"

    data class KeyValue(val value: Int, val desc: String) {
        companion object {
            internal val EMPTY = KeyValue(-999, "")
        }
    }

    companion object {
        fun from(key: IntEnumPropertyKey, value: Int? = null) =
            IntEnumPropertyValue(key, key.specs.singleOrNull {
                value == it.value
            } ?: KeyValue.EMPTY)
    }
}

fun IntEnumPropertyValue.isEmpty(): Boolean {
    return keyValue == IntEnumPropertyValue.KeyValue.EMPTY
}

fun IntEnumPropertyValue.KeyValue.isEmpty(): Boolean {
    return this == IntEnumPropertyValue.KeyValue.EMPTY
}

fun IntEnumPropertyValue.keyValues(): List<IntEnumPropertyValue.KeyValue> {
    return this.key.specs
}

class TextEnumPropertyValue(override val key: TextEnumPropertyKey, val keyValue: KeyValue) :
    PropertyValue<TextEnumPropertyKey> {
    override val displayValue: String
        get() = if (isEmpty()) "" else "[${keyValue.value}]${keyValue.desc}"

    data class KeyValue(val value: String, val desc: String) {
        companion object {
            internal val EMPTY = KeyValue("", "")
        }
    }

    companion object {
        fun from(key: TextEnumPropertyKey, value: String? = null) =
            TextEnumPropertyValue(key, key.specs.singleOrNull {
                value == it.value
            } ?: KeyValue.EMPTY)
    }

}

fun TextEnumPropertyValue.isEmpty(): Boolean {
    return keyValue == TextEnumPropertyValue.KeyValue.EMPTY
}

fun TextEnumPropertyValue.KeyValue.isEmpty(): Boolean {
    return this == TextEnumPropertyValue.KeyValue.EMPTY
}

fun TextEnumPropertyValue.keyValues(): List<TextEnumPropertyValue.KeyValue> {
    return this.key.specs
}

sealed class ArrayPropertyValue(
    override val key: ArrayPropertyKey,
) : PropertyValue<ArrayPropertyKey>

class IntArrayPropertyValue(
    override val key: IntArrayPropertyKey,
    val value: List<Int>
) : ArrayPropertyValue(key) {
    override val displayValue: String
        get() = if (value.isEmpty()) "[]" else "[\n" + value.joinToString(",") + "\n]"
}

class FloatArrayPropertyValue(
    override val key: FloatArrayPropertyKey,
    val value: List<Float>
) : ArrayPropertyValue(key) {
    override val displayValue: String
        get() = if (value.isEmpty()) "[]" else "[\n" + value.joinToString(",") + "\n]"
}

class DoubleArrayPropertyValue(
    override val key: DoubleArrayPropertyKey,
    val value: List<Double>
) : ArrayPropertyValue(key) {
    override val displayValue: String
        get() = if (value.isEmpty()) "[]" else "[\n" + value.joinToString(",") + "\n]"

}


class TextArrayPropertyValue(
    override val key: TextArrayPropertyKey,
    val value: List<String>
) : ArrayPropertyValue(key) {
    override val displayValue: String
        get() = if (value.isEmpty()) "[]" else "[\n" + value.joinToString(",") + "\n]"
}

class StructArrayPropertyValue(
    override val key: StructArrayPropertyKey,
    val value: List<StructPropertyValue>
) : ArrayPropertyValue(key) {
    override val displayValue: String
        get() = if (value.isEmpty()) "[]" else "[\n" + value.joinToString("") {
            "${it.displayValue}\n"
        } + "\n]"
}

class StructPropertyValue(
    override val key: StructPropertyKey,
    val itemValues: Map<PropertyKey, PropertyValue<*>>
) : PropertyValue<StructPropertyKey>, DisplayValue<String> {
    override val displayValue: String
        get() = "{\n" + itemValues.values.map { v ->
            "${v.key.identifier} : " +
                    when (v) {
                        is DoubleArrayPropertyValue -> throw TslException("StructPropertyValue属性值${this.key.type} 不支持 ${v.key.type}")
                        is FloatArrayPropertyValue -> throw TslException("StructPropertyValue属性值${this.key.type} 不支持 ${v.key.type}")
                        is IntArrayPropertyValue -> throw TslException("StructPropertyValue属性值${this.key.type} 不支持 ${v.key.type}")
                        is TextArrayPropertyValue -> throw TslException("StructPropertyValue属性值${this.key.type} 不支持 ${v.key.type}")
                        is BoolPropertyValue -> v.displayValue
                        is DatePropertyValue -> v.displayValue
                        is DoublePropertyValue -> v.displayValue
                        is FloatPropertyValue -> v.displayValue
                        is IntEnumPropertyValue -> v.displayValue
                        is IntPropertyValue -> v.displayValue
                        is StructPropertyValue -> throw TslException("StructPropertyValue属性值${this.key.type} 不支持 ${v.key.type}")
                        is TextEnumPropertyValue -> v.displayValue
                        is TextPropertyValue -> v.displayValue
                        is StructArrayPropertyValue -> throw TslException("StructPropertyValue属性值${this.key.type} 不支持 ${v.key.type}")
                    } + ","
        }.toList().joinToString("\n") + "\n}"
}


