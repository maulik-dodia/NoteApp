package com.noteapp.presentation.ui.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.noteapp.util.NoteConstant.NO
import com.noteapp.util.NoteConstant.YES

@Composable
fun ConfirmationDialog(
    title: String,
    message: String,
    onConfirm:() -> Unit,
    onDismiss:() -> Unit,
    confirmText: String = YES,
    dismissText: String = NO
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = { Text(text = message) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(confirmText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(dismissText)
            }
        }
    )
}