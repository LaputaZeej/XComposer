package com.yunext.twins.module.mqtt.core

import com.yunext.twins.module.mqtt.data.MQTTParam

actual interface MQTTClient {
    actual val clientId: String
    actual fun init()
    actual fun connect(
        param: MQTTParam,
        listener: MQTTActionListener,
    )

    actual fun subscribeTopic(
        topic: String,
        listener: MQTTActionListener,
    )

    actual fun unsubscribeTopic(
        topic: String,
        listener: MQTTActionListener,
    )

    actual fun publish(
        topic: String,
        payload: ByteArray,
        qos: Int,
        retained: Boolean,
        listener: MQTTActionListener,
    )

    actual fun disconnect()
    actual fun clear()

}