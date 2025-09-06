package com.noteapp.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd-MMM-yy hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}