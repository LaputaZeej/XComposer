//package com.yunext.twins.module.repository
//
//import android.content.Context
//import com.google.gson.reflect.TypeToken
//import com.tencent.mmkv.MMKV
//import com.yunext.farm.common.logger.DefaultLogger
//import com.yunext.farm.common.logger.ILogger
//import com.yunext.farm.twins.data.*
//import com.yunext.farm.twins.device.ext.MQTT_GSON
//import com.yunext.twins.data.report.AllDeviceIdAndReportData
//import com.yunext.twins.data.report.CustomDeviceIdAndReportData
//import com.yunext.twins.data.report.DeviceAndFirstReportData
//import com.yunext.twins.data.report.DeviceIdAndReportData
//import kotlinx.coroutines.suspendCancellableCoroutine
//import javax.inject.Inject
//import kotlin.coroutines.resume
//
//class ReportRepositoryImpl @Inject constructor(context: Context, mmkv: MMKV) : ReportRepository,
//    ILogger by DefaultLogger("_ReportRepository_") {
//
//    private var mAllReportJson: String? by MMKVSp(context.applicationContext, mmkv, "")
//    private var mCustomReportJson: String? by MMKVSp(context.applicationContext, mmkv, "")
//    private var mReportType: Int? by MMKVSp(context.applicationContext, mmkv, 999)
//    private var mFirstReportJson: String? by MMKVSp(context.applicationContext, mmkv, "")
//
//    override suspend fun put(id: String, data: DeviceIdAndReportData?): Boolean {
//        return suspendCancellableCoroutine {
//            try {
//                ld("[put] $id $data")
//                if (id.isEmpty() || data == null) {
//                    ld("[put]null $id $data ")
//                    mReportType = 999
//                    ld("[put]null $id $data type=$mReportType")
//                    it.resume(true)
//                } else {
//                    when (data) {
//                        is AllDeviceIdAndReportData -> {
//                            ld("[put]AllDeviceIdAndReportData $id $data ")
//                            mReportType = 0
//                            mAllReportJson = MQTT_GSON.toJson(data)
//                            it.resume(true)
//                        }
//                        is CustomDeviceIdAndReportData -> {
//                            ld("[put]CustomDeviceIdAndReportData $id $data ")
//                            val list = fromJsonCustomDeviceIdAndReportData(mCustomReportJson)
//                            val finalList = if (list.isNullOrEmpty()) {
//                                listOf(data)
//                            } else {
//                                list.filter { d ->
//                                    d.deviceId != id
//                                } + data
//                            }
//                            mReportType = 1
//                            mCustomReportJson = MQTT_GSON.toJson(finalList)
//                            it.resume(true)
//                        }
//                        null -> {
//                            ld("[put]null $id $data ")
//                            mReportType = 999
//                            ld("[put]null $id $data type=$mReportType")
//                            it.resume(true)
//                        }
//                    }
//                }
//
//            } catch (e: Throwable) {
//                e.printStackTrace()
//                ld("[put] error $e")
//                it.resume(false)
//            }
//        }
//    }
//
//    override suspend fun take(id: String): DeviceIdAndReportData? = suspendCancellableCoroutine {
//        try {
//            ld("[take] $id $mReportType")
//            when (mReportType) {
//                0 -> {
//                    val data =
//                        MQTT_GSON.fromJson(mAllReportJson, AllDeviceIdAndReportData::class.java)
//                    it.resume(data)
//                }
//                1 -> {
//                    val list = fromJsonCustomDeviceIdAndReportData(mCustomReportJson)
//                    val tsl = list.singleOrNull { d ->
//                        d.deviceId == id
//                    }
//                    ld("[take] $id result $tsl")
//                    it.resume(tsl)
//                }
//                else -> {
//                    it.resume(null)
//                }
//            }
//        } catch (e: Throwable) {
//            e.printStackTrace()
//            ld("[take] $id error $e")
//            it.resume(null)
//        }
//    }
//
//    override suspend fun putFirstReport(id: String, data: DeviceAndFirstReportData?): Boolean {
//        return suspendCancellableCoroutine {
//            ld("[putFirstReport] $id ${data?.list}")
//            if (id.isEmpty() || data == null) {
//                ld("[putFirstReport] empty")
//                it.resume(true)
//            } else {
//                val json = mFirstReportJson
//                ld("[putFirstReport] json=${json}")
//                val newJson = if (json.isNullOrBlank()) {
//                    ld("[putFirstReport] empty 1")
//                    MQTT_GSON.toJson(listOf(data))
//                } else {
//                    val list = fromJsonForFirstReportList(mFirstReportJson)
//                    ld("[putFirstReport] size=${list.size}")
//                    val finalList = if (list.isNullOrEmpty()) {
//                        listOf(data)
//                    } else {
//                        list.filter { d ->
//                            d.deviceId != id
//                        } + data
//                    }
//                    ld("[putFirstReport] size=${finalList.size}")
//                    MQTT_GSON.toJson(finalList)
//                }
//                ld("[putFirstReport] newJson=${newJson}")
//                mFirstReportJson = newJson
//                it.resume(true)
//            }
//        }
//    }
//
//    override suspend fun takeFirstReport(id: String): DeviceAndFirstReportData? {
//        return suspendCancellableCoroutine {
//            try {
//                ld("[takeFirstReport] $id")
//                if (id.isEmpty()) {
//                    it.resume(null)
//                } else {
//                    val json = mFirstReportJson
//                    ld("[takeFirstReport] json=${json}")
//                    val data = if (json.isNullOrBlank()) {
//                        null
//                    } else {
//                        val list = fromJsonForFirstReportList(mFirstReportJson)
//                        if (list.isNullOrEmpty()) {
//                            null
//                        } else {
//                            list.singleOrNull() { d ->
//                                d.deviceId == id
//                            }
//                        }
//
//                    }
//                    ld("[takeFirstReport] ${data?.list}")
//                    it.resume(data)
//                }
//            } catch (e: Throwable) {
//                ld("[takeFirstReport] ${e.localizedMessage}")
//                it.resume(null)
//            }
//
//        }
//    }
//
//    companion object {
//        private val TYPE_TOKEN = object : TypeToken<List<CustomDeviceIdAndReportData>>() {}.type
//        private val TYPE_TOKEN_FIRST = object : TypeToken<List<DeviceAndFirstReportData>>() {}.type
//        private fun fromJsonCustomDeviceIdAndReportData(json: String?) = try {
//            MQTT_GSON.fromJson<List<CustomDeviceIdAndReportData>>(json, TYPE_TOKEN)
//        } catch (e: Throwable) {
//            listOf()
//        }
//
//
//        private fun fromJsonForFirstReportList(json: String?) = try {
//            MQTT_GSON.fromJson<List<DeviceAndFirstReportData>>(json, TYPE_TOKEN_FIRST)
//        } catch (e: Throwable) {
//            listOf()
//        }
//    }
//
//
//}