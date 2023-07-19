package com.yunext.twins.ui.page.home

import androidx.compose.runtime.State
import com.yunext.twins.base.End
import com.yunext.twins.base.Processing
import com.yunext.twins.base.Start
import com.yunext.twins.base.UiState
import com.yunext.twins.data.DeviceAndState
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

expect fun rememberDeviceAndStateList(): State<List<DeviceAndState>>

object AppController :
    CoroutineScope by CoroutineScope(SupervisorJob() + CoroutineName("AppController")) {
    private val mutableDeviceAndStateListFlow: MutableStateFlow<List<DeviceAndState>> =
        MutableStateFlow(listOf())

    val deviceAndStateListFlow = mutableDeviceAndStateListFlow.asStateFlow()

    private val mutableUiState: MutableStateFlow<UiState<Unit>> = MutableStateFlow(Start(Unit))
    val uiState = mutableUiState.asStateFlow()

    fun loadDevice() {
        launch {
            try {
                mutableUiState.update {
                    Processing(Unit)
                }
                delay(2000)
                mutableDeviceAndStateListFlow.value =
                    DeviceAndState.DEBUG_LIST//deviceRepository.loadDevice()
                mutableUiState.update {
                    End.Success(Unit, mutableDeviceAndStateListFlow.value)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                mutableUiState.update {
                    End.Fail(Unit, e)
                }
            } finally {
                mutableUiState.update {
                    Start(Unit)
                }
            }
        }
    }

    fun addDevice(deviceAndState: DeviceAndState) {
        mutableDeviceAndStateListFlow.update { list ->
            listOf(deviceAndState) + list
        }
    }
}
