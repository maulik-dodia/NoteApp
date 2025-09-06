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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.noteapp.R
import com.noteapp.data.repository.NoteRepositoryImpl
import com.noteapp.presentation.viewmodel.AddEditNoteViewModel
import com.noteapp.preview.FakeNoteDao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController,
    viewModel: AddEditNoteViewModel
) {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
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
            Spacer(modifier = Modifier.height(8.dp))
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
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    viewModel.saveNote {
                        navController.popBackStack()
                    }
                },
                enabled = viewModel.isAnyError
            ) {
                Text(text = stringResource(id = R.string.save_note))
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
        noteTitle = "Sample Title"
        noteDesc = "Sample Description"
        noteTitleError = true
        noteDescError = true
    }
    val navController = rememberNavController()
    AddEditNoteScreen(navController = navController, viewModel = dummyViewModel)
}