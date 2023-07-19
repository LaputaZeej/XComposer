//package com.yunext.twins.data.mqtt
//
//class MqttCmdDataParam(val keys: List<String>) {
//
//    companion object {
//        fun from(json: String?): MqttCmdDataParam? {
//            return try {
//                if (json.isNullOrEmpty()) return null
//                MQTT_GSON.fromJson<MqttCmdDataParam>(json, MqttCmdDataParam::class.java)
//            } catch (e: Throwable) {
//                null
//            }
//        }
//    }
//}