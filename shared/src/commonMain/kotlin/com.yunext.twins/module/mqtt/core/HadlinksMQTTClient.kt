package com.yunext.twins.module.mqtt.core


//class HadlinksMQTTClient(context: Context) : MQTTClient, ILogger by DefaultLogger(TAG, true) {

//    private val mContext: Context = context.applicationContext
//    private lateinit var mClient: MqttAndroidClient
//    private var mState: AtomicReference<MqttState> = AtomicReference(MqttState.Init)
//    val clientId:String
//        get() = mClient.clientId
//
//    val state: MqttState
//        get() = mState.get()
//
//    private val mTopics: CopyOnWriteArraySet<String> = CopyOnWriteArraySet()
//
//    var onStateChangedListener: OnStateChangedListener? = null
//    private var onStateChangedListeners: MutableList<OnStateChangedListener> = mutableListOf()
//    fun registerOnStateChangedListener(listener: OnStateChangedListener) {
//        this.onStateChangedListeners.add(listener)
//    }
//
//    fun unRegisterOnStateChangedListener(listener: OnStateChangedListener) {
//        this.onStateChangedListeners.remove(listener)
//    }
//
//    private var onMessageChangedListeners: MutableList<OnMessageChangedListener> = mutableListOf()
//    var onMessageChangedListener: OnMessageChangedListener? = null
//    fun registerOnMessageChangedListener(listener: OnMessageChangedListener) {
//        this.onMessageChangedListeners.add(listener)
//    }
//
//    fun unRegisterOnMessageChangedListener(listener: OnMessageChangedListener) {
//        this.onMessageChangedListeners.remove(listener)
//    }
//
//    private val mMqttCallback = object : MqttCallbackExtended {
//        override fun connectComplete(reconnect: Boolean, serverURI: String?) {
//            mqttDebug("connectComplete reconnect:$reconnect @ $serverURI")
//            onStateChanged(MqttState.Connected)
//            if (reconnect) {
//                // Because Clean Session is true, we need to re-subscribe
//                try {
//                    mTopics.subscribeTopics()
//                } catch (e: Throwable) {
//                    e.printStackTrace()
//                }
//            }
//        }
//
//        override fun messageArrived(topic: String, message: MqttMessage?) {
//            try {
//                mqttDebug("messageArrived $topic # $message")
//                handleMessage(topic, message)
//            } catch (e: Throwable) {
//                mqttError("messageArrived error：$e")
//            }
//        }
//
//        override fun connectionLost(cause: Throwable?) {
//            onStateChanged(MqttState.Disconnected)
//            mqttError("connectionLost cause：$cause")
//        }
//
//        override fun deliveryComplete(token: IMqttDeliveryToken?) {
//            mqttError("deliveryComplete token：${token?.client?.clientId}")
//        }
//    }
//
//    override fun init() {
//
//    }
//
//    override fun connect(param: MqttParam, listener: IMqttActionListener) {
//        mqttDebug("connect ${param.display}")
//        val client: MqttAndroidClient = if (this::mClient.isInitialized) {
//            mqttInfo("mClient已初始化")
////            mClient.disconnect()
////            mClient.close()
////            try {
////                Thread.sleep(2000L)
////            } catch (e: Throwable) {
////
////            }
////            mqttInfo("mClient清理完毕")
//            mClient
//        } else {
//            val url = param.url
//            val clientId = param.clientId
//            MqttAndroidClient(mContext.applicationContext, url, clientId).apply {
//                //initTrace()
//            }.also { client ->
//                client.setCallback(mMqttCallback)
//            }.also {
//                mClient = it
//            }
//        }
//        val options = MqttConnectOptions().apply {
//            // 是否自动重新连接。当客户端网络异常或进入后台后导致连接中断，在这期间会不断的尝试重连，
//            // 重连等待最初会等待1 秒钟, 每次重连失败等待时间就会加倍，直到 2 分钟，此时延迟将保持在 2 分钟。
//            isAutomaticReconnect = true
//            // 是否自动清除 session. 注意如果为 true 则会清除session. 会导致如果你掉线的期间，
//            // 你所订阅的topic有新的消息，等你重新连接上后因为session被清除了，你将无法接收到在你
//            // 离线期间的新消息
//            isCleanSession = true
//            connectionTimeout = CONNECTION_TIMEOUT
//            keepAliveInterval = KEEP_ALIVE
//            // setWill(HadCategory.STATUS.createTopic(device.appType().strValue, device.mac), byteArrayOf(),1,true)
//            this.password = param.password.toCharArray()
//            this.userName = param.username
//            if (param.ssl) {
//                socketFactory = DefaultSSL.defaultFactory()
//            }
//        }
//
//        client.connect(options, null, object : IMqttActionListener {
//            override fun onSuccess(asyncActionToken: IMqttToken?) {
//                mqttDebug("connect onSuccess $asyncActionToken")
//                listener.onSuccess(asyncActionToken)
//            }
//
//            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
//                mqttError("connect onFailure $asyncActionToken # $exception")
//                //listener.onFailure(asyncActionToken, exception)
//            }
//
//        })
//    }
//
//
//    override fun subscribeTopic(topic: String, listener: IMqttActionListener) {
//        mTopics.add(topic)
//        mqttDebug("subscribeTopic $topic / ${mTopics.size}")
//        if (checkIsConnected()) {
//            mClient.subscribe(topic, 2, null, object : IMqttActionListener {
//                override fun onSuccess(asyncActionToken: IMqttToken?) {
//                    mqttDebug("subscribeTopic onSuccess $topic $asyncActionToken")
//                    listener.onSuccess(asyncActionToken)
//                }
//
//                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
//                    mqttError("subscribeTopic onFailure $topic $asyncActionToken $exception")
//                    listener.onFailure(asyncActionToken, exception)
//                }
//            })
//        } else {
//            mqttError("subscribeTopic error 连接已断开")
//            listener.onFailure(null, IllegalStateException("连接已断开"))
//        }
//
//    }
//
//
//    override fun unsubscribeTopic(topic: String, listener: IMqttActionListener) {
//        mTopics.remove(topic)
//        if (checkIsConnected()) {
//            mClient.unsubscribe(topic, null, object : IMqttActionListener {
//                override fun onSuccess(asyncActionToken: IMqttToken?) {
//                    mqttDebug("unsubscribeTopic onSuccess $topic $asyncActionToken ")
//                    listener.onSuccess(asyncActionToken)
//                }
//
//                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
//                    mqttError("unsubscribeTopic onFailure $topic $asyncActionToken $exception")
//                    listener.onFailure(asyncActionToken, exception)
//                }
//            })
//        } else {
//            mqttError("unsubscribeTopic error 连接已断开")
//            listener.onFailure(null, IllegalStateException("连接已断开"))
//        }
//
//    }
//
//    override fun publish(
//        topic: String,
//        payload: ByteArray,
//        qos: Int,
//        retained: Boolean,
//        listener: IMqttActionListener
//    ) {
//        if (checkIsConnected()) {
//            mClient.publish(topic, payload, qos, retained, null, object : IMqttActionListener {
//                override fun onSuccess(asyncActionToken: IMqttToken?) {
//                    mqttDebug("publish onSuccess $topic $asyncActionToken ")
//                    listener.onSuccess(asyncActionToken)
//                }
//
//                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
//                    mqttError("publish onFailure $topic $asyncActionToken $exception")
//                    listener.onFailure(asyncActionToken, exception)
//                }
//            })
//        } else {
//            mqttError("publish error 连接已断开")
//            listener.onFailure(null, IllegalStateException("连接已断开"))
//        }
//    }
//
//    override fun disconnect() {
//        mqttDebug("disconnect")
//        cancelSubscribeTopics()
//        try {
//            mState.set(MqttState.Init)
//            if (this::mClient.isInitialized) {
//                mClient.close()
//                mClient.disconnect()
//            }
//        } catch (e: Throwable) {
//            e.printStackTrace()
//            le("disconnect fail $e")
//        }
//        clearInternal()
//
//    }
//
//    private fun clearInternal(){
//        mqttDebug("clearInternal")
//        onMessageChangedListeners.clear()
//        onStateChangedListeners.clear()
//        onStateChangedListener = null
//        onMessageChangedListener = null
//    }
//
//    override fun clear() {
//        mqttDebug("clear")
//    }
//
//    private fun handleMessage(topic: String, message: MqttMessage?) {
//        if (topic.isBlank() || message == null) return
//        onMessageChangedListener?.onChanged(this, topic, message)
//        onMessageChangedListeners.forEach {
//            it.onChanged(this, topic, message)
//        }
//    }
//
//    private fun checkIsConnected(): Boolean = (mClient.isConnected && mState.get().isConnected)
//
//    private fun onStateChanged(state: MqttState) {
//        mqttInfo("onStateChanged state:$state")
//        mState.set(state)
//        onStateChangedListener?.onChanged(this, state)
//        onStateChangedListeners.forEach {
//            it.onChanged(this, state)
//        }
//    }
//
//    private fun cancelSubscribeTopics() {
//        mqttDebug("cancelSubscribeToTopics")
//        try {
//            sPool.shutdown()
//        } catch (e: Throwable) {
//            e.printStackTrace()
//        }
//    }
//    private fun CopyOnWriteArraySet<String>.subscribeTopics() {
//
//        fun doSingle(topic: String) {
//            mqttDebug("subscribeToTopics doSingle topic:$topic")
//            if (checkIsConnected()) {
//                subscribeTopic(topic, object : IMqttActionListener {
//                    override fun onSuccess(asyncActionToken: IMqttToken?) {
//                        mqttDebug("subscribeToTopics doSingle topic:$topic onSuccess")
//
//                    }
//
//                    override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
//                        mqttDebug("subscribeToTopics doSingle topic:$topic onFailure $exception")
//                        Thread.sleep(1000)
//                        doSingle(topic)
//                    }
//                })
//            } else {
//                throw IllegalStateException("mqtt断开连接")
//            }
//        }
//        sPool.submit {
//            this.forEach {
//                doSingle(it)
//            }
//        }
//    }
//
//    companion object {
//        private val sPool = Executors.newCachedThreadPool()
//        internal const val KEEP_ALIVE = 60 // 心跳检测时间间隔
//        internal const val CONNECTION_TIMEOUT = 60
//
//    }
//}