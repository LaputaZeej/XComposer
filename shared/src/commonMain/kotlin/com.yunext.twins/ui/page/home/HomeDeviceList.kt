package com.yunext.twins.ui.page.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.yunext.twins.data.DeviceAndState
import com.yunext.twins.data.ItemDefaults
import com.yunext.twins.ui.compoents.TwinsDeviceStatus
import com.yunext.twins.ui.compoents.TwinsLabelText

@Composable
expect fun TwinsDeviceList(
    modifier: Modifier,
    list: List<DeviceAndState>,
    onDeviceSelected: (DeviceAndState) -> Unit,
)

@Composable
expect fun TwinsDeviceItem(modifier: Modifier, device: DeviceAndState, onClick: () -> Unit)

@Composable
internal fun TwinsDeviceItemCommon(
    modifier: Modifier,
    device: DeviceAndState,
    onClick: () -> Unit,
) {

    Text("desktop - DeviceItem", modifier = modifier.padding(16.dp).clickable {
        onClick()
    })

    Column(modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .clip(ItemDefaults.itemShape)
        .background(Color.White)
        .clickable {
            onClick()
        }
        .padding(16.dp)) {

        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = device.name,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                fontWeight = FontWeight.Bold,
            )
            Spacer(modifier = Modifier.width(4.dp))
            TwinsDeviceStatus(deviceStatus = device.status)

        }

        Spacer(modifier = Modifier.height(16.dp))

        DeviceCommunicationIdAndModel(Modifier,device.communicationId,device.model)
    }
}

@Composable
internal fun DeviceCommunicationIdAndModel(
    modifier: Modifier,
    communicationId: String,
    model: String,
) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        TwinsLabelText(text = "通信ID")
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = communicationId,
            maxLines = 1,
            fontSize = 11.sp,
            color = Color(0xff666666),
            overflow = TextOverflow.Ellipsis
        )
    }

    Spacer(modifier = Modifier.height(8.dp))
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        TwinsLabelText(text = "设备型号")
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = model,
            maxLines = 1,
            fontSize = 11.sp,
            color = Color(0xff666666),
            overflow = TextOverflow.Ellipsis
        )
    }
}