package com.yunext.twins.module.mqtt.data

class MQTTParam(
    val username: String,
    val password: String,
    val clientId: String,
    val url: String = "",
    val ssl: Boolean = true,
)

val MQTTParam.display: String
    get() = "[username]${username} ## [password]${password} ## [clientId]${clientId} ## [url]${url} ## [ssl]${ssl}"
