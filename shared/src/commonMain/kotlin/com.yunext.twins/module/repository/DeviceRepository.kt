package com.yunext.twins.module.repository

import com.yunext.twins.data.device.TwinsDevice
import com.yunext.twins.data.devicemanager.Context
import com.yunext.twins.data.tsl.Tsl

interface DeviceRepository {
    suspend fun findAll(): List<TwinsDevice>

    suspend fun delete(device: TwinsDevice): Boolean

    suspend fun edit(device: TwinsDevice): Boolean

    suspend fun add(device: TwinsDevice): Boolean

    @Deprecated("delete")
    suspend fun findTsl(device: TwinsDevice): Tsl?

    @Deprecated("delete")
    suspend fun findDeviceType(context: Context): Array<String>


    suspend fun loadDeviceTslValue(id: String): String
    suspend fun saveDeviceTslValue(id: String, json:String): Boolean

}