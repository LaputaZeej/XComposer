package com.yunext.twins.data.devicemanager

import com.yunext.farm.common.logger.ILogger
import com.yunext.twins.base.TODO
import com.yunext.twins.base.logger.DefaultLogger
import com.yunext.twins.data.device.DeviceAndMessage
import kotlinx.coroutines.CoroutineScope

abstract class AbstractDeviceHandle(protected val coroutineScope: CoroutineScope) : DeviceHandle

class DefaultDeviceHandle(coroutineScope: CoroutineScope, private val context: Context) : AbstractDeviceHandle(coroutineScope),
    ILogger by DefaultLogger("DefaultDeviceHandle") {

    /**
     * TODO 模拟对接收到的服务，进行特殊处理
     */
    @TODO("模拟对接收到的服务，进行特殊处理")
//    private fun handleServiceCustom(cmd: String, jsonObject: JsonObject) {
//        if (cmd == "sysLock") {
//            jsonObject.put("result", true)
//        }
//    }
    /**
     *  todo 处理方式阻塞？还是并行？
     */
    override fun handle(deviceStore: DeviceStore, message: DeviceAndMessage): Boolean {
//        li("[handle] ${message.device.generateId()} # ${message.message}")
//        val handleMessage = message.message ?: return true
//        val cmd = handleMessage.cmd ?: return true
//        val params = handleMessage.params ?: return true
//        li("[handle] cmd=${cmd} params=${params.toString()}")
//
//
//        // 处理服务
//        fun handleService(cmd: String) {
//            coroutineScope.launch { // todo 处理方式阻塞？还是并行？
//                val services = deviceStore.serviceFlow.value
//                services.forEach { service ->
//                    if (service.key == cmd) {
//                        // 处理 如果有本地属性的值 则覆盖
//                        val serviceKey = service.value
//                        val receiverValues = serviceKey.inputData.map { propertyKey ->
//                            li("[handle] key = ${propertyKey.identifier}")
//                            propertyKey.tslHandleDefaultValue()
//                                .tslHandleUpdatePropertyValueFromJson(params.toString())
//                        }.filterNotNull()
//
//                        li("[handle]receiverValues=${receiverValues.joinToString("\n") { it.key.display + "->" + it.displayValue }}")
//
//                        // 处理
//                        deviceStore.handleService(serviceKey,receiverValues)
//
//                        // 回复
//                        val properties = deviceStore.propertiesFlow.value
//                        val out = serviceKey.outputData
//                        if (out.isEmpty()) return@launch
//
//                        val jsonObject = JSONObject()
//                        serviceKey.outputData.map { propertyKey ->
//                            properties.forEach { entry ->
//                                if (entry.key == propertyKey.identifier) {
//                                    jsonObject.put(
//                                        entry.key,
//                                        entry.value.tslHandleToJsonValue().second
//                                    )
//                                }
//                            }
//                        }
//
//                        // FIXME 具体业务 需要解耦出去
//                        handleServiceCustom(cmd, jsonObject)
//                        // FIXME 具体业务 需要解耦出去 end
//
//                        val json = jsonObject.toString()
//                        deviceStore.publish(ReplyServiceMQTTMessage(json, cmd))
//                    }
//                }
//            }
//        }
//
//        // 回复cmd set
//        fun replySet(mqttCmd: String, paramJson: String?) {
//            // 解析
//            val map = tslHandleUpdatePropertyValuesFromJson(
//                deviceStore.propertiesFlow.value,
//                paramJson
//            )
//            deviceStore.notifyProperties(map.first)
//
//            // 回复 todo 回复后会再次收到
//            coroutineScope.launch {
//                val reply = map.second.map {
//                    it.value
//                }.toTypedArray()
//                deviceStore.replySet(*reply)
//            }
//        }
//
//        // reply cmd = data
//        fun replyData(mqttCmd: String, paramJson: String?) {
//            coroutineScope.launch {
//                // 解析
//                if (paramJson.isNullOrEmpty()) return@launch
//                val keys = MqttCmdDataParam.from(paramJson)?.keys ?: return@launch
//                // 回复
//                deviceStore.replyProperty(*keys.toTypedArray())
//            }
//        }
//
//        try {
//            when (val mqttCmd = MQTTCmd.from(cmd)) {
//                InfoUpCmd -> {
//                    //handleProperty(params.toString())
//                }
//                OnlineCmd -> {
//                    // handleProperty(params.toString())
//                }
//                ReportCmd -> {
//                    //replySet(mqttCmd.cmd, params.toString())
//                }
//                SetCmd -> {
//                    replySet(mqttCmd.cmd, params.toString())
//                }
//                is ServiceCmd -> handleService(mqttCmd.cmd)
//
//                DataCmd -> {
//                    replyData(mqttCmd.cmd, params.toString())
//                }
//
//                TimestampCmd -> {
//
//                }
//
//            }
//        } catch (e: Throwable) {
//            li("[handle] error :$e")
////            if (e is TslCmdException) {
////                // 处理service
////                handleService(cmd)
////            }
//        }
//        li("[handle] end")
        return true
    }


}
