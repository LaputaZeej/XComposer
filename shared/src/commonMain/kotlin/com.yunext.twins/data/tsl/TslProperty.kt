package com.yunext.twins.data.tsl

import com.yunext.twins.module.http.tsl.TslPropertyResp

/**
 * todo
 */
//sealed class TslPropertyX(
//    /* 属性读写类型：只读（r）或读写（rw） */
//    val accessMode: String,
//    /* 是否是标准功能的必选属性（保留）*/
//    val required: Boolean,
//    val desc: String,
//    val identifier: String,
//    val name: String,
//)

/**
 * 属性集合
 * 和[TslParam]重叠了
 */
class TslProperty(
    /* 属性读写类型：只读（r）或读写（rw） */
    val accessMode: String,
    /* 是否是标准功能的必选属性（保留）*/
    val required: Boolean,
    val desc: String,
    val identifier: String,
    val name: String,
    /*
     * DataTypeEnum
     * 属性类型
     * int
     * float
     * double
     * text
     * date（String类型UTC毫秒）
     * bool（0或1的int类型）
     * enum（int类型，枚举项定义方法与bool类型定义0和1的值方法相同）
     * struct（结构体类型，可包含前面7种类型，下面使用"specs":[{}]描述包含的对象）
     * array（数组类型，支持int、double、float、text、struct）
     */
    val dataType: TslPropertyType,
    val specs: TslSpec?,
    val inner: Boolean = false
) {

    companion object {
        fun from(tslItemProperty: TslItemProperty) = TslProperty(
            accessMode = "",
            required = false,
            desc = "",
            identifier = tslItemProperty.identifier,
            name = tslItemProperty.name,
            dataType = tslItemProperty.dataType,
            specs = tslItemProperty.specs,
            inner = true
        )

        fun from(tslItemProperty: TslParam) = TslProperty(
            accessMode = "",
            required = false,
            desc = "",
            identifier = tslItemProperty.identifier,
            name = tslItemProperty.name,
            dataType =  TslPropertyType.from(tslItemProperty.dataType),
            specs = tslItemProperty.specs,
            inner = true
        )
    }
}

val TslProperty.display: String
    get() {
        return "$name<$identifier> [$dataType] required[$required] accessMode[$accessMode] "
    }

fun TslPropertyResp.convert() = TslProperty(
    accessMode = accessMode ?: "",
    required = required ?: false,
    desc = desc ?: "",
    identifier = identifier ?: "",
    name = name ?: "",
    dataType = TslPropertyType.from(dataType ?: ""),
    specs = specs?.convert(),
)