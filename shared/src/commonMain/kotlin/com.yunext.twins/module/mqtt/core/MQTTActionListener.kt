package com.yunext.twins.module.mqtt.core

interface MQTTActionListener {
    fun onSuccess(token: Any)
    fun onFailure(token: Any, exception: Throwable)
}

fun interface MQTTSuccessActionListener{
    fun onSuccess(token: Any)
}

fun interface MQTTFailureActionListener{
    fun onFailure(token: Any, exception: Throwable)
}