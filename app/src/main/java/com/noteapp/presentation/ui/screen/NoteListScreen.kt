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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.noteapp.R
import com.noteapp.data.repository.FirestoreDBRepositoryImpl
import com.noteapp.data.repository.RoomDBRepositoryImpl
import com.noteapp.presentation.ui.component.ConfirmationDialog
import com.noteapp.presentation.ui.component.NoteItem
import com.noteapp.presentation.ui.component.NoteItemShimmer
import com.noteapp.presentation.viewmodel.NoteListUiState
import com.noteapp.presentation.viewmodel.NoteListViewModel
import com.noteapp.preview.FakeNoteDao
import com.noteapp.util.NoteConstant.EIGHT
import com.noteapp.util.NoteConstant.NOTE_ACTION

@OptIn(markerClass = [ExperimentalMaterial3Api::class])
@Composable
fun NoteListScreen(navController: NavController,
                   viewModel: NoteListViewModel,
                   onNoteClick:(String) -> Unit,
                   onAddNoteClick:() -> Unit) {

    // SnackBar for showing messages
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = Unit) {
        val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
        savedStateHandle?.get<String>(NOTE_ACTION)?.let { noteState ->
            noteState.let {
                snackBarHostState.showSnackbar(message = noteState)
                savedStateHandle.remove<Boolean>(key = NOTE_ACTION)
            }
        }
    }
    /*
    As we are in same screen for SingleNoteDelete and AllNotesDelete functionalities,
    We can't use savedStateHandle approach to show snackBar.
    So, using ViewModel's SharedFlow to show snackBar for these events
    */
    LaunchedEffect(key1 = viewModel.snackBarEvent) {
        viewModel.snackBarEvent.collect { msg ->
            snackBarHostState.showSnackbar(message = msg)
        }
    }

    // Confirmation dialog for deleting all notes
    var showDeleteAllNotesDialog by remember { mutableStateOf(value = false) }
    if(showDeleteAllNotesDialog) {
        ConfirmationDialog(
            title = stringResource(id = R.string.delete_all_notes),
            message = stringResource(id = R.string.delete_all_notes_desc),
            onConfirm = {
                viewModel.deleteAllNotes()
                showDeleteAllNotesDialog = false
            },
            onDismiss = { showDeleteAllNotesDialog = false }
        )
    }

    val uiState by viewModel.uiState.collectAsState()
    val hasNotes = (uiState as? NoteListUiState.Success)?.noteList?.isNotEmpty() == true

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
        },
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->
        when (uiState) {
            NoteListUiState.Loading -> {
                LazyColumn(modifier = Modifier.padding(paddingValues)) {
                    items(count = EIGHT) {
                        NoteItemShimmer()
                    }
                }
            }

            is NoteListUiState.Success -> {
                val noteList = (uiState as NoteListUiState.Success).noteList
                if(noteList.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.padding(paddingValues)) {
                        items(items = noteList) { note ->
                            NoteItem(
                                note = note,
                                onNoteClick = { noteId ->
                                    onNoteClick(noteId)
                                },
                                onDeleteNoteClick = {
                                    viewModel.deleteNote(noteId = note.id)
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
    val firestore = FirebaseFirestore.getInstance()
    val fakeRoomRepository = RoomDBRepositoryImpl(noteDao = noteDao)
    val fakeFirestoreRepository = FirestoreDBRepositoryImpl(firestore = firestore)
    val dummyViewModel = NoteListViewModel(
        roomRepository = fakeRoomRepository,
        firestoreRepository = fakeFirestoreRepository
    )
    val navController = rememberNavController()
    NoteListScreen(
        onNoteClick = { },
        onAddNoteClick = { },
        viewModel = dummyViewModel,
        navController = navController
    )
}