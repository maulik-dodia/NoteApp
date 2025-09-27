package com.noteapp.domain.model

import com.google.firebase.firestore.PropertyName
import com.noteapp.util.NoteConstant.EMPTY_STRING
import com.noteapp.util.NoteConstant.TITLE_LOWER
import com.noteapp.util.formatTimestamp

data class Note(
    var id: String = EMPTY_STRING,
    var title: String = EMPTY_STRING,

    @get:PropertyName(value = TITLE_LOWER)
    @set:PropertyName(value = TITLE_LOWER)
    var titleLower: String = EMPTY_STRING, // To be used for searching

    var description: String = EMPTY_STRING,
    var timestamp: Long = System.currentTimeMillis()
)

// Extension for formatted timestamp
val Note.formattedTimestamp: String
    get() = formatTimestamp(timestamp = this.timestamp)