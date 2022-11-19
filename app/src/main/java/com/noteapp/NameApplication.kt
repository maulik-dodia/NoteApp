package com.noteapp

import android.app.Application
import com.noteapp.db.NameRoomDatabase
import com.noteapp.repository.NameRepository

class NameApplication : Application() {
    private val database by lazy { NameRoomDatabase.getDatabase(this) }
    val repository by lazy { NameRepository(database.nameDao()) }
}