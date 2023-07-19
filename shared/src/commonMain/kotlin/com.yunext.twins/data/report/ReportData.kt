package com.yunext.twins.data.report

import kotlinx.serialization.Serializable

@Serializable
data class ReportData(
    val id: String,
    val name: String,
    val on: Boolean = true,
    val interval: Long = DEFAULT_INTERVAL,
    val repeat: Int = DEFAULT_REPEAT
)  {

    companion object {
        const val DEFAULT_INTERVAL = 5L
        const val DEFAULT_REPEAT = 0
    }
}