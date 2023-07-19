package com.yunext.twins.ui.page.adddevice
import com.yunext.twins.base.Start
import com.yunext.twins.base.UiState
import com.yunext.twins.data.DeviceAndState
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object AddDeviceController :
    CoroutineScope by CoroutineScope( SupervisorJob() + CoroutineName("DeviceController")) {
    private val mutableCurDeviceAndStateFlow: MutableStateFlow<DeviceAndState?> =
        MutableStateFlow(null)

    val curDeviceAndStateFlow = mutableCurDeviceAndStateFlow.asStateFlow()

    private val mutableUiState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(Start(Unit))
    val uiState = mutableUiState.asStateFlow()

    fun prepareDeviceDetail(deviceAndState: DeviceAndState){
        mutableCurDeviceAndStateFlow.value = deviceAndState
    }
}
