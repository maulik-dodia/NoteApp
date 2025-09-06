package com.noteapp.preview

import com.noteapp.data.local.NoteDao
import com.noteapp.data.local.NoteEntity

class FakeNoteDao : NoteDao {

    override suspend fun getAllNotes(): List<NoteEntity> = listOf(
        NoteEntity(id = 1, title = "First Note", description = "This is the first note."),
        NoteEntity(id = 2, title = "Second Note", description = "This is the second note."),
        NoteEntity(id = 3, title = "Third Note", description = "This is the third note.")
    )

    override suspend fun insertNote(note: NoteEntity) { }
}