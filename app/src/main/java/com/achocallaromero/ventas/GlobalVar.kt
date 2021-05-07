package com.achocallaromero.ventas

import android.app.Application

class GlobalVar: Application() {
    companion object{
        var token: String = ""
    }

    override fun onCreate() {
        super.onCreate()
    }
}