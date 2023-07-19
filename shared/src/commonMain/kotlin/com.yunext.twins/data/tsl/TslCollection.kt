package com.yunext.twins.data.tsl

import kotlin.jvm.JvmStatic

object TslCollection {

    // 设备类型deviceType -> json文件
    private val mMap: MutableMap<String, String> = mutableMapOf()

    @JvmStatic
    fun load(json: String) {
        try {
            TODO("序列化")
//            val jsonArray = JSONArray(json)
//            val length = jsonArray.length()
//            if (length <= 0) return
//            (0 until length).forEach { i ->
//                val jsonObject = jsonArray.getJSONObject(i)
//                val keys = jsonObject.keys()
//                keys.forEach { key ->
//                    mMap[key] = jsonObject.optString(key)
//                }
//            }
        } catch (e: Throwable) {
            e.printStackTrace()
        } finally {
            println("TslCollection load complete . size = ${mMap.size}")
        }

    }

    @JvmStatic
    fun findTslFromDeviceType(deviceType: String): String {
        if (!mMap.containsKey(deviceType)) throw TslException("[TslCollection]不支持的deviceType$deviceType")
        return mMap[deviceType] ?: throw TslException("[TslCollection]${deviceType}不支持的json")
    }
}