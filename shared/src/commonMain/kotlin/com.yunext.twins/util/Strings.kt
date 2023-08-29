package com.yunext.twins.util

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.compositionLocalOf
import kotlinx.datetime.Clock

expect fun randomText(count:Int = 4):String

expect fun md5(text:String,upperCase:Boolean =false):String?

fun currentTime() = Clock.System.now().toEpochMilliseconds()
