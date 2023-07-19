package com.yunext.farm.common.logger

interface ILogger {
    val debug: Boolean
    val defaultTag: String
        get() = "_hadlinks_"

    fun li(tag: String, msg: Any?)
    fun le(tag: String, msg: Any?)
    fun lw(tag: String, msg: Any?)
    fun ld(tag: String, msg: Any?)

    fun li(msg: Any?) {
        li(defaultTag, msg)
    }

    fun le(msg: Any?) {
        le(defaultTag, msg)
    }

    fun lw(msg: Any?) {
        lw(defaultTag, msg)
    }

    fun ld(msg: Any?) {
        ld(defaultTag, msg)
    }
}