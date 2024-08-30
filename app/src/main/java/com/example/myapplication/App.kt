package com.example.myapplication

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.example.myapplication.di.AppComponent
import com.example.myapplication.di.DaggerAppComponent

class App : Application(), Configuration.Provider {

    private lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .build()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .build()
}