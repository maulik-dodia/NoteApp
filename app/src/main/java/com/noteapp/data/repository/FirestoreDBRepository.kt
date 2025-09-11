package com.noteapp.data.repository

import com.noteapp.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface FirestoreDBRepository {

    suspend fun observeAllNotes(): Flow<List<Note>>

    suspend fun insertNote(note: Note): String

    suspend fun getNoteById(id: String): Note?

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(id: String)

    suspend fun deleteAllNotes()
}