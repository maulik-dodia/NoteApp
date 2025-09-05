package com.noteapp.presentation.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.noteapp.R
import com.noteapp.presentation.ui.component.NoteItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(onAddEditNoteClick: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.note_list_title))
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.Home,
                        contentDescription = stringResource(id = R.string.home)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddEditNoteClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_note)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            items(10) {
                NoteItem()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNoteListScreen() {
    NoteListScreen(
        onAddEditNoteClick = {}
    )
}