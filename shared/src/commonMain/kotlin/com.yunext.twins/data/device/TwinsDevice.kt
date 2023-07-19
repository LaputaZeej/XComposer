package com.yunext.twins.data.device

import com.yunext.twins.base.TODO
import com.yunext.twins.data.ProjectInfo
import com.yunext.twins.data.devicemanager.DeviceInitializer
import com.yunext.twins.data.devicemanager.JsonDeviceInitializer
import com.yunext.twins.data.mqtt.ProtocolMQTTRule
import com.yunext.twins.module.mqtt.data.MQTTParam
import com.yunext.twins.util.md5
import kotlinx.serialization.Serializable

@Serializable
class TwinsDevice(
    val name: String,
    val mac: String,
    val imei: String,
    override val deviceType: String,
    /**
     * device与mqtt通信的username
     * 0:mac
     * 1:imei
     * 2:其他
     */
    val communicationType: CommunicationType,
) : MQTTDevice {
    override val rule: ProtocolMQTTRule
        get() = ProtocolMQTTRule.Device

    override fun generateId(): String {
        return when (communicationType) {
            CommunicationType.G4 -> imei
            CommunicationType.WIFI -> mac
        }
    }

    /**
     * TODO 需要结构出去的业务，每个项目不一样
     */
    @TODO("一些初始化的值")
    override fun providerDeviceInitializer(): DeviceInitializer {
        val json = "{" +
                "\"imei\":\"${this.generateId()}\"" +
                ",\"deviceSn\":\"${this.generateId()}\"" +
                ",\"version\":\"1.0.0\"" +
                ",\"deviceType\":156" +
                ",\"iccid\":\"${this.generateId()}\"" +
                ",\"runState\":\"1\"" +
                "}"
        return JsonDeviceInitializer(json)
    }

    enum class CommunicationType {
        G4, WIFI;
    }


    override fun createMqttParam(projectInfo: ProjectInfo): MQTTParam {
        val id = this.generateId()
        val clientId = "DEV:${deviceType}_${generateId()}_${randomNumber()}"
        val username = id
        val password = md5(clientId + projectInfo.secret) ?: ""
        return MQTTParam(
            username = username,
            password = password,
            clientId = clientId,
            url = projectInfo.host
        )
    }

    private fun randomNumber(): String {
        return List(4) { NUMs.random() }.joinToString("") { it.toString() }
    }// = randomText(4)//String.format("%04d", Random.nextInt(9999))


    companion object {
        private const val NUMs = "1234567890"
        val EMPTY = TwinsDevice("", "", "", "", CommunicationType.G4)
    }
}

val TwinsDevice.display: String
    get() {
        return "name: $name \n" +
                "mac: $mac \n" +
                "imei: $imei \n" +
                "deviceType: $deviceType \n" +
                "generateId: ${generateId()} \n"
    }

