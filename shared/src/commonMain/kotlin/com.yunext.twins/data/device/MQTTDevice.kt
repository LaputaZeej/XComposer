package com.yunext.twins.data.device

import com.yunext.twins.data.mqtt.ProtocolMQTTRule
import com.yunext.twins.data.mqtt.ProtocolMQTTTopic
import com.yunext.twins.data.ProjectInfo
import com.yunext.twins.data.devicemanager.DefaultMqttConvertor
import com.yunext.twins.data.devicemanager.DeviceInitializer
import com.yunext.twins.data.devicemanager.MQTTConvertor
import com.yunext.twins.data.isYinDu
import com.yunext.twins.module.mqtt.data.MQTTParam

sealed interface MQTTDevice {

    val rule: ProtocolMQTTRule

    val deviceType: String

    fun createMqttParam(projectInfo: ProjectInfo): MQTTParam

    fun generateId(): String

    fun supportTopics(): Array<ProtocolMQTTTopic> =
        arrayOf(ProtocolMQTTTopic.DOWN, ProtocolMQTTTopic.QUERY)

    fun providerDeviceInitializer(): DeviceInitializer

}

fun MQTTDevice.generateTopic(projectInfo: ProjectInfo, mqttTopic: ProtocolMQTTTopic): String {
    val brand = projectInfo.brand
    val deviceID = generateId()
    if (projectInfo.isYinDu()) {
        return "/$brand/mqtt/$deviceID/${mqttTopic.category}"
    }
    return "/$brand/$deviceType/$deviceID/${mqttTopic.category}"
}

fun MQTTDevice.providerMqttConvertor(): MQTTConvertor {
    return when (this) {
        is AndroidDevice -> DefaultMqttConvertor()
        is TwinsDevice -> DefaultMqttConvertor()
        else->throw IllegalStateException("non convertor")
    }
}







