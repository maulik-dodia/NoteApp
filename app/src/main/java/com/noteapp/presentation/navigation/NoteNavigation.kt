package com.noteapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.noteapp.data.repository.NoteRepository
import com.noteapp.presentation.ui.screen.AddEditNoteScreen
import com.noteapp.presentation.ui.screen.NoteListScreen
import com.noteapp.presentation.viewmodel.AddEditNoteViewModel
import com.noteapp.presentation.viewmodel.AddEditNoteViewModelFactory

@Composable
fun NoteNavigation(noteRepository: NoteRepository) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "notes_list") {
        composable("notes_list") {
            NoteListScreen(
                onAddEditNoteClick = {
                    navController.navigate("add_edit_note")
                }
            )
        }
        composable("add_edit_note") {
            val factory = AddEditNoteViewModelFactory(noteRepository)
            val navBackStackEntry = remember(navController.currentBackStackEntry) {
                navController.getBackStackEntry("add_edit_note")
            }
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