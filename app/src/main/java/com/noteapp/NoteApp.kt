package com.noteapp

import android.app.Application
import com.noteapp.di.AppComponent
import com.noteapp.di.AppModule
import com.noteapp.di.DaggerAppComponent

class NoteApp: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .appModule(AppModule(application = this))
            .build()
    }
}