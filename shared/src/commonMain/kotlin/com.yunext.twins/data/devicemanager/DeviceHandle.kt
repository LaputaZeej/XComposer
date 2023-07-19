package com.yunext.twins.data.devicemanager

import com.yunext.twins.data.device.DeviceAndMessage


fun interface DeviceHandle {
    fun handle(deviceStore: DeviceStore, message: DeviceAndMessage):Boolean
}