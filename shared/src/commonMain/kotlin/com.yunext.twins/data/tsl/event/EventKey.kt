package com.yunext.twins.data.tsl.event

import com.yunext.twins.data.tsl.TslEventType
import com.yunext.twins.data.tsl.property.PropertyKey

sealed interface EventKey {
    val identifier: String
    val name: String
    val desc: String
    val required: Boolean
    val type: TslEventType
    val method: String?
    val outputData: List<PropertyKey>
}

class AlertEventKey(
    override val identifier: String,
    override val name: String,
    override val desc: String,
    override val required: Boolean,
    override val method: String?,
    override val outputData: List<PropertyKey>,

    ) : EventKey {
    override val type: TslEventType
        get() = TslEventType.ALERT
}

class InfoEventKey(
    override val identifier: String,
    override val name: String,
    override val desc: String,
    override val required: Boolean,
    override val method: String?,
    override val outputData: List<PropertyKey>,
) : EventKey {
    override val type: TslEventType
        get() = TslEventType.INFO
}

class ErrorEventKey(
    override val identifier: String,
    override val name: String,
    override val desc: String,
    override val required: Boolean,
    override val method: String?,
    override val outputData: List<PropertyKey>,

    ) : EventKey {
    override val type: TslEventType
        get() = TslEventType.ERROR
}
