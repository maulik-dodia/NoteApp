package com.noteapp.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.noteapp.R
import com.noteapp.data.repository.FirestoreDBRepositoryImpl
import com.noteapp.data.repository.RoomDBRepositoryImpl
import com.noteapp.presentation.ui.component.ConfirmationDialog
import com.noteapp.presentation.viewmodel.AddEditNoteViewModel
import com.noteapp.preview.FakeNoteDao
import com.noteapp.util.NoteConstant.FLOAT_ONE
import com.noteapp.util.NoteConstant.NOTE_ACTION
import com.noteapp.util.NoteConstant.PREVIEW_NOTE_DESC
import com.noteapp.util.NoteConstant.PREVIEW_NOTE_TITLE

@OptIn(markerClass = [ExperimentalMaterial3Api::class])
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: AddEditNoteViewModel
) {
    val noteAddedMsg = stringResource(id = R.string.note_added)
    val noteUpdatedMsg = stringResource(id = R.string.note_updated)
    val noteDeletedMsg = stringResource(id = R.string.note_deleted)

    // SnackBar for showing messages
    val snackBarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = viewModel.snackBarEvent) {
        viewModel.snackBarEvent.collect { msg ->
            snackBarHostState.showSnackbar(message = msg)
        }
    }

    var showDeleteNoteDialog by remember { mutableStateOf(value = false) }
    if(showDeleteNoteDialog) {
        ConfirmationDialog(
            title = stringResource(id = R.string.delete_note),
            message = stringResource(id = R.string.delete_note_desc),
            onConfirm = {
                viewModel.deleteNote {
                    navController.apply {
                        previousBackStackEntry?.savedStateHandle?.set(NOTE_ACTION, noteDeletedMsg)
                        popBackStack()
                    }
                }
                showDeleteNoteDialog = false
            },
            onDismiss = { showDeleteNoteDialog = false }
        )
    }

    Scaffold(
        topBar = {
            AddEditNoteTopBar(
                onBackClick = { navController.popBackStack() }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        }
    ) { paddingValues ->
        AddEditNoteContent(
            paddingValues = paddingValues,
            viewModel = viewModel,
            onSaveClick = {
                navController.apply {
                    previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(NOTE_ACTION, if(viewModel.isEdit) noteUpdatedMsg else noteAddedMsg)
                    popBackStack()
                }
            },
            onDeleteClick = { showDeleteNoteDialog = true }
        )
    }
}

@Composable
fun AddEditNoteContent(paddingValues: PaddingValues,
                       viewModel: AddEditNoteViewModel,
                       onSaveClick: () -> Unit,
                       onDeleteClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(all = 16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(id = R.string.title_label)) },
            value = viewModel.noteTitle,
            onValueChange = { newTitle ->
                viewModel.apply {
                    noteTitle = newTitle
                    noteTitleError = false
                    onTitleChange(newTitle)
                }
            },
            isError = viewModel.noteTitleError,
            supportingText = {
                if (viewModel.noteTitleError) {
                    Text(text = stringResource(id = R.string.field_required))
                }
            }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = FLOAT_ONE),
            value = viewModel.noteDesc,
            onValueChange = { newDesc ->
                viewModel.apply {
                    noteDesc = newDesc
                    noteDescError = false
                    onDescChange(newDesc)
                }
            },
            label = { Text(text = stringResource(id = R.string.description_label)) },
            isError = viewModel.noteDescError,
            supportingText = {
                if (viewModel.noteDescError) {
                    Text(text = stringResource(id = R.string.field_required))
                }
            }
        )
        SaveDeleteButton(
            isEdit = viewModel.isEdit,
            isUserInputValid = viewModel.isUserInputValid,
            onSaveClick = {
                viewModel.saveNote { onSaveClick() }
            },
            onDeleteClick = { onDeleteClick() }
        )
    }
}

@OptIn(markerClass = [ExperimentalMaterial3Api::class])
@Composable
fun AddEditNoteTopBar(onBackClick:() -> Unit) {
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.add_note))
        },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back)
                )
            }
        }
    )
}

@Composable
fun SaveDeleteButton(isEdit: Boolean = false,
                     isUserInputValid: Boolean = false,
                     onSaveClick:() -> Unit,
                     onDeleteClick:() -> Unit) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onSaveClick() },
        enabled = isUserInputValid
    ) {
        Text(text = stringResource(id = R.string.save))
    }
    if(isEdit) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onDeleteClick() }
        ) {
            Text(text = stringResource(id = R.string.delete))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddEditNoteScreen() {
    val noteDao = FakeNoteDao()
    val firestore = FirebaseFirestore.getInstance()
    val fakeRoomDBRepository = RoomDBRepositoryImpl(noteDao = noteDao)
    val fakeFirestoreRepository = FirestoreDBRepositoryImpl(firestore = firestore)
    val dummyViewModel = AddEditNoteViewModel(
        roomRepository = fakeRoomDBRepository,
        firestoreRepository = fakeFirestoreRepository
    ).apply {
        noteTitle = PREVIEW_NOTE_TITLE
        noteDesc = PREVIEW_NOTE_DESC
        noteTitleError = true
        noteDescError = true
    }
    val navController = rememberNavController()
    AddEditNoteScreen(
        navController = navController,
        viewModel = dummyViewModel
    )
}