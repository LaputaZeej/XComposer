package com.yunext.twins.data.tsl.service

import com.yunext.twins.data.tsl.TslServiceCallType
import com.yunext.twins.data.tsl.property.PropertyKey

sealed interface ServiceKey {
    val identifier: String
    val name: String
    val desc: String
    val required: Boolean
    val type: TslServiceCallType
    val method: String?
    val inputData: List<PropertyKey>
    val outputData: List<PropertyKey>
}

class AsyncEventKey(
    override val identifier: String,
    override val name: String,
    override val desc: String,
    override val required: Boolean,
    override val method: String?,
    override val inputData: List<PropertyKey>,
    override val outputData: List<PropertyKey>,

    ) : ServiceKey {
    override val type: TslServiceCallType
        get() = TslServiceCallType.ASYNC
}

class SyncEventKey(
    override val identifier: String,
    override val name: String,
    override val desc: String,
    override val required: Boolean,
    override val method: String?,
    override val inputData: List<PropertyKey>,
    override val outputData: List<PropertyKey>,

    ) : ServiceKey {
    override val type: TslServiceCallType
        get() = TslServiceCallType.SYNC
}

