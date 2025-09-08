package com.noteapp

import android.app.Application
import androidx.room.Room
import com.noteapp.data.local.NoteDatabase
import com.noteapp.util.NoteConstant.NOTE_DB

class NoteApp: Application() {

    lateinit var database: NoteDatabase
        private set

    override fun onCreate() {
        super.onCreate()

        database = Room.databaseBuilder(
            applicationContext,
            klass = NoteDatabase::class.java,
            name = NOTE_DB
        ).build()
    }
}