package com.yunext.twins.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunext.twins.base.End
import com.yunext.twins.base.Processing
import com.yunext.twins.base.Start
import com.yunext.twins.base.UiState

import com.yunext.twins.data.DeviceAndState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel  constructor(
) : ViewModel() {
    private val _deviceAndStateListFlow: MutableStateFlow<List<DeviceAndState>> =
        MutableStateFlow(listOf())

    init {
//        D.i("deviceRepository   ${deviceRepository.hashCode()}")
//        D.i("tslRepository      ${tslRepository.hashCode()}")
    }


    val deviceAndStateListFlow = _deviceAndStateListFlow.asStateFlow()

    private val _curDeviceAndStateFlow: MutableStateFlow<DeviceAndState?> =
        MutableStateFlow(null)

    val curDeviceAndStateFlow = _curDeviceAndStateFlow.asStateFlow()

    private val _uiState:MutableStateFlow<UiState<Unit>> = MutableStateFlow(Start(Unit))
    val uiState = _uiState.asStateFlow()

    var a: Int = 10

    fun loadDeviceData() {
        Log.i("MainViewModel", "loadData")
        viewModelScope.launch {
            try {
                _uiState.update {
                    Processing(Unit)
                }
//                _deviceAndStateListFlow.value = deviceRepository.loadDevice()
                _uiState.update {
                    End.Success(Unit,_deviceAndStateListFlow.value)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    End.Fail(Unit,e)
                }
            } finally {
                _uiState.update {
                    Start(Unit)
                }
            }
        }
    }


    fun prepareDeviceDetail(deviceAndState: DeviceAndState){
        _curDeviceAndStateFlow.value = deviceAndState
    }

    fun connectAndRefresh(){
        Log.i("MainViewModel", "loadDeviceDetail")
        viewModelScope.launch {
            try {
//                _curDeviceAndStateFlow.value = deviceRepository.findDevice(curDeviceAndStateFlow.value?:throw IllegalStateException("device为空"))
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
            }
        }
    }

    fun updateTsl() {

    }
}