package com.yunext.twins.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yunext.twins.base.mvi.Store
import kotlinx.coroutines.CoroutineScope

internal abstract class MVIViewModel: ViewModel(),Store {
    override val storeScope: CoroutineScope
        get() = viewModelScope

    override fun onCleared() {
        super.onCleared()
    }
}