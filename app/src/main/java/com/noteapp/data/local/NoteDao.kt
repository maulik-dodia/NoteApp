package com.noteapp.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.noteapp.util.NoteConstant.TABLE_NOTE
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {

    @Query(value = "SELECT * FROM $TABLE_NOTE ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<NoteEntity>>

    @Insert
    suspend fun insertNote(note: NoteEntity)

    @Query(value = "SELECT * FROM $TABLE_NOTE WHERE id = :id")
    fun getNoteById(id: Int): Flow<NoteEntity?>

    @Update
    suspend fun updateNote(note: NoteEntity)

    @Query(value = "DELETE FROM $TABLE_NOTE")
    suspend fun deleteAllNotes()

    @Delete
    suspend fun deleteNote(note: NoteEntity)
}