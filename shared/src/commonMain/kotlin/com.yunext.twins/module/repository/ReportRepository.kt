package com.yunext.twins.module.repository

import com.yunext.twins.data.report.DeviceAndFirstReportData
import com.yunext.twins.data.report.DeviceIdAndReportData


interface ReportRepository {

    suspend fun put(id: String, data: DeviceIdAndReportData?): Boolean
    suspend fun take(id: String): DeviceIdAndReportData?

    suspend fun putFirstReport(id: String, data: DeviceAndFirstReportData?): Boolean
    suspend fun takeFirstReport(id: String): DeviceAndFirstReportData?


}