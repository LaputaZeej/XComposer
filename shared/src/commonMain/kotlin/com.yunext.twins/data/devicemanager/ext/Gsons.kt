package com.yunext.twins.data.devicemanager.ext

import com.yunext.farm.common.logger.ILogger
/**
 * 通用gson
 */
//val MQTT_GSON =  Gson()


fun ILogger.gsonD(tag:String,msg:Any?){
    ld(tag,msg)
}


fun ILogger.gsonI(tag:String,msg:Any?){
    li(tag,msg)
}

fun ILogger.gsonE(tag:String,msg:Any?){
    le(tag,msg)
}