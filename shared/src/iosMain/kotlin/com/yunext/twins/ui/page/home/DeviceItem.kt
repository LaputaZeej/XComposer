package com.yunext.twins.ui.page.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yunext.twins.data.DeviceAndState
import com.yunext.twins.ui.compoents.CHItemShadowShape
import com.yunext.twins.ui.compoents.Debug

@Composable
actual fun TwinsDeviceItem(modifier: Modifier , device:DeviceAndState,onClick:()->Unit) {
    TwinsDeviceItemCommon(modifier,device,onClick)
}

@Composable
actual fun TwinsDeviceList(modifier: Modifier,list:List<DeviceAndState>,onDeviceSelected:(DeviceAndState)->Unit){
    Debug("TwinsHomePage-内容-设备列表-IOS")
    LazyColumn(
        modifier = Modifier.padding(0.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(items = list, key = { it.communicationId }) { device ->
            CHItemShadowShape {
                TwinsDeviceItem(modifier = Modifier.fillMaxWidth(), device = device) {
                    onDeviceSelected.invoke(device)
                }
            }
        }
    }
}