package com.noteapp.data.repository

import com.noteapp.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun getAllNotes(): Flow<List<NoteEntity>>

    suspend fun insertNote(note: NoteEntity)

    suspend fun getNoteById(id: Int): Flow<NoteEntity?>

    suspend fun updateNote(note: NoteEntity)
}