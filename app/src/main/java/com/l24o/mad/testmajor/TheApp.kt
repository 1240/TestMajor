package com.l24o.mad.testmajor

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class TheApp : Application() {


    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
            private set
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

}