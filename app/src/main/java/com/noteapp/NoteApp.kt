package com.noteapp

import android.app.Application
import androidx.room.Room
import com.noteapp.data.local.NoteDatabase

class NoteApp: Application() {

    lateinit var database: NoteDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "notes_db"
        ).build()
    }
}