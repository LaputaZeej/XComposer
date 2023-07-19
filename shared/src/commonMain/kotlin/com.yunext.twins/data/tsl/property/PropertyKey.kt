package com.yunext.twins.data.tsl.property

import com.yunext.twins.data.tsl.TslPropertyType
import com.yunext.twins.util.currentTime

sealed interface PropertyKey {
    /* 属性读写类型：只读（r）或读写（rw） */
    val accessMode: String

    /* 是否是标准功能的必选属性（保留）*/
    val required: Boolean
    val desc: String
    val identifier: String
    val name: String
    val item: Boolean
        get() = false
    val type: TslPropertyType
}

class IntPropertyKey(
    override val item: Boolean = false,
    override val accessMode: String,
    override val required: Boolean,
    override val desc: String,
    override val identifier: String,
    override val name: String,
    val max: Int,
    val min: Int,
    val unit: String,
    val step: String
) : PropertyKey {
    override val type: TslPropertyType
        get() = TslPropertyType.INT

    companion object {
        internal val EMPTY = IntPropertyKey(false, "", false, "", "", "", 0, 0, "", "")


    }
}

class FloatPropertyKey(
    override val item: Boolean = false,
    override val accessMode: String,
    override val required: Boolean,
    override val desc: String,
    override val identifier: String,
    override val name: String,
    val max: Float, val min: Float, val unit: String, val step: String
) : PropertyKey {
    override val type: TslPropertyType
        get() = TslPropertyType.FLOAT

    companion object {
        internal val EMPTY = FloatPropertyKey(false, "", false, "", "", "", 0f, 0f, "", "")
    }
}

class DoublePropertyKey(
    override val item: Boolean = false,
    override val accessMode: String,
    override val required: Boolean,
    override val desc: String,
    override val identifier: String,
    override val name: String,
    val max: Double, val min: Double, val unit: String, val step: String
) : PropertyKey {
    override val type: TslPropertyType
        get() = TslPropertyType.DOUBLE

    companion object {
        internal val EMPTY = DoublePropertyKey(false, "", false, "", "", "", 0.0, 0.0, "", "")
    }
}

class TextPropertyKey(
    override val item: Boolean = false,
    override val accessMode: String,
    override val required: Boolean,
    override val desc: String,
    override val identifier: String,
    override val name: String,
    val length: Int
) : PropertyKey {
    override val type: TslPropertyType
        get() = TslPropertyType.TEXT

    companion object {
        internal val EMPTY = TextPropertyKey(false, "", false, "", "", "", 0)
    }
}

class DatePropertyKey(
    override val item: Boolean = false,
    override val accessMode: String,
    override val required: Boolean,
    override val desc: String,
    override val identifier: String,
    override val name: String,
) : PropertyKey {
    override val type: TslPropertyType
        get() = TslPropertyType.DATE

    companion object {
        internal val EMPTY = DatePropertyKey(false, "", false, "", "", "")
    }
}

class BooleanPropertyKey(
    override val item: Boolean = false,
    override val accessMode: String,
    override val required: Boolean,
    override val desc: String,
    override val identifier: String,
    override val name: String,
    val specs: List<Pair<Int, String>>
) : PropertyKey {
    override val type: TslPropertyType
        get() = TslPropertyType.BOOL
}


sealed class EnumPropertyKey() : PropertyKey {
    override val type: TslPropertyType
        get() = TslPropertyType.INT
    abstract val enumType: TslPropertyType

}

class IntEnumPropertyKey(
    override val item: Boolean = false,
    override val accessMode: String,
    override val required: Boolean,
    override val desc: String,
    override val identifier: String,
    override val name: String,
    val specs: List<IntEnumPropertyValue.KeyValue>
) : EnumPropertyKey() {
    override val enumType = TslPropertyType.INT
    override val type: TslPropertyType
        get() = TslPropertyType.ENUM


}

class TextEnumPropertyKey(
    override val item: Boolean = false,
    override val accessMode: String,
    override val required: Boolean,
    override val desc: String,
    override val identifier: String,
    override val name: String,
    val specs: List<TextEnumPropertyValue.KeyValue>
) : EnumPropertyKey() {
    override val enumType = TslPropertyType.TEXT
    override val type: TslPropertyType
        get() = TslPropertyType.ENUM
}

sealed class ArrayPropertyKey(
    override val item: Boolean = false,
    override val accessMode: String,
    override val required: Boolean,
    override val desc: String,
    override val identifier: String,
    override val name: String,
    val size: Int
) : PropertyKey {
    override val type: TslPropertyType
        get() = TslPropertyType.ARRAY
    abstract val itemType: TslPropertyType
}

class IntArrayPropertyKey(
    item: Boolean = false,
    accessMode: String,
    required: Boolean,
    desc: String,
    identifier: String,
    name: String,
    size: Int,
//    val items: List<PropertyKey>
) : ArrayPropertyKey(item, accessMode, required, desc, identifier, name, size) {
    override val itemType: TslPropertyType = TslPropertyType.INT
}

class FloatArrayPropertyKey(

    item: Boolean = false,
    accessMode: String,
    required: Boolean,
    desc: String,
    identifier: String,
    name: String,
    size: Int,
) : ArrayPropertyKey(item, accessMode, required, desc, identifier, name, size) {
    override val itemType: TslPropertyType = TslPropertyType.FLOAT
}


class DoubleArrayPropertyKey(

    item: Boolean = false,
    accessMode: String,
    required: Boolean,
    desc: String,
    identifier: String,
    name: String,
    size: Int,
) : ArrayPropertyKey(item, accessMode, required, desc, identifier, name, size) {
    override val itemType: TslPropertyType = TslPropertyType.DOUBLE
}


class TextArrayPropertyKey(
    item: Boolean = false,
    accessMode: String,
    required: Boolean,
    desc: String,
    identifier: String,
    name: String,
    size: Int,
) : ArrayPropertyKey(item, accessMode, required, desc, identifier, name, size) {
    override val itemType: TslPropertyType = TslPropertyType.TEXT
}

class StructArrayPropertyKey(
    item: Boolean = false,
    accessMode: String,
    required: Boolean,
    desc: String,
    identifier: String,
    name: String,
    size: Int,
    val itemKeys: List<PropertyKey>
) : ArrayPropertyKey(item, accessMode, required, desc, identifier, name, size) {
    override val itemType: TslPropertyType = TslPropertyType.STRUCT
}


class StructPropertyKey(
    override val accessMode: String,
    override val required: Boolean,
    override val desc: String,
    override val identifier: String,
    override val name: String,
    val items: List<PropertyKey>,
    /*构造出来的*/
    val fake: Boolean = false
) : PropertyKey {
    override val type: TslPropertyType
        get() = TslPropertyType.STRUCT

    companion object {
        fun fake(items: List<PropertyKey>): StructPropertyKey {
            return StructPropertyKey(
                accessMode = "rw",
                required = true,
                desc = "fake_StructArrayPropertyKey_desc",
                identifier = "fake_StructArrayPropertyKey_identifier_${currentTime()}",
                name = "fake_StructArrayPropertyKey_name",
                items = items,
                fake = true
            )
        }
    }

}

val PropertyKey.display: String
    get() {
        return "{\n" +
                "   [identifier]$identifier\n" +
                "   [name]$name\n" +
                "   [type]${type.text}\n" +
                "   [spec]$specDisplay\n" +
                "\n}"
    }

val PropertyKey.specDisplay: String
    get() {
        return when (this) {
            is DoubleArrayPropertyKey -> "[大小]${this.size}\n[子类型]${this.itemType.text}"
            is FloatArrayPropertyKey -> "[大小]${this.size}\n[类型]${this.itemType.text}"
            is IntArrayPropertyKey -> "[大小]${this.size}\n[类型]${this.itemType.text}"
            is StructArrayPropertyKey -> "[大小]${this.size}\n[类型]${this.itemType.text}" // todo
            is TextArrayPropertyKey -> "[大小]${this.size}\n[类型]${this.itemType.text}"
            is BooleanPropertyKey -> "[取值范围]\n${
                this.specs.map {
                    "[${it.first}]:${it.second}"
                }.joinToString("\n")
            }"
            is DatePropertyKey -> "[说明]\nString类型UTC毫秒"
            is DoublePropertyKey -> "[最大值]${this.max}\n[最小值]${this.min}\n[步长]${this.step}\n[单位]${this.unit}"
            is IntEnumPropertyKey -> "[子类型]${this.enumType}\n[取值范围]\n${
                this.specs.map {
                    "   [${it.value}]:${it.desc}"
                }.joinToString("\n")
            }"
            is TextEnumPropertyKey -> "[子类型]${this.enumType}\n[取值范围]\n${
                this.specs.map {
                    "   [${it.value}]:${it.desc}"
                }.joinToString("\n")
            }"
            is FloatPropertyKey -> "[最大值]${this.max}\n[最小值]${this.min}\n[步长]${this.step}\n[单位]${this.unit}"
            is IntPropertyKey -> "[最大值]${this.max}\n[最小值]${this.min}\n[步长]${this.step}\n[单位]${this.unit}"
            is StructPropertyKey -> "结构： ${
                this.items.map {
                    "[${it.identifier}]:${it.type}"
                }.joinToString("\n")
            }"
            is TextPropertyKey -> "[最大长度]${this.length}"
        }
    }

val PropertyKey.specDisplayDebug: String
    get() {
        return when (this) {
            is DoubleArrayPropertyKey -> "[大小]${this.size}\n[子类型]${this.itemType.text}"
            is FloatArrayPropertyKey -> "[大小]${this.size}\n[类型]${this.itemType.text}"
            is IntArrayPropertyKey -> "[大小]${this.size}\n[类型]${this.itemType.text}"
            is StructArrayPropertyKey -> "[大小]${this.size}\n[类型]${this.itemType.text}" // todo
            is TextArrayPropertyKey -> "[大小]${this.size}\n[类型]${this.itemType.text}"
            is BooleanPropertyKey -> "[取值范围]\n${
                this.specs.map {
                    "[${it.first}]:${it.second}"
                }.joinToString("\n")
            }"
            is DatePropertyKey -> "[说明]\nString类型UTC毫秒"
            is DoublePropertyKey -> "[最大值]${this.max}\n[最小值]${this.min}\n[步长]${this.step}\n[单位]${this.unit}"
            is IntEnumPropertyKey -> "[子类型]${this.enumType}\n[取值范围]\n${
                this.specs.map {
                    "   [${it.value}]:${it.desc}"
                }.joinToString("\n")
            }"
            is TextEnumPropertyKey -> "[子类型]${this.enumType}\n[取值范围]\n${
                this.specs.map {
                    "   [${it.value}]:${it.desc}"
                }.joinToString("\n")
            }"
            is FloatPropertyKey -> "[最大值]${this.max}\n[最小值]${this.min}\n[步长]${this.step}\n[单位]${this.unit}"
            is IntPropertyKey -> "[最大值]${this.max}\n[最小值]${this.min}\n[步长]${this.step}\n[单位]${this.unit}"
            is StructPropertyKey -> "结构： ${
                this.items.map {
                    "[${it.identifier}]:${it.type}"
                }.joinToString("\n")
            }"
            is TextPropertyKey -> "[最大长度]${this.length}"
        }
    }

val PropertyKey.keyDisplay: String
    get() = "${name}(${identifier})"