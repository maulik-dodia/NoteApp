package com.noteapp.data.repository

import com.noteapp.data.local.NoteDao
import com.noteapp.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomDBRepositoryImpl @Inject constructor(private val noteDao: NoteDao): RoomDBRepository {

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

    override suspend fun deleteNote(note: NoteEntity) {
        noteDao.deleteNote(note)
    }
}