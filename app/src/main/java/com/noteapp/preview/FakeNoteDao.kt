package com.noteapp.preview

import com.noteapp.data.local.NoteDao
import com.noteapp.data.local.NoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNoteDao : NoteDao {

    override fun getAllNotes(): Flow<List<NoteEntity>> = flow { listOf(
            NoteEntity(id = 1, title = "First Note", description = "This is the first note."),
            NoteEntity(id = 2, title = "Second Note", description = "This is the second note."),
            NoteEntity(id = 3, title = "Third Note", description = "This is the third note.")
        )
    }

    override suspend fun insertNote(note: NoteEntity) {}

    override fun getNoteById(id: Int): Flow<NoteEntity?> = flow {
        NoteEntity(id = id, title = "Sample Note", description = "This is a sample note.")
    }

    override suspend fun updateNote(note: NoteEntity) {}
}