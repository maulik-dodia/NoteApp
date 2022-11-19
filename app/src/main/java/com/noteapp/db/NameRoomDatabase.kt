package com.noteapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.noteapp.dao.NameDao
import com.noteapp.model.Name

@Database(entities = [Name::class], version = 1, exportSchema = false)
abstract class NameRoomDatabase : RoomDatabase() {

    abstract fun nameDao(): NameDao

    companion object {

        @Volatile
        private var INSTANCE: NameRoomDatabase? = null

        fun getDatabase(context: Context): NameRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NameRoomDatabase::class.java,
                    "name_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}