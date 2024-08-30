package com.example.myapplication.di

import com.example.myapplication.App
import com.example.myapplication.presentation.ui.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(app: App)
    fun inject(mainActivity: MainActivity)
}