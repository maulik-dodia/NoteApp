package com.noteapp.presentation.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.noteapp.data.local.NoteEntity
import com.noteapp.data.local.formattedTimestamp

@Composable
fun NoteItem(note: NoteEntity, onNoteClick: (Int) -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp),
        onClick = {
            onNoteClick(note.id)
        }
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = note.title
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    style = MaterialTheme.typography.bodyMedium,
                    text = note.description
                )
                Text(
                    style = MaterialTheme.typography.labelSmall,
                    text = note.formattedTimestamp
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
            description = "This is a sample note description."
        ),
        onNoteClick = { }
    )
}