package com.yunext.twins.data

import kotlin.jvm.JvmStatic
import kotlin.random.Random

class DeviceAndState(
    val name: String,
    val communicationId: String,
    val model: String,
    val status:  DeviceStatus,
)  {

    companion object {

        @JvmStatic
        fun randomList(): List<DeviceAndState> =
            List(Random.nextInt(10)) {
                DeviceAndState(
                    name = "虚拟设备 -> $it",
                    communicationId = "通信id -> $it",
                    model = "设备型号 -> $it",
                    status = DeviceStatus.random()
                )
            }

        val DEBUG_LIST: List<DeviceAndState> by lazy {
            List(20) {
                DeviceAndState(
                    name = "虚拟设备 - $it",
                    communicationId = "通信id - $it",
                    model = "设备型号 - $it",
                    status = DeviceStatus.random()
                )
            }
        }

        @JvmStatic
        val DEBUG_ITEM by lazy {
            DeviceAndState(
                "设备测试01设备测试01设备测试01设备测试01设备测试01设备测试01设备测试01设备测试01",
                "通信id",
                "设备型号",
                DeviceStatus.random()
            )
        }
    }
}

sealed class DeviceStatus(val type: DeviceType, val state: DeviceState) {
    object WiFiOnLine : DeviceStatus(DeviceType.WIFI, DeviceState.ONLINE)
    object WiFiOffLine : DeviceStatus(DeviceType.WIFI, DeviceState.OFFLINE)
    object GPRSOnLine : DeviceStatus(DeviceType.GPRS, DeviceState.ONLINE)
    object GPRSOffLine : DeviceStatus(DeviceType.GPRS, DeviceState.OFFLINE)
    companion object {
        internal fun random(): DeviceStatus {
            return listOf(WiFiOnLine, WiFiOffLine, GPRSOnLine, GPRSOffLine).random()
        }
    }
}

enum class DeviceState {
    ONLINE, OFFLINE;
}


enum class DeviceType {
    WIFI, GPRS;
}

