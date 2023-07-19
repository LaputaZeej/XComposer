package com.yunext.twins.module.mqtt.core

import com.yunext.twins.module.mqtt.data.MQTTParam


interface MQTTClient {

    val clientId:String

    fun init()

    fun connect(param: MQTTParam, listener: MQTTActionListener)

    fun subscribeTopic(topic: String, listener: MQTTActionListener)

    fun unsubscribeTopic(topic: String, listener: MQTTActionListener)

    fun publish(
        topic: String, payload: ByteArray,
        qos: Int,
        retained: Boolean, listener: MQTTActionListener
    )

    fun disconnect()

    fun clear()
}