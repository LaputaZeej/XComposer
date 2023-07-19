package com.yunext.twins.data.tsl

import com.yunext.twins.module.http.tsl.TslParamResp
import com.yunext.twins.module.http.tsl.TslServiceResp

class TslService(
    val identifier: String,
    val name: String,
    /* async（异步调用）或sync（同步调用）*/
    val callType: String,
    /* 是否是标准功能的必选属性 */
    val required: Boolean,
    val desc: String,
    /* 事件对应的方法名称（根据identifier生成） */
    val method: String,
    /* 输入参数 */
    val inputData: List<TslParam>,
    /* 输出参数 */
    val outputData: List<TslParam>

)


fun TslServiceResp.convert() = TslService(
    identifier = this.identifier ?: "",
    name = this.name ?: "",
    callType = this.callType ?: "",
    required = this.required ?: false,
    desc = this.desc ?: "",
    method = this.method ?: "",
    inputData = this.inputData?.map(TslParamResp::converts) ?: listOf(),
    outputData = this.outputData?.map(TslParamResp::converts) ?: listOf(),
)