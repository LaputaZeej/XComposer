package com.yunext.twins.data.tsl

import com.yunext.twins.module.http.tsl.TslParamResp

class TslParam(
    /* 唯一标识符（产品下唯一） */
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
    val dataType: String,
    /* 属性约束 */
    val specs: TslSpec?,
)

fun TslParamResp.converts() = this.run {
    TslParam(
        identifier = this.identifier ?: "",
        name = this.name ?: "",
        dataType = this.dataType ?: "",
        specs = this.specs?.convert()
    )
}

