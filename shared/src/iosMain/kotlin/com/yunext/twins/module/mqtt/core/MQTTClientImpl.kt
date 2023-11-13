package com.yunext.twins.module.mqtt.core

import com.yunext.twins.module.mqtt.data.MQTTParam

actual val mqttClient:MQTTClient by lazy {
    MQTTClientImpl()
}

class MQTTClientImpl:MQTTClient {
    override val clientId: String
        get() = "123"

    override fun init() {
        println("mqtt-ios-init")
    }

    override fun connect(param: MQTTParam, listener: MQTTActionListener) {
        println("mqtt-ios-connect")
    }

    override fun subscribeTopic(topic: String, listener: MQTTActionListener) {
        println("mqtt-ios-subscribeTopic")
    }

    override fun unsubscribeTopic(topic: String, listener: MQTTActionListener) {
        println("mqtt-ios-unsubscribeTopic")
    }

    override fun publish(
        topic: String,
        payload: ByteArray,
        qos: Int,
        retained: Boolean,
        listener: MQTTActionListener,
    ) {
        println("mqtt-ios-publish")
    }

    override fun disconnect() {
        println("mqtt-ios-disconnect")
    }

    override fun clear() {
        println("mqtt-ios-clear")
    }
}