package com.noteapp.data.repository

import com.noteapp.data.local.NoteEntity

interface NoteRepository {

    suspend fun getAllNotes(): List<NoteEntity>

    suspend fun insertNote(note: NoteEntity)
}