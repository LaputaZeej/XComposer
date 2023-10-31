package com.yunext.twins.ui.page.home

import androidx.compose.runtime.State
import com.yunext.twins.base.AbstractStateHolder
import com.yunext.twins.base.UiState
import com.yunext.twins.data.DeviceAndState
import com.yunext.twins.data.DeviceAndState.Companion.randomList
import kotlinx.coroutines.delay

@Deprecated("delete")
expect fun rememberDeviceAndStateList(): State<List<DeviceAndState>>

@Deprecated("delete", ReplaceWith("HomeStore"))
object HomeStateHolder :
    AbstractStateHolder<List<DeviceAndState>, UiState<Unit, List<DeviceAndState>>>(listOf()) {
    /**
     * 设备列表
     */
    fun listDevice() {
        request {
            delay(2000)
            val list = randomList() +
                    DeviceAndState.DEBUG_LIST//deviceRepository.loadDevice()
            list
        }
    }

    /**
     * 添加设备
     */
    fun addDevice(deviceAndState: DeviceAndState) {
        state {
            listOf(deviceAndState) + this
        }
    }
}
