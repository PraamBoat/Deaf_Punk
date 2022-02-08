package com.example.fumolizer

import android.app.Application
import android.content.Context

class ContextClass: Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: ContextClass? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
        val context: Context = ContextClass.applicationContext()
    }

}