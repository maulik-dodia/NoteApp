package com.noteapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.noteapp.util.NoteConstant.ONE

@Database(
    entities = [NoteEntity::class],
    version = ONE,
    exportSchema = false)
abstract class NoteDatabase: RoomDatabase() {

    abstract fun noteDao(): NoteDao
}