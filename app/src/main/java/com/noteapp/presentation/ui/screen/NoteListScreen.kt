package com.noteapp.presentation.ui.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
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

    val uiState by viewModel.uiState.collectAsState()
    var isGridView by rememberSaveable { mutableStateOf(value = false) }

    Scaffold(
        topBar = {
            NoteListTopBar(viewModel = viewModel)
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
                        NoteListSuccess(
                            noteList = noteList,
                            isGridView = isGridView,
                            onNoteListViewChanged = { changedNoteListView ->
                                isGridView = changedNoteListView
                            },
                            onNoteClick = onNoteClick,
                            onDeleteNote = { noteId ->
                                viewModel.deleteNote(noteId)
                            }
                        )
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
fun NoteListTopBar(viewModel: NoteListViewModel) {
    TopAppBar(
        title = {
            SearchBar(
                searchQuery = viewModel.searchQueryToShowInSearchBox,
                onQueryChange = { newQuery ->
                    viewModel.onQueryChanged(newQuery = newQuery)
                }
            )
        }
    )
}

// Actual searchBar
@Composable
fun SearchBar(
    searchQuery: String,
    onQueryChange:(String) -> Unit
) {
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
            .padding(end = 16.dp)
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

// Add note button
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
fun NoteListSuccess(
    noteList: List<Note>,
    isGridView: Boolean,
    onNoteListViewChanged: (isGridView: Boolean) -> Unit,
    onNoteClick: (String) -> Unit,
    onDeleteNote: (String) -> Unit
) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 24.dp, top = 24.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                style = MaterialTheme.typography.headlineSmall,
                text = stringResource(id = R.string.note_list_title)
            )
            Icon(
                painter = if (isGridView) {
                    painterResource(id = R.drawable.grid_view)
                } else{
                    painterResource(id = R.drawable.list_view)
                },
                contentDescription = if (isGridView) {
                    stringResource(id = R.string.switch_to_grid_view)
                } else{
                    stringResource(id = R.string.switch_to_list_view)
                },
                modifier = Modifier
                    .size(size = 24.dp)
                    .clickable {
                        onNoteListViewChanged(!isGridView)
                    }
                    .padding(all = 0.dp) // ensure no extra padding
            )
        }
        if(isGridView) {
            NoteListGridAdaptive(
                noteList = noteList,
                onNoteClick = onNoteClick,
                onDeleteNote = onDeleteNote
            )
        } else {
            NoteList(
                noteList = noteList,
                onNoteClick = onNoteClick,
                onDeleteNote = onDeleteNote
            )
        }
    }
}

// Note list view
@Composable
fun NoteList(
    noteList: List<Note>,
    onNoteClick:(String) -> Unit,
    onDeleteNote:(String) -> Unit
) {
    LazyColumn(modifier = Modifier.padding(top = 16.dp)) {
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

// Note grid view
@Composable
fun NoteListGridAdaptive(
    noteList: List<Note>,
    onNoteClick:(String) -> Unit,
    onDeleteNote:(String) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 160.dp),
        contentPadding = PaddingValues(all = 8.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(
            items = noteList,
            key = { it.id }
        ) { note ->
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

// Note list empty
@Composable
fun NoteListEmpty() {
    Column(
        modifier = Modifier.fillMaxSize(),
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