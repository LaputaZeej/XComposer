package com.yunext.twins.base.logger

import com.yunext.farm.common.logger.ILogger

class DefaultLogger(override val defaultTag: String, override val debug: Boolean = true) : ILogger {

    private var tagInterceptor: (String) -> String = {
        it.ifBlank {
            EMPTY_TAG
        }
    }
    private var msgInterceptor: (Any?) -> String = {
        it?.toString() ?: EMPTY_MSG
    }

    var tagInterceptors: Array<(String) -> String> = arrayOf()
    var msgInterceptors: Array<(Any?) -> Any?> = arrayOf()

    override fun li(tag: String, msg: Any?) {
        log(tag, msg, Level.I)
    }


    override fun le(tag: String, msg: Any?) {
        log(tag, msg, Level.E)
    }

    override fun lw(tag: String, msg: Any?) {
        log(tag, msg, Level.W)
    }

    override fun ld(tag: String, msg: Any?) {
        log(tag, msg, Level.D)
    }

    private fun log(tag: String, msg: Any?, level: Level) {
        if (!debug) return
        var tagTemp: String = tag
        if (tagInterceptors.isNotEmpty()) {
            tagInterceptors.forEach {
                tagTemp = it.invoke(tag)
            }
        }
        var msgTemp: Any? = msg
        if (msgInterceptors.isNotEmpty()) {
            msgInterceptors.forEach {
                msgTemp = it.invoke(tag)
            }
        }
        val finalTag = tagInterceptor.invoke(tagTemp)
        val finalMsg = msgInterceptor.invoke(msgTemp)
        when (level) {
            Level.D -> println("[D]$finalTag$finalMsg")
            Level.I -> println("[I]$finalTag$finalMsg")
            Level.W -> println("[W]$finalTag$finalMsg")
            Level.E -> println("[E]$finalTag$finalMsg")
        }
    }

    companion object : ILogger by DefaultLogger("_hadlinks_", true) {
        private const val EMPTY_MSG = "the msg is null ."
        private const val EMPTY_TAG = "_hadlinks_"

    }

    private enum class Level {
        D, I, W, E;
    }
}