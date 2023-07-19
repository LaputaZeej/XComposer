package com.yunext.twins.data.tsl

import com.yunext.twins.module.http.tsl.TslPropertyResp
import com.yunext.twins.module.http.tsl.TslResp
import com.yunext.twins.module.http.tsl.TslServiceResp

class Tsl(
    val id: String,
    val version: String,
    val productKey: String,
    val current: Boolean,
    val events: List<TslEvent>,
    val properties: List<TslProperty>,
    val services: List<TslService>
)


fun TslResp.convert()=Tsl(
    id = this.id?:"",
    version = this.version?:"",
    productKey = this.productKey?:"",
    current = this.current?:false,
    events = this.events?.map {
        it.convert()
    }?: listOf<TslEvent>(),
    properties = this.properties?.map(TslPropertyResp::convert)?: listOf(),
    services = this.services?.map(TslServiceResp::convert)?: listOf(),

)