package com.yunext.twins.base.sp

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class Sp<T>(private val sDefaultValue: T?) :
    ReadWriteProperty<Any?, T?> {

    abstract fun putInt(key: String, value: Int)
    abstract fun putLong(key: String, value: Long)
    abstract fun putFloat(key: String, value: Float)
    abstract fun putString(key: String, value: String)
    abstract fun putBoolean(key: String, value: Boolean)
    abstract fun putObj(key: String, value: Any)

    abstract fun getInt(key: String, defaultValue: Int): Int
    abstract fun getLong(key: String, defaultValue: Long): Long
    abstract fun getFloat(key: String, defaultValue: Float): Float
    abstract fun getString(key: String, defaultValue: String): String?
    abstract fun getBoolean(key: String, defaultValue: Boolean): Boolean
    abstract fun getObj(key: String, defaultValue: Any?): Any?


    @Suppress("UNCHECKED_CAST")
    override fun getValue(thisRef: Any?, property: KProperty<*>): T? {
        val key = property.name
        return when (sDefaultValue) {
            is Long -> {
                getLong(key, sDefaultValue) as? T
            }
            is String -> {
                getString(key, sDefaultValue) as? T
            }
            is Float -> {
                getFloat(key, sDefaultValue) as? T
            }
            is Int -> {
                getInt(key, sDefaultValue) as? T
            }
            is Boolean -> {
                getBoolean(key, sDefaultValue) as? T
            }
            else -> {
                try {
                    getObj(key, sDefaultValue) as? T
                } catch (e: Throwable) {
                    null
                }
            }
        }
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
        val key = property.name
        when (value) {
            null -> {

            }
            is Long -> {
                putLong(key, value)
            }
            is String -> {
                putString(key, value)
            }
            is Float -> {
                putFloat(key, value)
            }
            is Int -> {
                putInt(key, value)
            }
            is Boolean -> {
                putBoolean(key, value)
            }

            else -> {
                try {
                    putObj(key, value)
                } catch (e: Throwable) {
                    e.printStackTrace()
                }
            }
        }
    }

}

