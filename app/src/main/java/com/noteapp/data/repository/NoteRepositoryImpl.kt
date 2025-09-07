package com.noteapp.data.repository

import com.noteapp.data.local.NoteDao
import com.noteapp.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl(private val noteDao: NoteDao): NoteRepository {

    override suspend fun getAllNotes(): Flow<List<NoteEntity>> {
        return noteDao.getAllNotes()
    }

    override suspend fun insertNote(note: NoteEntity) {
        noteDao.insertNote(note)
    }

    override suspend fun getNoteById(id: Int): Flow<NoteEntity?> {
        return noteDao.getNoteById(id)
    }

    override suspend fun updateNote(note: NoteEntity) {
        noteDao.updateNote(note)
    }

    override suspend fun deleteAllNotes() {
        noteDao.deleteAllNotes()
    }
}