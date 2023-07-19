package com.yunext.twins.data

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.yunext.twins.ui.theme.app_gray_light
import com.yunext.twins.util.randomText

internal object ItemDefaults {
    internal val itemBackground = Color.White
    internal val itemShape = RoundedCornerShape(16.dp)

    internal val itemElevation = 12.dp

    internal val labelShape = RoundedCornerShape(8.dp)
    internal val editShape = RoundedCornerShape(4.dp)
    private val contentShape = RoundedCornerShape(8.dp)
    private val content4Shape = RoundedCornerShape(4.dp)

    internal val contentBorderColor = app_gray_light

    internal val contentValueShape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
    internal val contentSpecShape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)

    internal val contentValueMaxHeight = 200.dp

    internal val borderModifier = Modifier
        .fillMaxWidth()
        .clip(contentShape)
        .border(
            width = .5.dp,
            color = contentBorderColor,
            shape = contentShape
        )

    internal val border4Modifier = Modifier
        .fillMaxWidth()
        .clip(content4Shape)
        .border(
            width = .5.dp,
            color = contentBorderColor,
            shape = content4Shape
        )

    internal val valueTypes = listOf("int","bool","enum","date","double","text")

    internal fun randomTextInternal(take:Int = 4):String = randomText(take)//UUID.randomUUID().toString().take(take)


}

