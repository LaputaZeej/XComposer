package com.yunext.twins.data

import androidx.compose.runtime.Stable
import com.yunext.twins.util.randomText
import kotlin.random.Random

@Stable
internal data class ServiceData(
    val name: String,
    val key: String,
    val required: Boolean,
    val async: Boolean,
    val input: List<*>,
    val output: List<*>,
    val desc: String,
) {
    companion object {
        internal fun random() = ServiceData(
            name = randomText(),
            key = randomText(),
            required = Random.nextBoolean(),
            async = Random.nextBoolean(),
            input = List(Random.nextInt(4)) { it },
            output = List(Random.nextInt(4)) { it },
            desc = randomText(),
        )
    }
}