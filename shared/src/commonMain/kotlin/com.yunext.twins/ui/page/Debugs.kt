package com.yunext.twins.ui.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.layout
import androidx.compose.ui.node.DrawModifierNode
import androidx.compose.ui.node.ModifierNodeElement
import androidx.compose.ui.platform.InspectorInfo
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yunext.twins.ui.compoents.Debug
import com.yunext.twins.ui.compoents.dialog.CHAlertDialog
import com.yunext.twins.ui.compoents.xplBackground
import com.yunext.twins.ui.compoents.xplRandomColor

private fun getPersonList(): MutableList<String> {
    val mutableListOf = mutableListOf<String>()
    for (i in 0..100) {
        mutableListOf.add("i$i")
    }
    return mutableListOf
}

@Composable
fun ListItemAnimationComponent() {
    val personList by remember {
        mutableStateOf(getPersonList())
    }
    val deletedPersonList = remember { mutableStateListOf<String>() }
    Box(modifier = Modifier.width(150.dp)) {
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            itemsIndexed(items = personList) { index, item ->
                Card(shape = RoundedCornerShape(4.dp), modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(xplRandomColor()),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = item, style = TextStyle(
                                    color = Color.Black,
                                    fontSize = 20.sp,
                                    textAlign = TextAlign.Center
                                ), modifier = Modifier.padding(16.dp)
                            )
                            IconButton(onClick = { personList.remove(item) }) {
                                Icon(
                                    imageVector = Icons.Filled.Delete, contentDescription = "delete"
                                )
                            }
                        }
                        Divider(modifier = Modifier.height(1.dp))
                    }

                }
            }
        }

        DebugTest()
    }


}

@Composable
private inline fun DebugTest() {
    var show by remember {
        mutableStateOf(false)
    }
    Box(Modifier.fillMaxSize()) {
        Button(
            onClick = { show = !show },
            modifier = Modifier.Companion.align(Alignment.BottomCenter).padding(100.dp)
        ) {
            Text(text = if (show) "关闭" else "开启")
        }
        if (show) {
            CHAlertDialog(msg = "天生我材必有用") {
                show = false
            }
        }
    }

}

// https://mp.weixin.qq.com/s/U8vYckZLX3xjxFJk100Xxw
// 除了跳过重组的好处， lambda 版本还有一点好处就是它可能会跳过布局的一半（在大小没有变化的情况下），因为我们知道 Compose 的布局阶段包含测量和摆放两个阶段，如果只修改了位置，尺寸大小没有变化，那么 Compose 可以完全可以只执行摆放阶段。因此在最好的情况下，它能为我们节省整个流程的一半的时间。😃️
// Composition---> Layout ---> Drawing
//                 /    \
//             measure  place
@Composable
inline fun DebugCompositionLayoutByMeasureAndPlaceDrawing() {
    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(300.dp)
//            .drawWithContent {
//                println("TwinsHomePage===>drawWithContent")
//            }.layout { measurable, constraints ->
//                val placeable = measurable.measure(constraints)
//                layout(placeable.width, placeable.height) {
//                    println("TwinsHomePage===>layout")
//                    placeable.placeRelative(0, 0)
//                }
//            }
//            .xplRoundRectangleOld { xplRandomColor() } // 颜色会变化
            .xplRoundRectangleNew ( xplRandomColor() ) // 颜色会变化

    ) {
        Debug("TwinsHomePage-Box")
        ListItemAnimationComponent()
    }
}


// Draw round rectangle with a custom color
fun Modifier.xplRoundRectangleOld(
    color: () -> Color,
): Modifier = composed {
    // when color changed，create and return new RoundRectanleModifier instance
    val modifier = remember(color) {
        RoundRectangleModifierXpl(color = color)
    }
    return@composed modifier.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints)
        layout(placeable.width, placeable.height) {
            println("TwinsHomePage===>layout")
            placeable.placeRelative(0, 0)
        }
    }
}

// implement DrawModifier
private class RoundRectangleModifierXpl(
    private val color: () -> Color,
) : DrawModifier {
    override fun ContentDrawScope.draw() {
        println("TwinsHomePage===>draw")
        drawRoundRect(
            color = color(),
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
class RoundRectangleNodeModifierNew(
    var color: Color,
) : DrawModifierNode, Modifier.Node() {
    override fun ContentDrawScope.draw() {
        drawRoundRect(
            color = color,
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
        )
    }
}

//@OptIn(ExperimentalComposeUiApi::class)
//fun Modifier.roundRectangle(
//    color: Color
//) = this then modifierElementOf(
//    params = color,
//    create = { RoundRectangleNodeModifierNew(color) },
//    update = { currentNode -> currentNode.color = color },
//    definitions =)
//)


fun Modifier.xplRoundRectangleNew(
    color: Color,
) = this . xplRoundRectangle(onDraw = {
    drawWithContent {
        println("TwinsHomePage===>draw")
        drawRoundRect(
            color = color,
            cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
        )
        drawContent()
    }
})

fun Modifier.xplRoundRectangle(
    onDraw: DrawScope.() -> Unit,
) = this then RoundRectangleElement(onDraw)

@OptIn(ExperimentalComposeUiApi::class)
private data class RoundRectangleElement(
    val onDraw: DrawScope.() -> Unit,
) : ModifierNodeElement<RoundRectangleModifier>() {
    override fun create() = RoundRectangleModifier(onDraw)

    override fun update(node: RoundRectangleModifier) = node.apply {
        println("TwinsHomePage===>update")
        onDraw = this@RoundRectangleElement.onDraw
    }

    override fun InspectorInfo.inspectableProperties() {
        name = "xplRoundRectangle"
        properties["onDraw"] = onDraw
    }
}

@OptIn(ExperimentalComposeUiApi::class)
private class RoundRectangleModifier(
    var onDraw: DrawScope.() -> Unit,
) : Modifier.Node(), DrawModifierNode {

    override fun ContentDrawScope.draw() {
        println("TwinsHomePage===>draw")
        onDraw()
        drawContent()
    }
}