package com.noteapp.data.repository

import com.noteapp.data.local.NoteDao
import com.noteapp.data.local.NoteEntity

class NoteRepositoryImpl(private val noteDao: NoteDao): NoteRepository {

    override suspend fun getAllNotes(): List<NoteEntity> {
        return noteDao.getAllNotes()
    }

    override suspend fun insertNote(note: NoteEntity) {
        noteDao.insertNote(note)
    }
}