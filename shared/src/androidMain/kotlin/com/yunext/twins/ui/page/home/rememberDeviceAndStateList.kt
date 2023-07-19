package com.yunext.twins.ui.page.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yunext.twins.data.DeviceAndState
import com.yunext.twins.ui.MainViewModel

@Composable
actual fun rememberDeviceAndStateList(): State<List<DeviceAndState>> {
    val viewModel: MainViewModel = viewModel()
   return viewModel.deviceAndStateListFlow.collectAsState()
}