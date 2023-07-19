package com.yunext.twins.data.tsl

const val TSL_PATH = "tsl"
private const val TSL_HEAD = "tsl_"
private const val TSL_TAIL = ".json"
//fun Context.extListTsl(): List<String> {
//    return assets.list(TSL_PATH)?.filterNotNull()?.filter {
//        it.startsWith(TSL_HEAD) && it.endsWith(TSL_TAIL)
//    } ?: listOf()
//}