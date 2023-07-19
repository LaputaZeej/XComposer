package com.yunext.twins.data.report

interface Reporter {
    fun start()
    fun stop()
    fun setData(data: DeviceIdAndReportData)
}