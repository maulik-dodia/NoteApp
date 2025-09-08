package com.noteapp.util

import com.noteapp.util.NoteConstant.DD_MMM_YY_HHMM_A
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat(DD_MMM_YY_HHMM_A, Locale.getDefault())
    return sdf.format(Date(timestamp))
}