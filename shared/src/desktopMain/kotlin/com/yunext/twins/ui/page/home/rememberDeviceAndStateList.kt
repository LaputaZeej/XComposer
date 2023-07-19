package com.yunext.twins.ui.page.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.yunext.twins.data.DeviceAndState

@Composable
actual fun rememberDeviceAndStateList(): State<List<DeviceAndState>> {
   return remember {
       mutableStateOf(DeviceAndState.DEBUG_LIST)
   }
}