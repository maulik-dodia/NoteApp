package com.noteapp.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.noteapp.R
import com.noteapp.data.repository.NoteRepositoryImpl
import com.noteapp.presentation.ui.component.ConfirmationDialog
import com.noteapp.presentation.viewmodel.AddEditNoteViewModel
import com.noteapp.preview.FakeNoteDao
import com.noteapp.util.NoteConstant.NOTE_ACTION
import com.noteapp.util.NoteConstant.PREVIEW_NOTE_DESC
import com.noteapp.util.NoteConstant.PREVIEW_NOTE_TITLE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: AddEditNoteViewModel
) {
    val noteAddedMsg = stringResource(id = R.string.note_added)
    val noteUpdatedMsg = stringResource(id = R.string.note_updated)
    val noteDeletedMsg = stringResource(id = R.string.note_deleted)

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
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.add_note))
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(all = 16.dp),
            verticalArrangement = Arrangement.Top
        ) {
            TextField(
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
            Spacer(modifier = Modifier.height(height = 8.dp))
            TextField(
                modifier = Modifier.fillMaxWidth(),
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
            Spacer(modifier = Modifier.height(height = 8.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.saveNote {
                        navController.apply {
                            previousBackStackEntry
                                ?.savedStateHandle
                                ?.set(NOTE_ACTION, if(viewModel.isEdit) noteUpdatedMsg else noteAddedMsg)
                            popBackStack()
                        }
                    }
                },
                enabled = viewModel.isUserInputValid
            ) {
                Text(text = stringResource(id = R.string.save))
            }
            if(viewModel.isEdit) {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = { showDeleteNoteDialog = true }
                ) {
                    Text(text = stringResource(id = R.string.delete))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddEditNoteScreen() {
    val noteDao = FakeNoteDao()
    val dummyRepository = NoteRepositoryImpl(noteDao)
    val dummyViewModel = AddEditNoteViewModel(dummyRepository).apply {
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