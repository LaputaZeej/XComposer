package com.yunext.twins.ui.page.device
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
import kotlin.random.Random

object DeviceStateHolder :
    CoroutineScope by CoroutineScope( SupervisorJob() + CoroutineName("DeviceController")) {
    private val mutableDeviceDetailFlow: MutableStateFlow<List<Int>> =
        MutableStateFlow(listOf())

    val deviceDetailFlow = mutableDeviceDetailFlow.asStateFlow()

    private val mutableUiState: MutableStateFlow<UiState<Unit,List<Int>>> = MutableStateFlow(UiState.Start(Unit))
    val uiState = mutableUiState.asStateFlow()
    fun detail(tab: DeviceTab){
        launch {
            try {
                mutableUiState.update {
                    UiState.Processing(Unit)
                }
                delay(2000)
                mutableDeviceDetailFlow.value = List(Random.nextInt(50)) { it }

                mutableUiState.update {
                    UiState.Success(Unit, mutableDeviceDetailFlow.value)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                mutableUiState.update {
                    UiState.Fail(Unit, e)
                }
            } finally {
                mutableUiState.update {
                    UiState.Start(Unit)
                }
            }
        }

    }

    fun prepareDeviceDetail(device: DeviceAndState) {

    }
}
