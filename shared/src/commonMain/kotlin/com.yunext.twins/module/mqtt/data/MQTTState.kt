package com.yunext.twins.module.mqtt.data

sealed class MQTTState {
    object Init : MQTTState()
    object Connected : MQTTState()
    object Disconnected : MQTTState()
}

val MQTTState.isConnected:Boolean
    get() = this == MQTTState.Connected