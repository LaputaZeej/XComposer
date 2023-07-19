package com.yunext.twins.ui.page.device

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yunext.twins.data.DeviceAndState
import com.yunext.twins.data.ItemDefaults
import com.yunext.twins.ui.compoents.TwinsBackgroundBlock
import com.yunext.twins.ui.compoents.TwinsEmptyView
import com.yunext.twins.ui.compoents.TwinsTitle
import com.yunext.twins.ui.compoents.CHItemShadowShape
import com.yunext.twins.ui.page.home.DeviceCommunicationIdAndModel
import com.yunext.twins.ui.theme.app_background_70
import com.yunext.twins.ui.theme.app_textColor_333333
import com.yunext.twins.ui.theme.app_textColor_666666
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

enum class DeviceTab(val text: String) {
    Properties("属性"),
    Events("事件"),
    Services("服务")
    ;
}

enum class MenuData(val icon: String, val text: String) {
    ConfigWiFi("icon_twins_distribution_network.png", "进入配网模式"),
    Setting("icon_twins_set_up.png", "配置"),
    Logger("icon_twins_log.png", "通信日志"),
    UpdateTsl("icon_twins_refresh.png", "检查更新物模型")
    ;
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun <T> TwinsDevicePage(
    device: DeviceAndState,
    list: List<T>,
    onTabSelected: (tab: DeviceTab) -> Unit,
    onMenuClick: (MenuData) -> Unit,
    onLeft: () -> Unit,
    onRight: () -> Unit,
) {

    var curTab by remember { mutableStateOf(DeviceTab.Properties) }
    var showMenu by remember { mutableStateOf(false) }
    var editingProperty: Any? by remember { mutableStateOf(null) }
    var addProperty by remember { mutableStateOf(false) }
    var showTips by remember {
        mutableStateOf("")
    }
    TwinsBackgroundBlock()

    Column(modifier = Modifier.fillMaxSize()) {
        // 标题
        TwinsTitle(modifier = Modifier
            .background(app_background_70),
            text = device.name,
            leftClick = {
                onLeft()
            },
            rightClick = {
                showMenu = !showMenu
            })

        // 内容
        Box(Modifier.fillMaxWidth()) {
            Column() {
                // top
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(app_background_70)
                        .padding(vertical = 0.dp, horizontal = 12.dp),
                ) {
                    Top(device = device)
                    Spacer(Modifier.height(12.dp))
                }

                // tab
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Tab(
                        modifier = Modifier.weight(1f, true)
                            .padding(16.dp), curTab
                    ) {
                        curTab = it
                        onTabSelected.invoke(it)
                    }
                    // 统计
                    CountInfo(
                        modifier = Modifier
                            .padding(horizontal = 16.dp), count = list.size
                    )
                }
                // list
                KVList(
                    curTab,
                    list,
                    onPropertyAction = {
                        if (editingProperty == null) {
                            editingProperty = it
                        }
                    },
                    onEventAction = {},
                    onServiceAction = {})
            }

            // pop
            if (showMenu) {
                MenuList(
                    modifier = Modifier
                        .padding(horizontal = 12.dp)
                        .align(Alignment.TopEnd), onDismiss = { showMenu = false }) {
                    onMenuClick(it)
                }
            }

            // 属性修改
            val temp = editingProperty
            if (temp != null) {
                Box(modifier = Modifier.align(Alignment.BottomCenter)) {
                    PropertyBottomSheet(temp, onClose = {
                        editingProperty = null
                    }, onCommitted = {
                        editingProperty = null
                        showTips = "选择了$it"
                    }, add = false to {
                        addProperty = true
                    })
                }
            }

            // 属性添加
            if (addProperty) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                    ) {
                        PropertyBottomSheet("", onClose = {
                            addProperty = false
                        }, onCommitted = {
                            addProperty = false
                            showTips = "添加了$it"
                        }, add = true to {

                        })
                    }
                }
            }
        }

    }

}


@Composable
private fun Top(
    device: DeviceAndState,
    modifier: Modifier = Modifier,
) {
    DeviceCommunicationIdAndModel(modifier, device.communicationId, device.model)

}

@Composable
private fun Tab(modifier: Modifier, tab: DeviceTab, onTabSelected: (tab: DeviceTab) -> Unit) {
    LazyRow(modifier = modifier) {
        val array = DeviceTab.values()
        items(items = array, key = {
            it.ordinal
        }) { item ->
            CHTabItem(
                tab == item,
                icon = Icons.Default.Home,
                title = (item.text),
                onClick = { onTabSelected.invoke(item) }
            )

        }
    }
}


@Composable
private fun CHTabItem(
    selected: Boolean,
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .width(56.dp)
            .height(36.dp)
            //.border(2.dp,Color.Red)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        //.padding(16.dp)

//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
    ) {
//        Icon(
//            imageVector = icon,
//            contentDescription = null
//        )
//        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = Color(0xff333333),
            fontSize = if (selected) 18.sp else 14.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.align(
                Alignment.Center
            )
        )
        Spacer(modifier = Modifier.height(16.dp))
        if (selected) {
            Box(
                modifier = Modifier
                    .width(12.dp)
                    .height(3.dp)
                    .background(color = Color(0xff339DFF))
                    .clip(RoundedCornerShape(3.dp))
                    .align(Alignment.BottomCenter)

            ) {

            }
        }

    }
}

@Composable
private fun CountInfo(modifier: Modifier = Modifier, count: Int) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "共计",
            color = app_textColor_666666,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light
        )
        Spacer(modifier = Modifier.width(5.dp))
        Text(
            text = "$count",
            color = app_textColor_333333,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}


@Composable
private fun <T> KVList(
    tab: DeviceTab, list: List<T>,
    onPropertyAction: (T) -> Unit,
    onServiceAction: (T) -> Unit,
    onEventAction: (T) -> Unit,
) {
    if (list.isEmpty()) {
        TwinsEmptyView()
    } else {
        Box(Modifier.padding(horizontal = 16.dp)) {
            when (tab) {
                DeviceTab.Properties -> {

                    ListTslProperty(list = list) {
                        onPropertyAction.invoke(it)
                    }
                }

                DeviceTab.Events -> {

                    ListTslEvent(list = list) {
                        onEventAction(it)
                    }
                }

                DeviceTab.Services -> {

                    ListTslService(list = list) {
                        onServiceAction(it)
                    }
                }
            }
        }
    }
}


@Composable
private fun MenuList(
    modifier: Modifier = Modifier,
    onDismiss: () -> kotlin.Unit,
    onMenuClick: (MenuData) -> Unit,
) {
    val list: Array<MenuData> by remember {
        mutableStateOf(MenuData.values())
    }
    XPopContainer(onDismiss = {
        onDismiss()
    }) {
        CHItemShadowShape(elevation = 16.dp, modifier = modifier) {
            Column(
                modifier = Modifier
//                .size(300.dp)
//                .wrapContentWidth()
                    .width(180.dp)
//                .widthIn(max = 300.dp, min = 100.dp)
//                .wrapContentHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(ItemDefaults.itemBackground)
//                .padding(16.dp)
            ) {
                list.forEach {
                    MenuItem(it) {
                        onMenuClick(it)
                    }
                }
            }
        }
    }

}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun MenuItem(menuData: MenuData, onClick: () -> Unit) {
    Row(modifier = Modifier
        .height(49.dp)
        .fillMaxWidth()
        .clickable {
            onClick()
        }
        .padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(menuData.icon),
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = (menuData.text),
            fontSize = 14.sp,
            color = app_textColor_333333
        )

    }
}

/**
 * todo 点击事件的问题
 */
@Composable
internal fun XPopContainer(
    contentAlignment: Alignment = Alignment.TopStart,
    dimAmount:Float=.5f,
    onDismiss: () -> Unit = {},
    content: @Composable BoxScope.() -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = dimAmount))
            .clickable(interactionSource = MutableInteractionSource(), indication = null) {
                onDismiss()
            }, contentAlignment = contentAlignment
    ) {
        content()
    }
}
