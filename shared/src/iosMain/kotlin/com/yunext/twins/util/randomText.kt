package com.yunext.twins.util

private const val BASE = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
actual fun randomText(count: Int): String {
    return List(count) {
        BASE.random().toString()
    }.joinToString { it }
}

actual fun md5(text: String, upperCase: Boolean): String? {
    return "ios md5 未实现"
}