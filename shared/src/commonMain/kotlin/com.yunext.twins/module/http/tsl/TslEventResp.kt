package com.yunext.twins.module.http.tsl

class TslEventResp(
    /* 唯一标识符（产品下唯一，其中post是默认生成的属性上报事件。）*/
    val identifier: String?,
    val name: String?,
    /* 事件类型（info、alert、error） */
    val type: String?,
    /* 是否是标准功能的必选属性 */
    val required: Boolean?,
    val desc: String?,
    /* 事件对应的方法名称（根据identifier生成）*/
    val method: String?,
    /* 输出参数 */
    val outputData: List<TslParamResp>?,
)