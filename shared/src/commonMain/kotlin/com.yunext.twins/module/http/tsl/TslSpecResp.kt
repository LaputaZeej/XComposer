package com.yunext.twins.module.http.tsl

/**
 * 属性约束
 */
class TslSpecResp(
    /* 参数最小值（int、float、double类型特有） */
    val min: Any?,
    /* 参数最大值（int、float、double类型特有） */
    val max: Any?,
    /* 属性单位（int、float、double类型特有，非必填） */
    val unit: Any?,
    /* 单位名称（int、float、double类型特有，非必填） */
    val unitName: Any?,
    /* 数组元素的个数，最大512（array类型特有） */
    val size: Int?,
    /* 步长，属性值变化的最小粒度（text、enum类型无此参数） */
    val step: Any?,
    /* 数据长度，最大10240（text类型特有） */
    val length: Int?,
    /* 数组元素的类型（array类型特有） */
    val type: Any?,
    /* struct格式的数据有多个properties */
    val item:Any?,
    /* 枚举描述:enum和bool类型的参数信息 */
    val enumDesc: Any?,
)