package com.yunext.twins.data.devicemanager

import com.yunext.farm.common.logger.ILogger
import com.yunext.twins.base.logger.DefaultLogger
import com.yunext.twins.data.ProjectInfo
import com.yunext.twins.data.device.MQTTDevice
import com.yunext.twins.data.device.TwinsDevice
import com.yunext.twins.data.mqtt.ProtocolMQTTMessage
import com.yunext.twins.data.tsl.TslCollection
import com.yunext.twins.module.mqtt.data.MQTTMessage
import com.yunext.twins.module.repository.DeviceRepository
import com.yunext.twins.module.repository.LogRepository
import com.yunext.twins.module.repository.ReportRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class MQTTDeviceManager constructor(
    context: Context,
    private val projectInfo: ProjectInfo,
    private val logRepository: LogRepository,
    private val deviceRepository: DeviceRepository,
    private val reportRepository: ReportRepository,
) :
    ILogger by DefaultLogger(TAG, true) {

    private val context = context
    private val coroutineScope: CoroutineScope =
        CoroutineScope(Dispatchers.IO + SupervisorJob() + CoroutineName("MQTTDeviceManager"))

    private val mDeviceStoreFlow: MutableStateFlow<Map<String, DeviceStoreWrapper>> =
        MutableStateFlow(
            mapOf()
        )
    val deviceStoreFlow = mDeviceStoreFlow.asStateFlow().stateIn(
        coroutineScope, SharingStarted.Eagerly,
        mapOf()
    )

    fun init(json: String) {
        TslCollection.load(json)
    }

    fun add(device: TwinsDevice) {
        val id = device.generateId()
        val storeMap = mDeviceStoreFlow.value.toMutableMap()
        ld("add size = ${storeMap.size}")
        if (storeMap.containsKey(id)) {
            return
        }
        val deviceStore =
            DeviceStore(
                context = context,
                projectInfo,
                device,
                coroutineScope,
                this,
                logRepository,
                deviceRepository,
                reportRepository,
                device.providerDeviceInitializer()
            )
        storeMap[id] = DeviceStoreWrapper(deviceStore)
        mDeviceStoreFlow.value = storeMap
        deviceStore.connect(context)
        ld("add end size = ${storeMap.size}")
    }

    fun remove(device: TwinsDevice) {
        val id = device.generateId()
        val deviceStoreMap = mDeviceStoreFlow.value.toMutableMap()
        ld("delete size = ${deviceStoreMap.size}")
        val d = deviceStoreMap[id]
        if (d != null) {
            deviceStoreMap.remove(id)
            mDeviceStoreFlow.value = deviceStoreMap
            d.deviceStore.disconnect()
        }
        ld("delete size = ${deviceStoreMap.size}")
    }

    fun requireDeviceStore(device: MQTTDevice) = mDeviceStoreFlow.value[device.generateId()]

    suspend fun publish(device: TwinsDevice, mqttMessage: ProtocolMQTTMessage): Boolean {
        val deviceStore = requireDeviceStore(device) ?: return false
//        val json = mqttGson.toJson(mqttMessage) ?: return false
//        ld("[publish] device:${device} message:$json")
        return deviceStore.deviceStore.publish(mqttMessage)
    }

    fun disconnect() {
        ld("disconnect")
        val mDeviceStoreMap = mDeviceStoreFlow.value.toMutableMap()
        mDeviceStoreMap.iterator().forEach {
            val deviceStore = it.value
            deviceStore.deviceStore.disconnect()
        }
        mDeviceStoreMap.clear()
        mDeviceStoreFlow.value = mapOf()
        ld("disconnect end!")
    }

    fun onDeviceChanged(deviceStore: DeviceStore) {
        ld("[onDeviceChanged] deviceStore:${deviceStore}")
        coroutineScope.launch {
            val generateId = deviceStore.device.generateId()
            val map = mDeviceStoreFlow.value.toMutableMap()
            val store = map[generateId]
            if (store != null) {
                map[generateId] = DeviceStoreWrapper(deviceStore)
                mDeviceStoreFlow.value = map.toMap()
            }
        }
    }

    companion object {
        private const val TAG = "_MQTTDeviceManager_"
    }
}

