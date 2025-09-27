package com.noteapp.data.repository

import com.noteapp.domain.model.Note
import com.noteapp.util.NoteConstant.LONG_FIFTY
import kotlinx.coroutines.flow.Flow

interface FirestoreDBRepository {

    suspend fun getAllNotes(userId: String? = null, limit: Long = LONG_FIFTY): Flow<List<Note>>

    suspend fun searchNoteByTitle(userId: String? = null, query: String, limit: Long = LONG_FIFTY): Flow<List<Note>>

    suspend fun insertNote(note: Note): String

    suspend fun getNoteById(id: String): Note?

    suspend fun updateNote(note: Note)

    suspend fun deleteNoteById(id: String)

    suspend fun deleteAllNotes()
}