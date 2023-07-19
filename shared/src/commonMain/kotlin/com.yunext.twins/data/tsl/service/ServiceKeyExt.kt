package com.yunext.twins.data.tsl.service

import com.yunext.twins.data.tsl.property.logger
import com.yunext.twins.data.tsl.property.tslHandleParsePropertyKey
import com.yunext.twins.data.tsl.Tsl
import com.yunext.twins.data.tsl.TslProperty
import com.yunext.twins.data.tsl.TslService
import com.yunext.twins.data.tsl.TslServiceCallType


fun Tsl.tslHandleTsl2ServiceKeys(): Map<String, ServiceKey> {
    logger.ld("tslHandleTsl2ServiceKeys a")
    return try {
        val services = services
        if (services.isEmpty()) return mapOf()
        val all: MutableMap<String, ServiceKey> = mutableMapOf()
        services.forEach { service ->
            val id = service.identifier
            val key = tslHandleParseServiceKey(service)
            all[id] = key
        }
        all
    } catch (e: Throwable) {
        e.printStackTrace()
        logger.ld("tslHandleTsl2ServiceKeys error $e")
        mapOf()
    } finally {
        logger.ld("tslHandleTsl2ServiceKeys z")
    }
}

private fun tslHandleParseServiceKey(
    tslEvent: TslService
): ServiceKey {
    val type = tslEvent.callType
    return when (TslServiceCallType.from(type)) {
        TslServiceCallType.ASYNC -> AsyncEventKey(
            identifier = tslEvent.identifier,
            name = tslEvent.name,
            required = tslEvent.required,
            desc = tslEvent.desc,
            method = tslEvent.desc,
            inputData = tslEvent.inputData.map {
                val tslProperty = TslProperty.from(it)
                tslHandleParsePropertyKey(tslProperty)
            }.filterNotNull(),
            outputData = tslEvent.outputData.map {
                val tslProperty = TslProperty.from(it)
                tslHandleParsePropertyKey(tslProperty)
            }.filterNotNull()
        )
        TslServiceCallType.SYNC -> SyncEventKey(
            identifier = tslEvent.identifier,
            name = tslEvent.name,
            required = tslEvent.required,
            desc = tslEvent.desc,
            method = tslEvent.desc,
            inputData = tslEvent.outputData.map {
                val tslProperty = TslProperty.from(it)
                tslHandleParsePropertyKey(tslProperty)
            }.filterNotNull(),
            outputData = tslEvent.outputData.map {
                val tslProperty = TslProperty.from(it)
                tslHandleParsePropertyKey(tslProperty)
            }.filterNotNull()
        )
    }


}