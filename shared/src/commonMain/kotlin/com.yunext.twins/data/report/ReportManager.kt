package com.yunext.twins.data.report

import com.yunext.farm.common.logger.ILogger
import com.yunext.twins.base.logger.DefaultLogger
import com.yunext.twins.data.devicemanager.DeviceStore
import com.yunext.twins.module.repository.ReportRepository
import kotlinx.coroutines.*

class ReportManager(
    private val coroutineScope: CoroutineScope,
    private val deviceStore: DeviceStore,
    private val reportRepository: ReportRepository,
) : Reporter, ILogger by DefaultLogger("_ReportManager_") {

    private var mJob: Job? = null
    private val mJobMap: MutableMap<String, Job> = mutableMapOf()
    private var mDeviceIdAndReportData: DeviceIdAndReportData =
        AllDeviceIdAndReportData(deviceStore.device.generateId())

    override fun setData(data: DeviceIdAndReportData) {
        this.mDeviceIdAndReportData = data
    }

    override fun start() {
        ld("start")
        mJob?.cancel()
        mJobMap.iterator().forEach {
            it.value.cancel()
        }
        mJobMap.clear()

        when (val data = mDeviceIdAndReportData) {
            is CustomDeviceIdAndReportData -> {
                val reports = data.reports
                reports.forEach { report ->
                    mJobMap[report.id] = coroutineScope.launch {
                        ld("[custom]${data.deviceId}/${report.id} ${report.on} ${report.interval} ${report.repeat} <<<start")
                        var count = 0L
                        val repeat = report.repeat
                        while (report.on) {
                            ensureActive()
                            delay(report.interval * 1000L)
                            val v = deviceStore.propertiesFlow.value[report.id]
                            if (v != null) {
                                try {
                                    deviceStore.reportProperties(v)
                                } catch (e: Throwable) {
                                    e.printStackTrace()
                                }
                            }
                            ld("[custom]${data.deviceId}/${report.id} <<<doing:$count")
                            count++
                            if (repeat != ReportData.DEFAULT_REPEAT && count > repeat) {
                                break
                            }
                            if (count==10000L)count=0
                        }
                        ld("[custom]${data.deviceId}/${report.id} <<<end")
                    }
                }

            }
            is AllDeviceIdAndReportData -> {
                mJob = coroutineScope.launch {
                    ld("[all]${data.deviceId} ${data.on} ${data.interval} ${data.repeat} <<<start")
                    var count = 0L
                    val interval = data.interval*1000L
                    val repeat = data.repeat
                    while (data.on) {
                        ensureActive()
                        delay(interval)
                        val list = deviceStore.propertiesFlow.value.map {
                            it.value
                        }
                        if (list.isNotEmpty()) {
                            try {
                                deviceStore.reportProperties(*list.toTypedArray())
                            } catch (e: Throwable) {
                                e.printStackTrace()
                            }

                        }
                        ld("[all]${data.deviceId} <<<doing:$count")
                        count++
                        if (repeat != ReportData.DEFAULT_REPEAT && count > repeat) {
                            break
                        }
                        if (count==10000L)count=0
                    }
                    ld("[all]${data.deviceId} <<<end")
                }
            }
        }
    }

    override fun stop() {
        ld("stop")
        mJob?.cancel()
        mJobMap.iterator().forEach {
            it.value.cancel()
        }
        mJobMap.clear()
    }

}