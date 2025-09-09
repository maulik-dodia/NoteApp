package com.noteapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.noteapp.data.local.NoteDao
import com.noteapp.data.local.NoteDatabase
import com.noteapp.data.repository.NoteRepository
import com.noteapp.data.repository.NoteRepositoryImpl
import com.noteapp.presentation.viewmodel.NoteListViewModelFactory
import com.noteapp.util.NoteConstant.NOTE_DB
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(private val application: Application) {

    // Other providers
    @Provides
    fun provideContext(): Context {
        return application
    }

    // Database providers
    @Provides
    @Singleton
    fun provideNoteDatabase(context: Context): NoteDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            klass = NoteDatabase::class.java,
            name = NOTE_DB
        ).build()
    }

    @Provides
    fun provideNoteDao(db: NoteDatabase): NoteDao {
        return db.noteDao()
    }

    // Repository providers
    @Provides
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(noteDao)
    }

    // ViewModel providers
    @Provides
    fun provideNoteListViewModelFactory(repository: NoteRepository): NoteListViewModelFactory {
        return NoteListViewModelFactory(repository)
    }
}