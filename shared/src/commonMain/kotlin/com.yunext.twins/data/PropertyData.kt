package com.yunext.twins.data

import androidx.compose.runtime.Stable
import com.yunext.twins.util.randomText
import kotlin.jvm.JvmStatic
import kotlin.random.Random

@Stable
internal data class PropertyData(
    val name: String = "",
    val key: String ="",
    val required: Boolean = false,
    val readWrite: ReadWrite = ReadWrite.R,
    val type: String = "",
    val desc: String,
) {
    enum class ReadWrite {
        R, W, RW;
    }

    companion object{

        @JvmStatic
        internal fun random() = PropertyData(
            randomText(), randomText(6),
            Random.nextBoolean(),
            ReadWrite.values().random(), randomText(), randomText(8)
        )
    }
}