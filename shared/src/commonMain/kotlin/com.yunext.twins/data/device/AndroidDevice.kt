package com.yunext.twins.data.device

import com.yunext.twins.data.ProjectInfo
import com.yunext.twins.data.devicemanager.DeviceInitializer
import com.yunext.twins.data.mqtt.ProtocolMQTTRule
import com.yunext.twins.module.mqtt.data.MQTTParam
import com.yunext.twins.util.currentTime
import com.yunext.twins.util.md5

class AndroidDevice(
    private val userId: String,
    private val access: String,
    override val deviceType: String,
    ) : MQTTDevice {
    override val rule: ProtocolMQTTRule
        get() = ProtocolMQTTRule.App.Android

    override fun createMqttParam(projectInfo: ProjectInfo): MQTTParam {
        val id = this.generateId()
        val clientId = "APP:${deviceType}_${id}_${currentTime()}"
        val username = id
        val passwordMd5 = md5(access) ?: ""
        return MQTTParam(
            username = username,
            password = passwordMd5,
            clientId = clientId,
            url = projectInfo.host
        )
    }

    override fun generateId(): String {
        return userId
    }

    override fun providerDeviceInitializer(): DeviceInitializer {
        TODO("Not yet implemented")
    }
}