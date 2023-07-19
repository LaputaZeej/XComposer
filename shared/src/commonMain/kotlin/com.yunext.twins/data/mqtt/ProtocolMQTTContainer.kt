package com.yunext.twins.data.mqtt

import kotlinx.serialization.Serializable

@Serializable
class ProtocolMQTTContainer<T>(
    val cmd: String?,
    val child: String?,
    val params: T
) {
    companion object {
//        private val TYPE = object : TypeToken<MQTTContainer<*>>() {}.type
//        fun fromJson(json: String): MQTTContainer<*>? {
//            return try {
//                mqttGson.fromJson(json, TYPE)
//            } catch (e: Throwable) {
//                null
//            }
//        }
    }
}

//internal val mqttGson: Gson = GsonBuilder()
//    .create()

