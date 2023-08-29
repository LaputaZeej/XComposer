package com.yunext.twins

import android.app.Application

class TwinsApp :Application(){
    override fun onCreate() {
        super.onCreate()
        AndroidApp.init(this)
    }
}