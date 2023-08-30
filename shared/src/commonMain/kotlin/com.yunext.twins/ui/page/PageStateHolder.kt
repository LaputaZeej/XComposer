package com.yunext.twins.ui.page

import com.yunext.twins.base.AbstractStateHolder
import com.yunext.twins.base.Effect
import com.yunext.twins.ui.AppDestination
import com.yunext.twins.ui.HomeDestination
import com.yunext.twins.ui.back
import com.yunext.twins.ui.isEmpty

object PageStateHolder:AbstractStateHolder<AppDestination,Effect>(HomeDestination) {

    fun skip(appDestination: AppDestination){
        request {
            appDestination
        }
    }

    fun back(){
        state {
            this.back().let {
                if(it.isEmpty()) this else it
            }
        }
    }
}