package com.noteapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Insert
    suspend fun insertNote(note: NoteEntity)

    @Query("SELECT * FROM notes WHERE id = :id")
    fun getNoteById(id: Int): Flow<NoteEntity?>

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Query("DELETE FROM notes")
    suspend fun deleteAllNotes()
}