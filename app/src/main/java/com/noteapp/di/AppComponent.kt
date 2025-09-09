package com.noteapp.di

import com.noteapp.MainActivity
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun inject(activity: MainActivity)
}