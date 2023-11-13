package com.yunext.twins.ui.page.home

import com.yunext.twins.base.Effect
import com.yunext.twins.base.mvi.MVIAction
import com.yunext.twins.base.mvi.MVIMiddleware
import com.yunext.twins.base.mvi.MVIReducer
import com.yunext.twins.base.mvi.MVIState
import com.yunext.twins.base.mvi.MVIStore
import com.yunext.twins.data.DeviceAndState
import com.yunext.twins.data.DeviceStatus
import com.yunext.twins.data.DeviceType
import com.yunext.twins.module.mqtt.core.mqttClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.jvm.JvmStatic

data class HomeState(
    val list: List<DeviceAndState> = emptyList(),
    val effect: Effect = Effect.Idle,
) : MVIState {

}

sealed interface HomeAction : MVIAction {

    sealed interface Sync : HomeAction {
        data class ListDeviceEffect(val effect: Effect) : Sync
        data class DeviceList(val list: List<DeviceAndState>) : Sync
        data class AddDeviceEffect(val effect: Effect) : Sync
    }

    sealed interface Async : HomeAction {
        object StartListDevice : Async {
            override val type: MVIAction.Type
                get() = MVIAction.Type.Async

        }

        data class StartAddDevice(
            val deviceName: String,
            val communicationId: String,
            val deviceType: DeviceType,
            val deviceModel: String,
        ) : Async {
            override val type: MVIAction.Type
                get() = MVIAction.Type.Async
        }
    }
}

class HomeReducer : MVIReducer<HomeState, HomeAction> {
    override fun reduce(state: HomeState, action: HomeAction): HomeState? {
        return if (action is HomeAction.Sync) {
            when (action) {
                is HomeAction.Sync.AddDeviceEffect -> state.copy(effect = action.effect)
                is HomeAction.Sync.DeviceList -> state.copy(list = action.list)
                is HomeAction.Sync.ListDeviceEffect -> state.copy(effect = action.effect)
            }
        } else null

    }
}

class HomeMiddleware : MVIMiddleware<HomeState, HomeAction>() {
    override fun process(
        state: HomeState,
        action: HomeAction,
        coroutineScope: CoroutineScope,
    ) {
        if (action is HomeAction.Async) {
            when (action) {
                is HomeAction.Async.StartAddDevice -> {
                    coroutineScope.launch {
                        try {
                            dispatch(HomeAction.Sync.AddDeviceEffect(Effect.Processing))
                            val newDevice = addDevice(
                                deviceName = action.deviceName,
                                communicationId = action.communicationId,
                                deviceType = action.deviceType,
                                deviceModel = action.deviceModel
                            )
                            dispatch(HomeAction.Sync.DeviceList(state.list + newDevice))
                            dispatch(HomeAction.Sync.AddDeviceEffect(Effect.Success))
                        } catch (e: Throwable) {
                            dispatch(HomeAction.Sync.AddDeviceEffect(Effect.Fail(e)))
                        } finally {
                            dispatch(HomeAction.Sync.AddDeviceEffect(Effect.Idle))
                        }
                    }
                }

                HomeAction.Async.StartListDevice -> {
                    coroutineScope.launch {
                        try {
                            dispatch(HomeAction.Sync.ListDeviceEffect(Effect.Processing))
                            val list = listDevice()
                            dispatch(HomeAction.Sync.DeviceList(list))
                            dispatch(HomeAction.Sync.ListDeviceEffect(Effect.Success))
                        } catch (e: Throwable) {
                            dispatch(HomeAction.Sync.ListDeviceEffect(Effect.Fail(e)))
                        } finally {
                            dispatch(HomeAction.Sync.ListDeviceEffect(Effect.Idle))
                        }
                    }
                }
            }
        }
    }

    private suspend fun listDevice(): List<DeviceAndState> {
        return coroutineScope {
            delay(2000)
            val list = com.yunext.twins.data.DeviceAndState.randomList() +
                    com.yunext.twins.data.DeviceAndState.DEBUG_LIST//deviceRepository.loadDevice()
            list
        }
    }

    private suspend fun addDevice(
        deviceName: String,
        communicationId: String,
        deviceType: DeviceType,
        deviceModel: String,
    ): DeviceAndState {
        return coroutineScope {
            delay(2000)
            DeviceAndState(
                deviceName,
                communicationId = communicationId + deviceType,
                model = deviceModel,
                status = DeviceStatus.random()
            )
        }
    }

}

class HomeStore(middleware: HomeMiddleware = HomeMiddleware()) : MVIStore<HomeState, HomeAction>(
    reducer = HomeReducer(),
    middlewares = listOf(middleware),
    initialState = HomeState()
) {

    fun onDeviceList() {
        dispatch(HomeAction.Async.StartListDevice)
        testMqtt()
    }

    fun onDeviceAdd(
        deviceName: String,
        communicationId: String,
        deviceType: DeviceType,
        deviceModel: String,
    ) {
        dispatch(
            HomeAction.Async.StartAddDevice(
                deviceName = deviceName,
                communicationId = communicationId,
                deviceType = deviceType,
                deviceModel = deviceModel
            )
        )

    }

    fun testMqtt(){
        mqttClient.init()
    }

    companion object {
        @JvmStatic
        val INSTANCE = HomeStore()
    }
}