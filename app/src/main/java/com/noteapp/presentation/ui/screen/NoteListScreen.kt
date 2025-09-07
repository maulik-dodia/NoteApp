package com.noteapp.presentation.ui.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.noteapp.R
import com.noteapp.data.local.NoteEntity
import com.noteapp.data.repository.NoteRepositoryImpl
import com.noteapp.presentation.ui.component.ConfirmationDialog
import com.noteapp.presentation.ui.component.NoteItem
import com.noteapp.presentation.viewmodel.NoteListUiState
import com.noteapp.presentation.viewmodel.NoteListViewModel
import com.noteapp.preview.FakeNoteDao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteListScreen(viewModel: NoteListViewModel,
                   onDeleteNoteClick:(note: NoteEntity) -> Unit,
                   onDeleteAllClick:() -> Unit,
                   onNoteClick:(Int) -> Unit,
                   onAddNoteClick:() -> Unit) {

    val uiState by viewModel.uiState.collectAsState()
    val hasNotes = (uiState as? NoteListUiState.Success)?.noteList?.isNotEmpty() == true

    var showDeleteAllNotesDialog by remember { mutableStateOf(false) }

    if(showDeleteAllNotesDialog) {
        ConfirmationDialog(
            title = "Delete All Notes",
            message = "Are you sure you want to delete all notes?",
            onConfirm = {
                onDeleteAllClick()
                showDeleteAllNotesDialog = false
            },
            onDismiss = { showDeleteAllNotesDialog = false }
        )
    }

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
                },
                actions = {
                    if (hasNotes) {
                        TextButton(onClick = { showDeleteAllNotesDialog = true }) {
                            Text(text = stringResource(id = R.string.delete_all))
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNoteClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.add_note)
                )
            }
        }
    ) { paddingValues ->
        when (uiState) {
            NoteListUiState.Loading -> {
                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CircularProgressIndicator()
                }
            }

            is NoteListUiState.Success -> {
                val noteList = (uiState as NoteListUiState.Success).noteList
                if(noteList.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.padding(paddingValues)) {
                        items(noteList) { note ->
                            NoteItem(
                                note = note,
                                onDeleteNoteClick = {
                                    onDeleteNoteClick(note)
                                },
                                onNoteClick = { noteId ->
                                    onNoteClick(noteId)
                                }
                            )
                        }
                    }
                } else {
                    Column(modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            style = MaterialTheme.typography.displaySmall,
                            text = stringResource(id = R.string.no_notes_available)
                        )
                    }
                }
            }

            is NoteListUiState.Error -> {
                val errorMsg = (uiState as NoteListUiState.Error).message
                Toast.makeText(LocalContext.current, errorMsg, Toast.LENGTH_LONG).show()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewNoteListScreen() {
    val noteDao = FakeNoteDao()
    val dummyRepository = NoteRepositoryImpl(noteDao)
    val dummyViewModel = NoteListViewModel(dummyRepository)
    NoteListScreen(
        onDeleteNoteClick = { },
        onDeleteAllClick = { },
        onNoteClick = { },
        onAddNoteClick = { },
        viewModel = dummyViewModel
    )
}