package com.yunext.twins.data.report

import com.yunext.twins.data.tsl.property.PropertyKey

@Deprecated("")
data class ReportTask(val propertyKey: PropertyKey, val repeat: Boolean, val interval: Long)