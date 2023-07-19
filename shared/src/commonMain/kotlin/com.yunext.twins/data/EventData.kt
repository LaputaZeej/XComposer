package com.yunext.twins.data

import androidx.compose.runtime.Stable
import androidx.compose.ui.graphics.Color
import com.yunext.twins.ui.theme.app_appColor
import com.yunext.twins.ui.theme.app_blue_light
import com.yunext.twins.ui.theme.app_orange
import com.yunext.twins.ui.theme.app_orange_light
import com.yunext.twins.ui.theme.app_red
import com.yunext.twins.ui.theme.app_red_light
import com.yunext.twins.util.randomText
import kotlin.random.Random

@Stable
internal data class EventData(
    val name: String,
    val key: String,
    val required: Boolean,
    val eventType: EventType,
    val output: List<*>,
    val desc: String,
) {

    enum class EventType( val text:String, val color:Pair<Color,Color>) {
        Alert("alert", app_orange to app_orange_light),
        Info("info", app_appColor to app_blue_light),
        Fault("fault", app_red to app_red_light)
        ;
    }

    companion object {
        internal fun random() = EventData(
            name = randomText(),
            key = randomText(),
            required = Random.nextBoolean(),
            eventType = EventType.values().random(),
            output = List(Random.nextInt(4)) { it },
            desc = randomText(),
        )
    }
}