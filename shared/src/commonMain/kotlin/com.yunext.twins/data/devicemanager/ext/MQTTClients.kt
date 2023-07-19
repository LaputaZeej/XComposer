//package com.yunext.twins.data.devicemanager.ext
//
//import com.yunext.farm.mqtt.core.HadlinksMqttClient
//import com.yunext.twins.module.mqtt.core.OnStateChangedListener
//import com.yunext.twins.module.mqtt.data.MqttParam
//import com.yunext.twins.module.mqtt.data.MqttState
//import kotlinx.coroutines.suspendCancellableCoroutine
//import org.eclipse.paho.client.mqttv3.IMqttActionListener
//import org.eclipse.paho.client.mqttv3.IMqttToken
//import kotlin.coroutines.resume
//
//suspend fun HadlinksMqttClient.suspendConnect(param: MqttParam) =
//    suspendCancellableCoroutine<Boolean> { con ->
//        var hasResume = false
//        val listener = object : OnStateChangedListener {
//            override fun onChanged(mqttClient: HadlinksMqttClient, mqttState: MqttState) {
//                unRegisterOnStateChangedListener(this)
//                if (hasResume || con.isCompleted || con.isCancelled) {
//                    return
//                }
//                if (state == MqttState.Connected) {
//                    ld("_mqtt_", "suspendConnect OnStateChangedListener connected ")
//                    con.resume(true)
//                } else {
//                    ld("_mqtt_", "suspendConnect OnStateChangedListener disconnected ")
//                    con.resume(false)
//                }
//            }
//        }
//        this.registerOnStateChangedListener(listener)
//        try {
//            this.connect(param, object : IMqttActionListener {
//                override fun onSuccess(asyncActionToken: IMqttToken?) {
//                    // it.resume(true)
//                    ld("_mqtt_", "suspendConnect  onSuccess")
//                }
//
//                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
//                    ld("_mqtt_", "suspendConnect  onFailure")
//                    unRegisterOnStateChangedListener(listener)
//                    if (hasResume) return
//                    hasResume = true
//                    con.resume(false)
//                }
//            })
//        } catch (e: Throwable) {
//            ld("_mqtt_", "suspendConnect  error $e")
//            this.unRegisterOnStateChangedListener(listener)
//            if (!hasResume) {
//                hasResume = true
//                con.resume(false)
//            }
//        }
//
//        con.invokeOnCancellation {
//            ld("_mqtt_", "suspendConnect::invokeOnCancellation ")
//            this.unRegisterOnStateChangedListener(listener)
//        }
//    }
//
//
//suspend fun HadlinksMqttClient.suspendSubscribeTopic(topic: String): Boolean {
//    return suspendCancellableCoroutine {
//        try {
//            this.subscribeTopic(topic, object : IMqttActionListener {
//                override fun onSuccess(asyncActionToken: IMqttToken?) {
//                    it.resume(true)
//                }
//
//                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
//                    it.resume(false)
//                }
//            })
//        } catch (e: Throwable) {
//            it.resume(false)
////            it.resumeWithException(e)
//        }
//
//    }
//}
//
//suspend fun HadlinksMqttClient.suspendUnsubscribeTopic(topic: String): Boolean {
//    return suspendCancellableCoroutine {
//        try {
//            this.unsubscribeTopic(topic, object : IMqttActionListener {
//                override fun onSuccess(asyncActionToken: IMqttToken?) {
//                    it.resume(true)
//                }
//
//                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
//                    it.resume(false)
//                }
//            })
//        } catch (e: Throwable) {
//            e.printStackTrace()
//        }
//
//    }
//}
//
//suspend fun HadlinksMqttClient.suspendPublish(
//    topic: String,
//    payload: ByteArray,
//    qos: Int,
//    retained: Boolean
//): Boolean {
//    return suspendCancellableCoroutine {
//        try {
//            this.publish(topic, payload, qos, retained, object : IMqttActionListener {
//                override fun onSuccess(asyncActionToken: IMqttToken?) {
//                    it.resume(true)
//                }
//
//                override fun onFailure(asyncActionToken: IMqttToken?, exception: Throwable?) {
//                    it.resume(false)
//                }
//            })
//        } catch (e: Throwable) {
//            e.printStackTrace()
//        }
//    }
//}