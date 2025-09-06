package com.noteapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.noteapp.util.formatTimestamp

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    val timestamp: Long = System.currentTimeMillis(),
)

// Extension property for formatted timestamp
val NoteEntity.formattedTimestamp: String
    get() = formatTimestamp(timestamp = this.timestamp)