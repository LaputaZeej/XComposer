package com.yunext.twins.data

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlin.jvm.JvmStatic
import kotlin.random.Random


//@Serializable
@Immutable
@Stable
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
            List(19) {
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

//object DeviceStatusSerializer: JsonContentPolymorphicSerializer<DeviceStatus>(DeviceStatus::class){
//    override fun selectDeserializer(element: JsonElement): DeserializationStrategy<DeviceStatus> {
//        when(element){
//            element.jsonObject->DeviceStatus.serializer()
//        }
//    }
//
//}
//@Serializable()

@Immutable
@Stable
sealed class DeviceStatus(val type: DeviceType, val state: DeviceState) {
    @Immutable
    @Stable
    object WiFiOnLine : DeviceStatus(DeviceType.WIFI, DeviceState.ONLINE)
    @Immutable
    @Stable
    object WiFiOffLine : DeviceStatus(DeviceType.WIFI, DeviceState.OFFLINE)
    @Immutable
    @Stable
    object GPRSOnLine : DeviceStatus(DeviceType.GPRS, DeviceState.ONLINE)
    @Immutable
    @Stable
    object GPRSOffLine : DeviceStatus(DeviceType.GPRS, DeviceState.OFFLINE)
    companion object {
        internal fun random(): DeviceStatus {
            return listOf(WiFiOnLine, WiFiOffLine, GPRSOnLine, GPRSOffLine).random()
        }
    }
}

@Serializable
enum class DeviceState {
    ONLINE, OFFLINE;
}

@Serializable
enum class DeviceType {
    WIFI, GPRS;
}

