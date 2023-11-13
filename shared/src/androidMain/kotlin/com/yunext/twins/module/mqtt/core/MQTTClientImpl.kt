package com.yunext.twins.module.mqtt.core

import android.content.Context
import com.yunext.twins.AndroidApp
import com.yunext.twins.module.mqtt.data.MQTTMessage
import com.yunext.twins.module.mqtt.data.MQTTParam
import com.yunext.twins.module.mqtt.data.MQTTState
import com.yunext.twins.module.mqtt.data.isConnected
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.client.mqttv3.IMqttActionListener
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage
import java.lang.IllegalStateException
import java.util.concurrent.CopyOnWriteArraySet
import java.util.concurrent.atomic.AtomicReference

actual val mqttClient: MQTTClient by lazy {
    MQTTClientImpl(AndroidApp.application)
}

class MQTTClientImpl(context: Context) : MQTTClient {

    private val ctx: Context = context.applicationContext

    private lateinit var client: MqttAndroidClient

    private var stateInternal: AtomicReference<MQTTState> = AtomicReference(MQTTState.Init)

    override val clientId: String
        get() = "laputa-${System.currentTimeMillis()}"

    val state: MQTTState
        get() = stateInternal.get()

    private val mTopics: CopyOnWriteArraySet<String> = CopyOnWriteArraySet()

    var onStateChangedListener: OnMQTTStateChangedListener? = null
    private var onStateChangedListeners: MutableList<OnMQTTStateChangedListener> = mutableListOf()

    override fun init() {
        println("mqtt-android-init")
        val url = "ssl://${MQTTConstant.PATH}:${MQTTConstant.PORT}"
        client = MqttAndroidClient(ctx.applicationContext, url, clientId).also {
            it.setCallback(internalMqttCallback)
        }

        connect(
            MQTTParam(
                "laputa",
                "123456",
                clientId = clientId,
                url = url
            ), object : MQTTActionListener {
                override fun onSuccess(token: Any?) {
                    println("mqtt-android-init connect success token:$token")
                }

                override fun onFailure(token: Any?, exception: Throwable?) {
                    println("mqtt-android-init connect fail token:$token exception:$exception")
                }

            })
    }

    fun registerOnStateChangedListener(listener: OnMQTTStateChangedListener) {
        this.onStateChangedListeners.add(listener)
    }

    fun unRegisterOnStateChangedListener(listener: OnMQTTStateChangedListener) {
        this.onStateChangedListeners.remove(listener)
    }

    private var onMessageChangedListeners: MutableList<OnMQTTMessageChangedListener> =
        mutableListOf()
    var onMessageChangedListener: OnMQTTMessageChangedListener? = null
    fun registerOnMessageChangedListener(listener: OnMQTTMessageChangedListener) {
        this.onMessageChangedListeners.add(listener)
    }

    fun unRegisterOnMessageChangedListener(listener: OnMQTTMessageChangedListener) {
        this.onMessageChangedListeners.remove(listener)
    }

    private val internalMqttCallback = object : MqttCallbackExtended {
        override fun connectComplete(reconnect: Boolean, serverURI: String?) {
            mqttDebug("connectComplete reconnect:$reconnect @ $serverURI")
            onStateChanged(MQTTState.Connected)
            if (reconnect) {
                // Because Clean Session is true, we need to re-subscribe
                try {
                    //mTopics.subscribeTopics()



                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }


        override fun messageArrived(topic: String, message: MqttMessage?) {
            try {
                mqttDebug(
                    "messageArrived $topic # $message ---> ${
                        message?.payload?.let {
                            String(it)
                        }
                    }"
                )
                handleMessage(topic, message?.toMsg())
            } catch (e: Throwable) {
                mqttError("messageArrived error：$e")
            }
        }

        override fun connectionLost(cause: Throwable?) {
            onStateChanged(MQTTState.Disconnected)
            mqttError("connectionLost cause：$cause")
        }

        override fun deliveryComplete(token: IMqttDeliveryToken?) {
            mqttError("deliveryComplete token：${token?.client?.clientId}")
        }
    }

    override fun connect(param: MQTTParam, listener: MQTTActionListener) {
        println("mqtt-android-connect")

        val options = MqttConnectOptions().apply {
            // 是否自动重新连接。当客户端网络异常或进入后台后导致连接中断，在这期间会不断的尝试重连，
            // 重连等待最初会等待1 秒钟, 每次重连失败等待时间就会加倍，直到 2 分钟，此时延迟将保持在 2 分钟。
            isAutomaticReconnect = true
            // 是否自动清除 session. 注意如果为 true 则会清除session. 会导致如果你掉线的期间，
            // 你所订阅的topic有新的消息，等你重新连接上后因为session被清除了，你将无法接收到在你
            // 离线期间的新消息
            isCleanSession = true
            connectionTimeout = 60
            keepAliveInterval = 60
            // setWill(HadCategory.STATUS.createTopic(device.appType().strValue, device.mac), byteArrayOf(),1,true)
            this.password = param.password.toCharArray()
            this.userName = param.username
            if (param.ssl) {
                socketFactory = DefaultSSL.defaultFactory()
            }
        }

        client.connect(options, null, object : IMqttActionListener {
            override fun onSuccess(asyncActionToken: IMqttToken?) {
                mqttDebug("connect onSuccess $asyncActionToken")
                listener.onSuccess(asyncActionToken ?: "")

                subscribeTopic("xpl/abc", object : MQTTActionListener {
                    override fun onSuccess(token: Any?) {
                        println("mqtt-android-init subscribeTopic success token:$token")
                    }

                    override fun onFailure(token: Any?, exception: Throwable?) {
                        println("mqtt-android-init subscribeTopic success token:$token")
                    }

                })

            }

            override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                mqttError("connect onFailure $asyncActionToken # $exception")
                listener.onFailure(asyncActionToken ?: "", exception)

            }

        })
    }

    override fun subscribeTopic(topic: String, listener: MQTTActionListener) {
        println("mqtt-android-subscribeTopic")
//        if (checkIsConnected()) {
            client.subscribe(topic, 0, null, object : IMqttActionListener {
                override fun onSuccess(asyncActionToken: IMqttToken?) {
                    mqttDebug("subscribeTopic onSuccess $topic $asyncActionToken")
                    listener.onSuccess(asyncActionToken)
                }

                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
                    mqttError("subscribeTopic onFailure $topic $asyncActionToken $exception")
                    listener.onFailure(asyncActionToken, exception)
                }
            })
//        } else {
//            mqttError("subscribeTopic error 连接已断开")
//            listener.onFailure(null, IllegalStateException("连接已断开"))
//        }

    }

    override fun unsubscribeTopic(topic: String, listener: MQTTActionListener) {
        println("mqtt-android-unsubscribeTopic")
    }


    override fun publish(
        topic: String,
        payload: ByteArray,
        qos: Int,
        retained: Boolean,
        listener: MQTTActionListener,
    ) {
        println("mqtt-android-publish")
    }

    override fun disconnect() {
        println("mqtt-android-disconnect")
    }

    override fun clear() {
        println("mqtt-android-clear")
    }


    private fun handleMessage(topic: String, message: MQTTMessage?) {
        if (topic.isBlank() || message == null) return
        onMessageChangedListener?.onChanged(this, topic, message)
        onMessageChangedListeners.forEach {
            it.onChanged(this, topic, message)
        }
    }

    private fun checkIsConnected(): Boolean =
        (client.isConnected && stateInternal.get().isConnected)

    private fun onStateChanged(state: MQTTState) {
        mqttInfo("onStateChanged state:$state")
        stateInternal.set(state)
        onStateChangedListener?.onChanged(this, state)
        onStateChangedListeners.forEach {
            it.onChanged(this, state)
        }
    }
}