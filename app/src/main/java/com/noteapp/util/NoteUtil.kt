package com.noteapp.util

import com.google.firebase.firestore.FirebaseFirestoreException
import com.noteapp.util.NoteConstant.ABORT_ERROR
import com.noteapp.util.NoteConstant.DD_MMM_YY_HHMM_A
import com.noteapp.util.NoteConstant.NETWORK_ERROR
import com.noteapp.util.NoteConstant.NOTE_ALREADY_EXISTS_ERROR
import com.noteapp.util.NoteConstant.PERMISSION_ERROR
import com.noteapp.util.NoteConstant.REQUEST_TIMEOUT_ERROR
import com.noteapp.util.NoteConstant.RESOURCE_NOT_FOUND_ERROR
import com.noteapp.util.NoteConstant.SAVE_NOTE_ERROR_1
import com.noteapp.util.NoteConstant.SAVE_NOTE_ERROR_2
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat(DD_MMM_YY_HHMM_A, Locale.getDefault())
    return sdf.format(Date(timestamp))
}

fun getFirestoreError(firestoreException: FirebaseFirestoreException): String {
    return when (firestoreException.code) {
        // Map Firestore codes to user-friendly messages
        FirebaseFirestoreException.Code.PERMISSION_DENIED -> PERMISSION_ERROR
        FirebaseFirestoreException.Code.UNAVAILABLE -> NETWORK_ERROR
        FirebaseFirestoreException.Code.NOT_FOUND -> RESOURCE_NOT_FOUND_ERROR
        FirebaseFirestoreException.Code.ABORTED -> ABORT_ERROR
        FirebaseFirestoreException.Code.ALREADY_EXISTS -> NOTE_ALREADY_EXISTS_ERROR
        FirebaseFirestoreException.Code.DEADLINE_EXCEEDED -> REQUEST_TIMEOUT_ERROR
        else -> SAVE_NOTE_ERROR_1 + " (${firestoreException.code}). " + SAVE_NOTE_ERROR_2
    }
}