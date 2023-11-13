package com.yunext.twins.module.mqtt.core

import org.eclipse.paho.client.mqttv3.MqttConnectOptions

internal fun MqttConnectOptions.defaultFactory(ssl: Boolean = true) {
    if (ssl) {
        socketFactory = if (empty) {
            DefaultSSL.defaultFactory()
        } else SSLUtils.getSocketFactory(caFilePath, clientCrtFilePath, clientKeyFilePath, "")
    }
}

private const val empty = true
private const val caFilePath = "/cacert.pem"
private const val clientCrtFilePath = "/client.pem"
private const val clientKeyFilePath = "/client.key"