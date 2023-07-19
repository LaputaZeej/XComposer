package com.yunext.twins.data.tsl

import com.yunext.twins.module.http.tsl.TslItemPropertyResp

/**
 * Struct内部属性集合
 */
class TslItemProperty(

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
) {
    companion object {
//        private val gson: Gson = Gson()
//        private val TYPE = object : TypeToken<List<TslItemPropertyResp>>() {
//
//        }.type

        fun from(json: String): List<TslItemProperty> {
            return try {
                TODO("序列化")
//                gson.fromJson<List<TslItemPropertyResp>>(json, TYPE)
//                    .map(TslItemPropertyResp::convert)

            }catch (e:Throwable){
                listOf()
            }
        }
    }
}

internal val TslItemProperty.display: String
    get() {
        return "$name<$identifier> [$dataType] "
    }


internal fun TslItemPropertyResp.convert() = TslItemProperty(
    identifier = identifier ?: "",
    name = name ?: "",
    dataType = TslPropertyType.from(dataType ?: ""),
    specs = specs?.convert(),
)
