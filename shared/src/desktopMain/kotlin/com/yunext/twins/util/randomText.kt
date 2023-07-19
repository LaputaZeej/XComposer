package com.yunext.twins.util

import java.util.UUID

actual fun randomText(count: Int): String {
   return UUID.randomUUID().toString().take(count)
}

actual fun md5(text: String, upperCase: Boolean): String? {
    TODO("Not yet implemented")
}