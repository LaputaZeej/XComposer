package com.yunext.twins.data.device

import com.yunext.twins.module.mqtt.data.MQTTState
import com.yunext.twins.module.mqtt.data.isConnected

class DeviceAndState(
    val device: MQTTDevice,
    val state: MQTTState
)

val DeviceAndState.display: String
    get() {
        return device.toString() + "\n[${
            state.isConnected.run {
                if (this) "online" else "offline"
            }
        }]"
    }