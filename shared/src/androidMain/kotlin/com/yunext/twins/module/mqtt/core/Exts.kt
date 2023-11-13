package com.yunext.twins.module.mqtt.core

import com.yunext.twins.base.logger.DefaultLogger
import com.yunext.twins.module.mqtt.data.MQTTMessage
import org.eclipse.paho.android.service.MqttAndroidClient
import org.eclipse.paho.android.service.MqttTraceHandler
import org.eclipse.paho.client.mqttv3.MqttMessage

private const val DEBUG = true
internal const val TAG = "_mqtt_"
internal val logger = DefaultLogger(TAG,DEBUG)

private const val TEMPLATE = "[%s]%s-->[message]=%s,[error]=%s"
fun MqttAndroidClient.initTrace() {
    setTraceEnabled(DEBUG)
    setTraceCallback(object : MqttTraceHandler {

        override fun traceDebug(tag: String?, message: String?) {
            mqttDebug(String.format(TEMPLATE, "Debug", tag ?: "-", message ?: "-", "-"))
        }

        override fun traceException(tag: String?, message: String?, e: Exception?) {
            mqttError(
                String.format(
                    TEMPLATE,
                    "Exception",
                    tag ?: "-",
                    message ?: "-",
                    e?.message ?: "-"
                )
            )
        }

        override fun traceError(tag: String?, message: String?) {
            mqttError(String.format(TEMPLATE, "Error", tag ?: "-", message ?: "-", "-"))
        }
    })
}

internal fun mqttDebug(msg: String) {
    logger.ld(TAG, msg)
}

internal fun mqttInfo(msg: String) {
    logger.li(TAG, msg)
}

internal fun mqttError(msg: String) {
    logger.le(TAG, msg)
}

fun MqttMessage.toMsg() = MQTTMessage(
    payload = this.payload,
    retained = this.isRetained, qos = this.qos, messageId = this.id, dup = this.isDuplicate

)