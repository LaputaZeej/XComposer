package com.yunext.twins.data.devicemanager.ext

internal inline fun <reified T> tryGetJSON(json: String): T? {
    try {
//        val source = JSONTokener(json).nextValue()
//        val isJsonObject = when (source) {
//            is JSONObject -> {
//                println("tryGetJson JSONObject")
//                true
//
//            }
//            is JSONArray -> {
//                println("tryGetJson JSONArray")
//                false
//            }
//            else -> return null
//        }
//        return when (T::class.java) {
//            JSONArray::class.java -> {
//                if (isJsonObject) null else source as T
//            }
//            JSONObject::class.java -> {
//                if (isJsonObject) source as T else null
//            }
//            else -> null
//        }
        return null

    } catch (e: Throwable) {
        e.printStackTrace()
        println("tryGetJson 更新异常：$e")
        return null
    }
}