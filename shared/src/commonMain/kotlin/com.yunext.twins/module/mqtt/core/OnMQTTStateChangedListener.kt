package com.yunext.twins.module.mqtt.core

import com.yunext.twins.module.mqtt.data.MQTTState

fun interface OnMQTTStateChangedListener {

    fun onChanged(mqttClient: MQTTClient, mqttState: MQTTState)
}