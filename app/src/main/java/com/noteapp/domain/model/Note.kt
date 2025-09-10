package com.noteapp.domain.model

import com.noteapp.util.NoteConstant.EMPTY_STRING
import com.noteapp.util.formatTimestamp

data class Note(
    var id: String = EMPTY_STRING,
    var title: String = EMPTY_STRING,
    var description: String = EMPTY_STRING,
    var timestamp: Long = System.currentTimeMillis()
)

// Extension for formatted timestamp
val Note.formattedTimestamp: String
    get() = formatTimestamp(timestamp = this.timestamp)