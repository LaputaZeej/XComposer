package com.yunext.twins.module.http.tsl

class TslResp(
    val id: String?,
    val version: String?,
    val productKey: String?,
    val current: Boolean?,
    val events: List<TslEventResp>?,
    val properties: List<TslPropertyResp>?,
    val services: List<TslServiceResp>?
)