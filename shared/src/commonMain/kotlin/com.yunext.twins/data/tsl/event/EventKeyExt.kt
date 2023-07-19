package com.yunext.twins.data.tsl.event

import com.yunext.twins.data.tsl.Tsl
import com.yunext.twins.data.tsl.TslEvent
import com.yunext.twins.data.tsl.TslEventType
import com.yunext.twins.data.tsl.TslProperty
import com.yunext.twins.data.tsl.property.logger
import com.yunext.twins.data.tsl.property.tslHandleParsePropertyKey

fun Tsl.tslHandleTsl2EventKeys(): Map<String, EventKey> {
    logger.ld("tslHandleTsl2EventKeys a")
    return try {
        val events = events
        if (events.isEmpty()) return mapOf()
        val all: MutableMap<String, EventKey> = mutableMapOf()
        events.forEach { event ->
            val id = event.identifier
            val key = tslHandleParseEventKey(event)
            all[id] = key
        }
        all
    } catch (e: Throwable) {
        e.printStackTrace()
        logger.ld("tslHandleTsl2EventKeys error $e")
        mapOf()
    } finally {
        logger.ld("tslHandleTsl2EventKeys z")
    }
}

private fun tslHandleParseEventKey(
    tslEvent: TslEvent
): EventKey {
    val type = tslEvent.type
    return when (TslEventType.from(type)) {
        TslEventType.ALERT -> AlertEventKey(
            identifier = tslEvent.identifier,
            name = tslEvent.name,
            required = tslEvent.required,
            desc = tslEvent.desc,
            method = tslEvent.desc,
            outputData = tslEvent.outputData.map {
                val tslProperty = TslProperty.from(it)
                tslHandleParsePropertyKey(tslProperty)
            }.filterNotNull()
        )
        TslEventType.INFO -> InfoEventKey(
            identifier = tslEvent.identifier,
            name = tslEvent.name,
            required = tslEvent.required,
            desc = tslEvent.desc,
            method = tslEvent.desc,
            outputData = tslEvent.outputData.map {
                val tslProperty = TslProperty.from(it)
                tslHandleParsePropertyKey(tslProperty)
            }.filterNotNull()
        )
        TslEventType.ERROR -> ErrorEventKey(
            identifier = tslEvent.identifier,
            name = tslEvent.name,
            required = tslEvent.required,
            desc = tslEvent.desc,
            method = tslEvent.desc,
            outputData = tslEvent.outputData.map {
                val tslProperty = TslProperty.from(it)
                tslHandleParsePropertyKey(tslProperty)
            }.filterNotNull()
        )
    }


}