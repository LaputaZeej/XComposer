package com.yunext.twins.data.report

sealed class DeviceIdAndReportData(
    val deviceId: String
) {

}

class CustomDeviceIdAndReportData(deviceId: String, val reports: List<ReportData>) :
    DeviceIdAndReportData(deviceId) {

}

 class AllDeviceIdAndReportData(
     deviceId: String,
     val on: Boolean = true,
     val interval: Long = ReportData.DEFAULT_INTERVAL,//s
     val repeat: Int = ReportData.DEFAULT_REPEAT
) : DeviceIdAndReportData(deviceId){
    override fun toString(): String {
        return "deviceId=$deviceId,interval = $interval"
    }
}

fun AllDeviceIdAndReportData.copy(time:Long): AllDeviceIdAndReportData {
    return AllDeviceIdAndReportData(deviceId= this.deviceId,on=this.on,interval=time,repeat=this.repeat)
}

