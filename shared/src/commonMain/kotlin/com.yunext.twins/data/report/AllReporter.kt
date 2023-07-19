package com.yunext.twins.data.report

import com.yunext.twins.data.devicemanager.DeviceStore
import kotlinx.coroutines.*

@Deprecated("")
class AllReporter(
    private val coroutineScope: CoroutineScope,
    private val deviceStore: DeviceStore,
    private val repeat: Int = -1,
    private val interval: Long = 10000,
) : Reporter {
    private var mJob: Job? = null

    override
    fun start() {
        mJob = coroutineScope.launch {
            var count = 0
            while (true) {
                ensureActive()
                delay(interval)
                val list = deviceStore.propertiesFlow.value.map {
                    it.value
                }
                if (list.isNotEmpty()) {
                    deviceStore.reportProperties(*list.toTypedArray())
                }
                count++
                if (repeat != -1 && count > repeat) {
                    break
                }
            }
        }
    }

    override
    fun stop() {
        mJob?.cancel()
    }

    override fun setData(data: DeviceIdAndReportData) {
        TODO("Not yet implemented")
    }
}