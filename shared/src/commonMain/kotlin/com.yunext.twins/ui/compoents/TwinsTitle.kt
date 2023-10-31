package com.yunext.twins.ui.compoents

import LocalPaddingValues
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidthIn
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yunext.twins.data.DeviceStatus
import com.yunext.twins.ui.theme.app_textColor_333333
import com.yunext.twins.ui.theme.app_textColor_666666
import com.yunext.twins.ui.theme.app_textColor_999999
import com.yunext.twins.ui.theme.debugBorder
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

private object TwinsTitleDefaults {
    val height = 44.dp
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TwinsTitle(
    modifier: Modifier = Modifier,
    text: String,
    icon: Painter? = null,
    leftClick: () -> Unit = {},
    rightClick: (() -> Unit)? = null,
) {
    Row(
        modifier = modifier.fillMaxWidth().padding(LocalPaddingValues.current)
            .height(TwinsTitleDefaults.height),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // left
        Image(
            painter = painterResource("icon_twins_return_nor.png"),
            contentDescription = "back",
            Modifier
                .clip(CircleShape)
                .clickable {
                    leftClick.invoke()
                }
                .applyIcon()
        )

        //
        Row(modifier = Modifier.wrapContentSize().weight(1f)) {
            Text(
                text = text,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .requiredWidthIn(max = 200.dp),
//                    .width(200.dp),
                maxLines = 1
            )
            if (icon != null) {
                Spacer(modifier = Modifier.width(2.dp))
                Image(
                    painter = icon,//painterResource(id = icon),
                    modifier = Modifier.align(Alignment.CenterVertically),
                    contentDescription = "status"
                )
            }

        }

        //
        if (rightClick != null) {
            Image(
                painter = painterResource("icon_twins_more_nor.png"),//painterResource(id = R.mipmap.icon_twins_more_nor),
                contentDescription = "more",
                modifier = Modifier
                    .clip(CircleShape)
                    .clickable {
                        rightClick.invoke()
                    }
                    .applyIcon()
            )
        } else {
            Box(modifier = Modifier.applyIcon())
        }

    }


}

private fun Modifier.applyIcon() = this
    .size(TwinsTitleDefaults.height)
    .padding(10.dp)


@OptIn(ExperimentalResourceApi::class)
@Composable
fun TwinsEmptyView() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource("ic_app.png"),//painterResource(id = R.mipmap.ic_app),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "没有找到相关内容",
            fontSize = 14.sp,
            color = app_textColor_666666
        )
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TwinsEmptyViewForDevice() {
    Debug("TwinsHomePage-内容-设备列表-空")
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource("ic_app.png"),//painterResource(id = R.mipmap.ic_app),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material.Text(
            text = "你还没有添加设备",
            fontSize = 16.sp,
            color = app_textColor_333333
        )
        Spacer(modifier = Modifier.height(12.dp))
        androidx.compose.material.Text(
            text = "添加设备，实时查看设备信息",
            fontSize = 12.sp,
            color = app_textColor_999999
        )
    }
}


@OptIn(ExperimentalResourceApi::class)
@Composable
fun TwinsDeviceStatus(modifier: Modifier = Modifier, deviceStatus: DeviceStatus) {
    when (deviceStatus) {
        DeviceStatus.GPRSOffLine -> {
            Image(
                painter = painterResource("icon_twins_offline.png"),//id = R.mipmap.icon_twins_offline),
                contentDescription = "gprs_offline", modifier = modifier
            )
        }

        DeviceStatus.GPRSOnLine -> {
            Image(
                painter = painterResource("icon_twins_4g.png"),//id = R.mipmap.icon_twins_4g),
                contentDescription = "wifi", modifier = modifier
            )
        }

        DeviceStatus.WiFiOffLine -> {
            Image(
                painter = painterResource("icon_twins_offline.png"),//id = R.mipmap.icon_twins_offline),
                contentDescription = "wifi", modifier = modifier
            )
        }

        DeviceStatus.WiFiOnLine -> {
            Image(
                painter = painterResource("icon_twins_wifi.png"),//id = R.mipmap.icon_twins_wifi),
                contentDescription = "wifi", modifier = modifier
            )
        }
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun TwinsLabelText(modifier: Modifier = Modifier, text: String) {
    Box(modifier = modifier.widthIn(min = 60.dp, max = 60.dp)) {
        Image(
            painter = painterResource("icon_twins_label_bg.png"),//id = R.mipmap.icon_twins_label_bg),
            contentDescription = "bg",
            modifier = Modifier.matchParentSize(),
            contentScale = ContentScale.FillBounds
        )
        Text(
            text = text, fontSize = 11.sp, color = Color(0x66666666), modifier = Modifier
                .align(
                    Alignment.CenterStart
                )
                .padding(horizontal = 6.dp)
        )
    }
}