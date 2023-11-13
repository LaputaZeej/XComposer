package com.yunext.twins.module.mqtt.core

import com.yunext.twins.module.mqtt.data.MQTTParam
import org.eclipse.paho.client.mqttv3.IMqttMessageListener
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttException
import org.eclipse.paho.client.mqttv3.MqttMessage
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence


actual val mqttClient:MQTTClient by lazy {
    MQTTClientImpl()
}

class MQTTClientImpl:MQTTClient {
    override val clientId: String
        get() = "mqtt-desktop-${System.currentTimeMillis()}"

    override fun init() {
        println("mqtt-desktop-init")
        val broker = "ssl://${MQTTConstant.PATH}:${MQTTConstant.PORT}"
        val topic  = "xpl/abc"
        val qos = 2
        val memoryPersistence = MemoryPersistence()
        val content = "hello  i am from desktop"
        val ssl = true
        try {
            val client =  MqttClient(broker,clientId,memoryPersistence)
            val connOpts = MqttConnectOptions().apply {
                userName = "laputa"
                this.password = "123456".toCharArray()
                defaultFactory(ssl)
            }
            connOpts.isCleanSession = true
            pl("Connecting to broker: $broker")
            client.connect(connOpts)
            client.subscribeWithResponse(topic
            ) { tp, message ->

                pl("$tp # $message")
            }
            pl("Connected")
            pl("Publishing message: $content")
            val message = MqttMessage(content.toByteArray())
            message.qos = qos
            client.publish(topic, message)

            pl("Message published")
//            client.disconnect()
            pl("Disconnected")
        } catch (me: MqttException) {
            pl("reason "+me.reasonCode);
            pl("msg "+me.message)
            pl("loc "+me.localizedMessage)
            pl("cause "+me.cause)
            pl("excep $me")
            me.printStackTrace();
        } finally {
        }


    }

    private fun pl(msg:String){
        println("[_mqtt_]$msg")
    }

    override fun connect(param: MQTTParam, listener: MQTTActionListener) {
        println("mqtt-desktop-connect")
    }

    override fun subscribeTopic(topic: String, listener: MQTTActionListener) {
        println("mqtt-desktop-subscribeTopic")
    }

    override fun unsubscribeTopic(topic: String, listener: MQTTActionListener) {
        println("mqtt-desktop-unsubscribeTopic")
    }

    override fun publish(
        topic: String,
        payload: ByteArray,
        qos: Int,
        retained: Boolean,
        listener: MQTTActionListener,
    ) {
        println("mqtt-desktop-publish")
    }

    override fun disconnect() {
        println("mqtt-desktop-disconnect")
    }

    override fun clear() {
        println("mqtt-desktop-clear")
    }
}