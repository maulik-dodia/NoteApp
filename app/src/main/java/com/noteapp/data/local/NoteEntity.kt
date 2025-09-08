package com.noteapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.noteapp.util.NoteConstant.TABLE_NOTE
import com.noteapp.util.NoteConstant.ZERO
import com.noteapp.util.formatTimestamp

@Entity(tableName = TABLE_NOTE)
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = ZERO,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
)

// Extension for formatted timestamp
val NoteEntity.formattedTimestamp: String
    get() = formatTimestamp(timestamp = this.timestamp)