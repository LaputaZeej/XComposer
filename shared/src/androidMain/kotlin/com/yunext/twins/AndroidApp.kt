package com.yunext.twins

import android.app.Application

object AndroidApp {

    lateinit var application: Application
        private set

    fun init(application: Application) {
        this.application = application
    }

}