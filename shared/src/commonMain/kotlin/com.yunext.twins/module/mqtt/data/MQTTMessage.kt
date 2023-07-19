package com.yunext.twins.module.mqtt.data

import kotlinx.serialization.Serializable

@Serializable
class MQTTMessage(
    val payload: ByteArray,
    val retained: Boolean,
    val qos: Int,
    val messageId: Int,
    val dup: Boolean,
    val mutable: Boolean,
)