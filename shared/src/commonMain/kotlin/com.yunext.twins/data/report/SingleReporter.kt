package com.yunext.twins.data.report

import com.yunext.twins.data.devicemanager.DeviceStore
import kotlinx.coroutines.*

@Deprecated("")
class SingleReporter(
    private val coroutineScope: CoroutineScope,
    private val deviceStore: DeviceStore
) : Reporter {
    private val mMap: MutableMap<String, ReportTask> = mutableMapOf()
    private val mJobMap: MutableMap<String, Job> = mutableMapOf()

    init {
        deviceStore.propertiesFlow.value.forEach() {
            val pk = it.value.key
            val task = ReportTask(pk, true, 5000)
            mMap[pk.identifier] = task

        }
    }

    override
    fun start() {
        coroutineScope.launch {
            mMap.forEach {
                startTask(it.value)
            }
        }
    }

    override
    fun stop() {
        mJobMap.forEach {
            it.value.cancel()
        }
        mJobMap.clear()
    }

    override fun setData(data: DeviceIdAndReportData) {
        TODO("Not yet implemented")
    }

    private fun startTask(task: ReportTask) {
        val id = task.propertyKey.identifier
        mJobMap[id]?.cancel()
        mJobMap[id] = coroutineScope.launch {
            while (task.repeat) {
                ensureActive()
                delay(task.interval)
                val v = deviceStore.propertiesFlow.value[id]
                if (v != null) {
                    deviceStore.reportProperties(v)
                }
            }
        }
    }
}