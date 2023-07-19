package com.yunext.twins.module.mqtt.core

import com.yunext.twins.module.mqtt.data.MQTTMessage

fun interface OnMQTTMessageChangedListener {

    fun onChanged(mqttClient: MQTTClient, topic: String, message: MQTTMessage)
}