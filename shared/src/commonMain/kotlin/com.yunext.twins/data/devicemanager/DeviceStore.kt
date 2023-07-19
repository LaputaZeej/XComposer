package com.yunext.twins.data.devicemanager

import com.yunext.farm.common.logger.ILogger
import com.yunext.twins.base.TODO
import com.yunext.twins.base.logger.DefaultLogger
import com.yunext.twins.data.ProjectInfo
import com.yunext.twins.data.device.DeviceAndMessage
import com.yunext.twins.data.device.MQTTDevice
import com.yunext.twins.data.device.generateTopic
import com.yunext.twins.data.device.providerMqttConvertor
import com.yunext.twins.data.mqtt.DataReplyMQTTMessage
import com.yunext.twins.data.mqtt.ProtocolMQTTMessage
import com.yunext.twins.data.mqtt.ReportMQTTMessage
import com.yunext.twins.data.mqtt.SetRepayMQTTMessage
import com.yunext.twins.data.report.DeviceIdAndReportData
import com.yunext.twins.data.report.ReportManager
import com.yunext.twins.data.report.Reporter
import com.yunext.twins.data.tsl.Tsl
import com.yunext.twins.data.tsl.event.EventKey
import com.yunext.twins.data.tsl.event.tslHandleTsl2EventKeys
import com.yunext.twins.data.tsl.property.IntEnumPropertyKey
import com.yunext.twins.data.tsl.property.IntEnumPropertyValue
import com.yunext.twins.data.tsl.property.IntPropertyValue
import com.yunext.twins.data.tsl.property.PropertyValue
import com.yunext.twins.data.tsl.property.tslHandleToJsonValues
import com.yunext.twins.data.tsl.property.tslHandleTsl2PropertyValues
import com.yunext.twins.data.tsl.property.tslHandleUpdatePropertyValues
import com.yunext.twins.data.tsl.property.tslHandleUpdatePropertyValuesFromJson
import com.yunext.twins.data.tsl.service.ServiceKey
import com.yunext.twins.data.tsl.service.tslHandleTsl2ServiceKeys
import com.yunext.twins.module.mqtt.core.MQTTClient
import com.yunext.twins.module.mqtt.core.OnMQTTMessageChangedListener
import com.yunext.twins.module.mqtt.data.MQTTMessage
import com.yunext.twins.module.mqtt.data.MQTTParam
import com.yunext.twins.module.mqtt.data.MQTTState
import com.yunext.twins.module.repository.DeviceRepository
import com.yunext.twins.module.repository.LogRepository
import com.yunext.twins.module.repository.ReportRepository
import com.yunext.twins.util.currentTime
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlin.random.Random

/**
 * 设备信息
 */
class DeviceStore(
    context: Context,
    private val projectInfo: ProjectInfo,
    val device: MQTTDevice,
    private val coroutineScope: CoroutineScope,
    private val mqttManager: MQTTDeviceManager,
    private val logRepository: LogRepository,
    private val deviceRepository: DeviceRepository,
    private val reportRepository: ReportRepository,
    /**
     * 设备通用的属性值初始化
     */
    private val deviceInitializer: DeviceInitializer,

    ) : ILogger by DefaultLogger(TAG, true) {
    private var mClient: MQTTClient? = null
    private var mTsl: Tsl? = null
    private var mConnectJob: Job? = null
    private val localDevice = LocalDevice()

    private val reportManager: Reporter = ReportManager(coroutineScope, this, reportRepository)

    val clientId: String?
        get() = mClient?.clientId

    private val mDeviceHandleList: MutableList<DeviceHandle> =
        mutableListOf(DefaultDeviceHandle(coroutineScope, context))

    val state: MQTTState
        get() = deviceStateFlow.value

    /**
     * tsl
     */
    private val _tslFlow: MutableStateFlow<Tsl?> =
        MutableStateFlow(null)

    val tslFlow = _tslFlow.asStateFlow()

    /**
     * 属性property
     */
    private val _propertiesFlow: MutableStateFlow<Map<String, PropertyValue<*>>> =
        MutableStateFlow(mapOf())

    val propertiesFlow: StateFlow<Map<String, PropertyValue<*>>> =
        _propertiesFlow.asStateFlow()

    private val mServiceDownEffect: MutableSharedFlow<String> = MutableSharedFlow()
    val serviceDownEffect = mServiceDownEffect.asSharedFlow()

    /**
     * 时间event
     */
    private val _eventFlow: MutableStateFlow<Map<String, EventKey>> =
        MutableStateFlow(mapOf())

    val eventFlow: StateFlow<Map<String, EventKey>> =
        _eventFlow.asStateFlow()

    /**
     * 服务Service
     */
    private val _serviceFLow: MutableStateFlow<Map<String, ServiceKey>> =
        MutableStateFlow(mapOf())

    val serviceFlow: StateFlow<Map<String, ServiceKey>> =
        _serviceFLow.asStateFlow()

    private val _deviceStateFlow: MutableStateFlow<MQTTState> = MutableStateFlow(MQTTState.Init)
    val deviceStateFlow: StateFlow<MQTTState> = _deviceStateFlow.asStateFlow()

    fun connect(context: Context, auto: Boolean = true) {
        ld("connect")
        mConnectJob?.cancel()
        mConnectJob = coroutineScope.launch {
            val result = connectInterval(context)
            ld("连接结果：$result")
            if (!result) {
                ensureActive()
                delay(10_000)
                ensureActive()
                ld("重新连接...")
                connect(context, auto)
            }
        }
    }

    private var mCurrentMqttParam: MQTTParam? = null

    @TODO("MQTTClient")
    private suspend fun connectInterval(context: Context): Boolean {

//        val mqttParam = device.createMqttParam(projectInfo)
//        mCurrentMqttParam = mqttParam
//        return withContext(Dispatchers.IO) {
//            try {
//                val myOnMessageChangedListener =
//                    OnMQTTMessageChangedListener { client, topic, message ->
//                        val msg = device.providerMqttConvertor().decode(message.payload)
//                        val dm = DeviceAndMessage(device, topic, msg)
//                        onMessageChanged(client, dm)
//                    }
//
//                val myOnStateChangedListener = OnStateChangedListener { client, state ->
//                    coroutineScope.launch {
//                        onStateChanged(client, state)
//                    }
//                }
//                val hadlinksMqttClient = HadlinksMqttClient(context).apply {
//                    registerOnMessageChangedListener(myOnMessageChangedListener)
//                    registerOnStateChangedListener(myOnStateChangedListener)
//                }
//                val connect =
//                    try {
//                        hadlinksMqttClient.suspendConnect(mqttParam)
//                    } catch (e: Throwable) {
//                        false
//                    }
//                if (connect) {
//                    mClient = hadlinksMqttClient
//                    //onStateChanged(hadlinksMqttClient, MqttState.Connected)
//                    val topics = device.supportTopics().iterator()
//                    while (topics.hasNext()) {
//                        val topic: String = device.generateTopic(projectInfo, topics.next())
//                        val success = subscribeTopicInner(hadlinksMqttClient, topic)
//                        ld(TAG, "[$topic] $success")
//                    }
//
//                    true
//                } else {
//                    hadlinksMqttClient.disconnect()
//                    false
//                }
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                false
//            }
//        }
        return false
    }

    private var mFirstReportJob: Job? = null

    /**
     * mqtt 在离线状态
     */
    private fun onStateChanged(client: MQTTClient, state: MQTTState) {
        coroutineScope.launch {
            _deviceStateFlow.emit(state)
            syncDeviceManagerChanged("onStateChanged $state")
            //startReport(state)
        }

        // online
        @TODO("logger")
        coroutineScope.launch {
//            logRepository.add(
//                OnlineLog(
//                    timestamp = System.currentTimeMillis(),
//                    deviceId = device.generateId(),
//                    clientId = client.clientId,
//                    onLine = state == MqttState.Connected
//                )
//            )
        }
        mFirstReportJob?.cancel()
        mFirstReportJob = coroutineScope.launch {
            if (state == MQTTState.Connected) {
                ld("--> report first properties ")
                var reported = false
                var count: Int = 0
                try {
                    delay(1000) // todo
//                    while (!reported && count < 3) {
                    ld("--> report first properties $reported $count")
                    val r = withContext(Dispatchers.IO) {
                        reportRepository.takeFirstReport(device.generateId())
                    }
                    if (r != null) {
                        val list = r.list
                        if (list.isEmpty()) return@launch
                        val pros = propertiesFlow.value

                        val firstList = list.map {
                            pros[it.id]
                        }.filterNotNull()
                        ld("--> report first properties : ${firstList.size}")
                        val result = reportProperties(*firstList.toTypedArray())
                        if (result) {
                            reported = true
                            ld("--> report first properties success")

                        } else {
                            reported = false
                            count++
                            delay(1000)
                        }
                    }
//                    }

                } catch (e: Throwable) {

                }

            }
        }
    }

    fun initTsl(tsl: Tsl, update: Boolean = false) {
        if (update) {
            // .
        } else {
            val cur = mTsl
            if (cur != null) {
                if (cur.version == tsl.version) {
                    return
                }
            }
        }

        li("initTsl")
        mTsl = tsl
        _tslFlow.value = tsl

        _propertiesFlow.value = tsl.tslHandleTsl2PropertyValues()
        _eventFlow.value = tsl.tslHandleTsl2EventKeys()
        _serviceFLow.value = tsl.tslHandleTsl2ServiceKeys()

        // 尝试从本地初始化值
        coroutineScope.launch {
            delay(500)
            val json = deviceRepository.loadDeviceTslValue(device.generateId())
            ld("tsl load json = $json")
            val map = tslHandleUpdatePropertyValuesFromJson(
                propertiesFlow.value,
                json
            )
            _propertiesFlow.value = (map.first)

            @TODO("应该在解析完TSL的时候选择性初始化属性值。")
            val initializerValues = deviceInitializer.init(_propertiesFlow.value)
            _propertiesFlow.value = initializerValues

            // 在初始化过后在执行
            @TODO("TODO 和DeviceInitializer合并")
            localDevice.randomRssi()
        }
        // 尝试从本地初始化值


        coroutineScope.launch {
            val custom = reportRepository.take(device.generateId())
            startReport(custom)
        }


    }

    @Deprecated("")
    private fun startReport(state: MQTTState) {
        // 开始上报
        coroutineScope.launch(Dispatchers.IO) {
            if (state == MQTTState.Connected) {
                val data = reportRepository.take(device.generateId())
                if (data != null) {
                    reportManager.setData(data)
                }
                reportManager.start()
            } else {
                reportManager.stop()
            }
        }
    }

    private var mStartReportJob: Job? = null
    fun startReport(data: DeviceIdAndReportData?) {
        ld("startReport $data")
        mStartReportJob?.cancel()
        reportManager.stop()
        mStartReportJob = coroutineScope.launch(Dispatchers.IO) {
            if (data == null) {
                reportManager.stop()
                return@launch
            }
            val reportData = try {
                reportRepository.take(device.generateId())
            } catch (e: Throwable) {
                null
            }
                ?: return@launch
//             ?: kotlin.run {
//                reportRepository.put(device.generateId(),data)
//                data
//            }

            reportManager.setData(reportData)
            reportManager.start()
        }
    }

    fun setReportData(data: DeviceIdAndReportData) {
        coroutineScope.launch(Dispatchers.IO) {
            reportManager.setData(data)
            reportManager.start()
        }

    }

    private fun tryAction(action: () -> Unit) {
        try {
            action()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * 同步更新本地的数据
     *
     * 1.从服务器下发的数据
     */
    internal fun notifyProperties(map: Map<String, PropertyValue<*>>) {
        _propertiesFlow.value = map
        syncDeviceManagerChanged("notifyProperties ${map.size}")
    }

    private fun onMessageChanged(client: MQTTClient, dm: DeviceAndMessage) {
        coroutineScope.launch {
            try {
                if (dm.device.generateId() != device.generateId()) return@launch
//                li("onMessageChanged:${gson.toJson(dm)}")
                // 处理业务
                mDeviceHandleList.forEach { handle ->
                    if (handle.handle(this@DeviceStore, dm)) {
                        // 处理过了就直接返回
                        return@launch
                    }
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }

        // down
        @TODO("add log")
        coroutineScope.launch {

//            logRepository.add(
//                DownLog(
//                    timestamp = System.currentTimeMillis(),
//                    deviceId = device.generateId(),
//                    clientId = client.clientId,
//                    topic = dm.topic,
//                    cmd = dm.message?.cmd ?: "",
//                    payload = dm.message?.params?.toString() ?: "",
//                )
//            )
        }
    }

    private fun syncDeviceManagerChanged(tag: String) {
        mqttManager.onDeviceChanged(this)

        ld("syncDeviceManagerChanged $tag")
        // 保存设备状态
        @TODO("保存设备状态")
        coroutineScope.launch {

//            val list = _propertiesFlow.value.values.toList()
//            val map = list.tslHandleToJsonValues()
//            if (map.isEmpty()) return@launch
//            val json = MQTT_GSON.toJson(map)
//            ld("tsl save  json = $json")
//            if (json.isNullOrEmpty()) return@launch
//            deviceRepository.saveDeviceTslValue(device.generateId(), json)
        }
    }

    private fun tryGetEventKey(id: String): EventKey? = eventFlow.value.values.singleOrNull {
        it.identifier == id
    }

    private var checkEventKey: Job? = null

    /**
     * @param keys 设置的属性 是否包含key 才会检测key
     * FIXME 具体业务 需要解耦出去
     */
    @TODO("处理事件")
    private fun checkEvent(keys: List<String>) {
        ld("checkEvent ")
        checkEventKey?.cancel()
        checkEventKey = coroutineScope.launch {
            propertiesFlow.value.forEach {
                when (it.key) {
                    "rawTDS" -> {
                        // 模拟tds异常时发出通知
                        try {
                            if (!keys.contains(it.key)) return@launch
                            val v = it.value as IntPropertyValue
                            v.value ?: return@launch
                            if ((v.value > 0) and (v.value <= 100)) {
                                tryGetEventKey("waterAlert")?.let { eventKey ->
                                    val key = eventKey.outputData.singleOrNull() { item ->
                                        item.identifier == "code"
                                    }
                                    sendEvent(
                                        eventKey,
                                        listOf(
                                            IntEnumPropertyValue.from(
                                                key as IntEnumPropertyKey,
                                                1
                                            )
                                        )
                                    )
                                }
                            }
                        } catch (e: Throwable) {
                            e.printStackTrace()
                        }
                    }

                }
            }
        }

    }

    fun disconnect() {
        try {
            reportManager.stop()
            mClient?.disconnect()
            mConnectJob?.cancel()
            mFirstReportJob?.cancel()
            mStartReportJob?.cancel()
            localDevice.cancel()
        } catch (e: Throwable) {
            e.printStackTrace()
        }

    }

    @TODO("MQTT client")
    private suspend fun subscribeTopicInner(
        mqttManager: MQTTClient,
        topic: String,
        count: Int = 0,
    ): Boolean {
        return false
//        if (count > 3) throw IllegalStateException("尝试注册topic[$topic]${count}次失败")
//        val result = mqttManager.suspendSubscribeTopic(topic)
//        return if (result) {
//            true
//        } else {
//            subscribeTopicInner(mqttManager, topic, count + 1)
//        }
    }

    /**
     * 主动设置属性
     * mqtt里pub为服务器
     */
    suspend fun sendProperty(vararg property: PropertyValue<*>, publish: Boolean = true): Boolean {
        // 更新自己的数据
        val map =
            tslHandleUpdatePropertyValues(_propertiesFlow.value, property.toList())
        _propertiesFlow.value = map
        checkEvent(property.map { it.key.identifier })

        // 上报服务器
//        mqttManager.publishWithMessageTypeForProperty<ReportMQTTMessage>(
//            device as TwinsDevice,
//            *property
//        )
        val reportMap = property.toList().tslHandleToJsonValues()
        if (reportMap.isEmpty()) return true
        if (publish) {
            publish(ReportMQTTMessage(reportMap))
        }
        syncDeviceManagerChanged("sendProperty")
        return true
    }

    /**
     *
     * 回复set
     */
    suspend fun replySet(vararg property: PropertyValue<*>): Boolean {
        val reportMap = property.toList().tslHandleToJsonValues()
        publish(SetRepayMQTTMessage(reportMap))
//        mqttManager.publishWithMessageTypeForProperty<SetRepayMQTTMessage>(
//            device as TwinsDevice,
//            *property
//        )
        return true
    }

    /**
     * 主动触发事件
     * todo
     */
    suspend fun sendEvent(key: EventKey, value: List<PropertyValue<*>>): Boolean {
        ld("sendEvent ${key.identifier}")
        val properties: MutableMap<String, PropertyValue<*>> = mutableMapOf()
        value.forEach { entry ->
            key.outputData.forEach { pk ->
                if (pk.identifier == entry.key.identifier) {
                    properties[pk.identifier] = entry
                }
            }
        }

        val finalMap = properties.values.toList().tslHandleToJsonValues()
        if (finalMap.isEmpty()) return true
        //val finalMap = properties.values.toList().tslHandleToJsonObject()
        val map = mapOf(key.identifier to finalMap)

        return publish(ReportMQTTMessage(map))
    }

    /**
     * 回复data到服务器
     */
    suspend fun replyProperty(vararg keys: String): Boolean {
        if (keys.isEmpty()) return true
        val newMap: MutableMap<String, PropertyValue<*>> = mutableMapOf()
        propertiesFlow.value.forEach { entry ->
            keys.forEach { k ->
                if (k == entry.key) {
                    newMap[k] = entry.value
                }
            }
        }
        // 回复
        val reply = newMap.map {
            it.value
        }

        val finalMap = reply.toList().tslHandleToJsonValues()
        if (finalMap.isEmpty()) return true
        publish(DataReplyMQTTMessage(finalMap))
//        mqttManager.publishWithMessageTypeForProperty<DataReplyMQTTMessage>(
//            device as TwinsDevice,
//            *reply
//        )
        syncDeviceManagerChanged("replyProperty")
        return true
    }

    @TODO("mqtt client")
    suspend fun publish(mqttMessage: ProtocolMQTTMessage): Boolean {
//        return mClient?.let { mqttClient ->
//            val topic: String = device.generateTopic(projectInfo, mqttMessage.topic)
//            val qos = mqttMessage.qos
//            val retention = mqttMessage.retain == 1
//            val payload = device.providerMqttConvertor().encode(mqttMessage)
//            val r = mqttClient.suspendPublish(
//                topic = topic, payload = payload,
//                qos = qos, retained = retention
//            )
//
//            // up
//            @TODO("add logger")
//            coroutineScope.launch {
////                logRepository.add(
////                    UpLog(
////                        timestamp = System.currentTimeMillis(),
////                        deviceId = device.generateId(),
////                        clientId = mqttClient.clientId,
////                        topic = topic,
////                        cmd = mqttMessage.cmd.cmd,
////                        payload = mqttMessage.data.toString(),
////                        state = r
////                    )
////                )
//            }
//            r
//
//
//        } ?: false
        return false
    }

    suspend fun reportProperties(vararg property: PropertyValue<*>): Boolean {
        val map = property.toList().tslHandleToJsonValues()
        if (map.isEmpty()) return true
        return publish(ReportMQTTMessage(map))
    }

    fun handleService(serviceKey: ServiceKey, values: List<PropertyValue<*>>?) {
        val newMap = tslHandleUpdatePropertyValues(propertiesFlow.value, values ?: listOf())
        _propertiesFlow.value = newMap

        coroutineScope.launch {
            li("handleService ${serviceKey.identifier}")
            val msg = serviceKey.name + "  " + values?.joinToString(",") {
                it.displayValue
            }
            mServiceDownEffect.emit(msg)
        }
    }

    private inner class LocalDevice {
        private var job: Job? = null
        fun randomRssi() {
            job?.cancel()
            job = coroutineScope.launch {
                launch {
                    while (true) {
                        delay(5000)//
                        val rssi = Random.nextInt(99)
                        propertiesFlow.value.forEach {
                            if (it.key == "signalStrength") {
                                val v = it.value as IntPropertyValue
                                sendProperty(IntPropertyValue(v.key, rssi), publish = false)
                            }
                        }

                    }
                }
            }


        }

        fun cancel() {
            job?.cancel()
        }

    }


    companion object {
        private const val TAG = "_DeviceStore_"
    }


}

class DeviceStoreWrapper(
    val deviceStore: DeviceStore,
    private val time: Long = currentTime() + Random.nextInt(10),
)