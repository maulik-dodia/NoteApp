package com.noteapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.noteapp.data.local.NoteDao
import com.noteapp.data.local.NoteDatabase
import com.noteapp.data.repository.FirestoreDBRepository
import com.noteapp.data.repository.FirestoreDBRepositoryImpl
import com.noteapp.data.repository.RoomDBRepository
import com.noteapp.data.repository.RoomDBRepositoryImpl
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

    // Firestore database providers
    @Provides
    fun provideFirestoreDatabase(context: Context): FirebaseFirestore {
        FirebaseApp.initializeApp(context)
        return FirebaseFirestore.getInstance()
    }

    // Room Database providers
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
    fun provideFirestoreRepository(firestore: FirebaseFirestore): FirestoreDBRepository {
        return FirestoreDBRepositoryImpl(firestore = firestore)
    }

    @Provides
    fun provideNoteRepository(noteDao: NoteDao): RoomDBRepository {
        return RoomDBRepositoryImpl(noteDao = noteDao)
    }

    // ViewModel providers
    @Provides
    fun provideNoteListViewModelFactory(roomRepository: RoomDBRepository,
                                        firestoreRepository: FirestoreDBRepository): NoteListViewModelFactory {
        return NoteListViewModelFactory(roomRepository = roomRepository, firestoreRepository = firestoreRepository)
    }
}