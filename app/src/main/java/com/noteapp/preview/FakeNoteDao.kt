package com.noteapp.preview

import com.noteapp.data.local.NoteDao
import com.noteapp.data.local.NoteEntity

class FakeNoteDao : NoteDao {

    override suspend fun getAllNotes(): List<NoteEntity> = emptyList()

    override suspend fun insertNote(note: NoteEntity) { }
}