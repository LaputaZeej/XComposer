package com.yunext.twins.ui.compoents

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutModifier
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.layout.MeasureResult
import androidx.compose.ui.layout.MeasureScope
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.platform.InspectorValueInfo
import androidx.compose.ui.platform.debugInspectorInfo
import androidx.compose.ui.unit.Constraints

@Stable
fun Modifier.xplBackground(color: () -> Color): Modifier {
//    return this then BackgroundModifier(
//        color = color,
//        rtlAware = true,
//        inspectorInfo = debugInspectorInfo {
//            name = "color"
//            properties["color"] = color
//        }
//    )
    return this
        .drawWithContent {
            drawRect(color())
            drawContent()
        }
}

//private class BackgroundModifier(
//    val color: () -> Color,
//    val rtlAware: Boolean,
//    inspectorInfo: InspectorInfo.() -> Unit,
//) : LayoutModifier, InspectorValueInfo(inspectorInfo) {
//    override fun MeasureScope.measure(
//        measurable: Measurable,
//        constraints: Constraints,
//    ): MeasureResult {
//        val placeable = measurable.measure(constraints)
//
//
//        return layout(placeable.width, placeable.height) {
//            placeable.placeRelative(0,0)
//        }
//    }
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        val otherModifier = other as? BackgroundModifier ?: return false
//
//        return color == otherModifier.color
//    }
//
//    override fun hashCode(): Int {
//        var result = color.hashCode()
//        result = 31 * result + rtlAware.hashCode()
//        return result
//    }
//
//    override fun toString(): String = "BackgroundModifier(color=$color)"
//}

