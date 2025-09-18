package com.noteapp.preview

import com.noteapp.data.local.NoteDao
import com.noteapp.data.local.NoteEntity
import com.noteapp.util.NoteConstant.ONE
import com.noteapp.util.NoteConstant.PREVIEW_NOTE_DESC
import com.noteapp.util.NoteConstant.PREVIEW_NOTE_TITLE
import com.noteapp.util.NoteConstant.THREE
import com.noteapp.util.NoteConstant.TWO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakeNoteDao : NoteDao {

    override fun getAllNotes(): Flow<List<NoteEntity>> = flow { listOf(
            NoteEntity(id = ONE, title = PREVIEW_NOTE_TITLE, description = PREVIEW_NOTE_DESC),
            NoteEntity(id = TWO, title = PREVIEW_NOTE_TITLE, description = PREVIEW_NOTE_DESC),
            NoteEntity(id = THREE, title = PREVIEW_NOTE_TITLE, description = PREVIEW_NOTE_DESC)
        )
    }

    override suspend fun insertNote(note: NoteEntity) {
        // Do not require actual body, added this comment to prevent detekt's EmptyFunctionBlock error
    }

    override fun getNoteById(id: Int): Flow<NoteEntity?> = flow {
        NoteEntity(id = id, title = PREVIEW_NOTE_TITLE, description = PREVIEW_NOTE_DESC)
    }

    override suspend fun updateNote(note: NoteEntity) {
        // Do not require actual body, added this comment to prevent detekt's EmptyFunctionBlock error
    }

    override suspend fun deleteAllNotes() {
        // Do not require actual body, added this comment to prevent detekt's EmptyFunctionBlock error
    }

    override suspend fun deleteNote(note: NoteEntity) {
        // Do not require actual body, added this comment to prevent detekt's EmptyFunctionBlock error
    }
}