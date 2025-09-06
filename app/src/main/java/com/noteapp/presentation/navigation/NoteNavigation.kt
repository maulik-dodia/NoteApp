package com.noteapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.noteapp.data.repository.NoteRepository
import com.noteapp.presentation.ui.screen.AddEditNoteScreen
import com.noteapp.presentation.ui.screen.NoteListScreen
import com.noteapp.presentation.viewmodel.AddEditNoteViewModel
import com.noteapp.presentation.viewmodel.AddEditNoteViewModelFactory
import com.noteapp.presentation.viewmodel.NoteListViewModel
import com.noteapp.presentation.viewmodel.NoteListViewModelFactory

@Composable
fun NoteNavigation(noteRepository: NoteRepository) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "notes_list") {
        // Note List Screen
        composable("notes_list") { navBackStackEntry ->
            val factory = NoteListViewModelFactory(noteRepository)
            val noteListViewModel: NoteListViewModel = viewModel(
                viewModelStoreOwner = navBackStackEntry,
                factory = factory
            )
            NoteListScreen(
                onAddEditNoteClick = {
                    navController.navigate("add_edit_note")
                },
                viewModel = noteListViewModel
            )
        }
        // Add Edit Note Screen
        composable("add_edit_note") { navBackStackEntry ->
            val factory = AddEditNoteViewModelFactory(noteRepository)
            val addEditNoteViewModel: AddEditNoteViewModel = viewModel(
                viewModelStoreOwner = navBackStackEntry,
                factory = factory
            )
            AddEditNoteScreen(
                navController = navController,
                viewModel = addEditNoteViewModel
            )
        }
    }
}