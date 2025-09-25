package com.noteapp.presentation.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.noteapp.R
import com.noteapp.data.repository.FirestoreDBRepositoryImpl
import com.noteapp.data.repository.RoomDBRepositoryImpl
import com.noteapp.domain.model.Note
import com.noteapp.presentation.ui.component.ConfirmationDialog
import com.noteapp.presentation.ui.component.NoteItem
import com.noteapp.presentation.ui.component.NoteItemShimmer
import com.noteapp.presentation.viewmodel.NoteListUiState
import com.noteapp.presentation.viewmodel.NoteListViewModel
import com.noteapp.preview.FakeNoteDao
import com.noteapp.util.NoteConstant.EIGHT
import com.noteapp.util.NoteConstant.EMPTY_STRING
import com.noteapp.util.NoteConstant.NOTE_ACTION

@OptIn(markerClass = [ExperimentalMaterial3Api::class])
@Composable
fun NoteListScreen(navController: NavController,
                   viewModel: NoteListViewModel,
                   onNoteClick:(String) -> Unit,
                   onAddNoteClick:() -> Unit) {

    // SnackBar for showing messages
    val snackBarHostState = remember { SnackbarHostState() }
    ShowSnackBarMsg(
        navController = navController,
        viewModel = viewModel,
        snackBarHostState = snackBarHostState
    )

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
            NoteListTopBar(viewModel = viewModel, hasNotes = hasNotes) {
                showDeleteAllNotesDialog = true
            }
        },
        floatingActionButton = {
            NoteAddEdit {
                onAddNoteClick()
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues = paddingValues)) {
            when (uiState) {
                NoteListUiState.Loading -> {
                    NoteShimmer()
                }

                is NoteListUiState.Success -> {
                    val noteList = (uiState as NoteListUiState.Success).noteList
                    if(noteList.isNotEmpty()) {
                        NoteList(noteList = noteList, onNoteClick = onNoteClick) { noteId ->
                            viewModel.deleteNote(noteId = noteId)
                        }
                    } else {
                        NoteListEmpty()
                    }
                }

                is NoteListUiState.Error -> {
                    val errorMsg = (uiState as NoteListUiState.Error).message
                    viewModel.showError(errStr = errorMsg)
                }
            }
        }
    }
}

@Composable
fun ShowSnackBarMsg(
    navController: NavController,
    viewModel: NoteListViewModel,
    snackBarHostState: SnackbarHostState
) {
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
}

// Note topbar
@OptIn(markerClass = [ExperimentalMaterial3Api::class])
@Composable
fun NoteListTopBar(
    viewModel: NoteListViewModel,
    hasNotes: Boolean,
    onDeleteAllNotesClick:() -> Unit
) {
    TopAppBar(
        title = {
            SearchBar(
                searchQuery = viewModel.searchQueryToShowInSearchBox,
                onQueryChange = { newQuery ->
                    viewModel.onQueryChanged(newQuery = newQuery)
                }
            )
        },
        actions = {
            if (hasNotes) {
                TextButton(onClick = {
                    onDeleteAllNotesClick()
                }) {
                    Text(text = stringResource(id = R.string.delete_all))
                }
            }
        }
    )
}

@Composable
fun SearchBar(searchQuery: String, onQueryChange:(String) -> Unit) {

    val textStyle = MaterialTheme.typography.bodyLarge
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    OutlinedTextField(
        value = searchQuery,
        onValueChange = { newQuery ->
            onQueryChange(newQuery)
        },
        singleLine = true,
        textStyle = textStyle,
        placeholder = {
            Text(text = stringResource(id = R.string.search_notes), style = textStyle)
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            capitalization = KeyboardCapitalization.Sentences
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        ),
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = stringResource(id = R.string.search)
            )
        },
        trailingIcon = {
            if (searchQuery.isNotEmpty()) {
                IconButton(onClick = { onQueryChange(EMPTY_STRING) }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(id = R.string.clear)
                    )
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(size = 32.dp)
            ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

// Note add edit
@Composable
fun NoteAddEdit(onAddNoteClick: () -> Unit) {
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

// Note shimmer
@Composable
fun NoteShimmer() {
    LazyColumn {
        items(count = EIGHT) {
            NoteItemShimmer()
        }
    }
}

// Note list success
@Composable
fun NoteList(noteList: List<Note>, onNoteClick:(String) -> Unit, onDeleteNote:(String) -> Unit) {
    Column {
        Text(
            modifier = Modifier.padding(start = 24.dp, top = 24.dp),
            style = MaterialTheme.typography.headlineSmall,
            text = stringResource(id = R.string.note_list_title)
        )
        LazyColumn(
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items(items = noteList) { note ->
                NoteItem(
                    note = note,
                    onNoteClick = { noteId ->
                        onNoteClick(noteId)
                    },
                    onDeleteNoteClick = {
                        onDeleteNote(note.id)
                    }
                )
            }
        }
    }
}

// Note list empty
@Composable
fun NoteListEmpty() {
    Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            style = MaterialTheme.typography.displaySmall,
            text = stringResource(id = R.string.no_notes_available)
        )
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