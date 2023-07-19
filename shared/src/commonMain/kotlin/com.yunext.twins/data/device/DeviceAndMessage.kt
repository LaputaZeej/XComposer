package com.yunext.twins.data.device

import com.yunext.twins.data.mqtt.ProtocolMQTTContainer


class DeviceAndMessage(
    val device: MQTTDevice,
    val topic:String,
    val message: ProtocolMQTTContainer<*>?
)