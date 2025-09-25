package com.noteapp.util

object NoteConstant {

    // Firebase Database
    const val NOTES = "notes"

    // Room Database
    const val NOTE_DB = "note_db"
    const val TABLE_NOTE = "note"

    // Navigation
    const val NOTE_LIST = "note_list"
    const val ADD_EDIT_NOTE = "add_edit_note"

    // Numbers
    // Positive
    const val ZERO = 0
    const val ONE = 1
    const val TWO = 2
    const val THREE = 3
    const val EIGHT = 8
    const val ONE_THOUSAND = 1000
    // Negative
    const val MINUS_TWO = -2
    // Float
    const val FLOAT_ONE = 1f
    const val FLOAT_POINT_TWO = 0.2f
    const val FLOAT_POINT_SIX = 0.6f
    // Long
    const val LONG_FIFTY = 50L
    const val LONG_FOUR_HUNDRED = 400L

    // Rest
    const val TITLE = "title"
    const val EMPTY_STRING = ""
    const val NOTE_ID = "noteId"
    const val USER_ID = "userId"
    const val NOTE_ACTION = "note_action"
    const val DD_MMM_YY_HHMM_A = "dd-MMM-yy hh:mm a"

    // Preview composable
    const val PREVIEW_NOTE_TITLE = "Sample Note Title"
    const val PREVIEW_NOTE_DESC = "This is a sample note description used for previewing"

    // TODO Need to move to strings.xml later
    const val SAVE_NOTE_ERROR_2 = "Please try again."
    const val NOTE_NOT_FOUND_ERROR = "Note not found."
    const val GENERIC_ERROR = "Something went wrong."
    const val SAVE_NOTE_ERROR_1 = "Failed to save note"
    const val ABORT_ERROR = "Write conflicted. Please retry."
    const val NOTE_DELETED_MSG = "Note deleted successfully."
    const val RESOURCE_NOT_FOUND_ERROR = "Resource not found."
    const val ALL_NOTES_DELETED_MSG = "All notes deleted successfully."
    const val REQUEST_TIMEOUT_ERROR = "Request timed out. Please retry."
    const val NOTE_ALREADY_EXISTS_ERROR = "A note with this ID already exists."
    const val NETWORK_ERROR = "Network unavailable. Check connection and try again."
    const val PERMISSION_ERROR = "Permission denied. Please sign in or check Firestore rules..........!!!!"
}