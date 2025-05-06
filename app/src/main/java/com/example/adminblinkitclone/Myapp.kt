package com.example.adminblinkitclone

import android.app.Application

class Myapp : Application() {

    override fun onCreate() {
        super.onCreate()
        utils.appContext = applicationContext
    }
}