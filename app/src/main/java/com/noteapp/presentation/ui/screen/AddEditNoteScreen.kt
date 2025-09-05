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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.noteapp.presentation.viewmodel.AddEditNoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditNoteScreen(
    navController: NavController?,
    viewModel: AddEditNoteViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Add Note")
                },
                navigationIcon = {
                    IconButton(onClick = { navController?.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
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
                label = { Text(text = "Title") },
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
                        Text(text = "This field is required")
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
                label = { Text(text = "Description") },
                isError = viewModel.noteDescError,
                supportingText = {
                    if (viewModel.noteDescError) {
                        Text(text = "This field is required")
                    }
                }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {},
                enabled = viewModel.isAnyError
            ) {
                Text(text = "Save Note")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddEditNoteScreen() {
    val dummyViewModel = AddEditNoteViewModel().apply {
        noteTitle = "Sample Title"
        noteDesc = "Sample Description"
        noteTitleError = true
        noteDescError = true
    }
    AddEditNoteScreen(navController = null, viewModel = dummyViewModel)
}