package com.noteapp.presentation.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.noteapp.R
import com.noteapp.data.local.NoteEntity
import com.noteapp.data.local.formattedTimestamp

@Composable
fun NoteItem(note: NoteEntity,
             onDeleteNoteClick:(note: NoteEntity) -> Unit,
             onNoteClick:(Int) -> Unit) {

    var showDeleteNoteDialog by remember { mutableStateOf(false) }

    if(showDeleteNoteDialog) {
        ConfirmationDialog(
            title = "Delete Note",
            message = "Are you sure you want to delete this note?",
            onConfirm = {
                onDeleteNoteClick(note)
                showDeleteNoteDialog = false
            },
            onDismiss = { showDeleteNoteDialog = false }
        )
    }

    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        onClick = {
            onNoteClick(note.id)
        }
    ) {
        Row(modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    style = MaterialTheme.typography.titleMedium,
                    text = note.title
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = note.description
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    style = MaterialTheme.typography.labelSmall,
                    text = note.formattedTimestamp
                )
            }
            IconButton(onClick = { showDeleteNoteDialog = true }) {
                Icon(
                    modifier = Modifier.fillMaxSize(),
                    imageVector = Icons.Default.Delete,
                    contentDescription = stringResource(id = R.string.delete)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNoteItem() {
    NoteItem(
        note = NoteEntity(
            title = "Sample Note",
            description = "This is a sample note description. This is a sample note description"
        ),
        onNoteClick = { },
        onDeleteNoteClick = { }
    )
}