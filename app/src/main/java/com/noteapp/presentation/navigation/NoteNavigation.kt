package com.noteapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
                navController = navController,
                viewModel = noteListViewModel,
                onNoteClick = { noteId ->
                    navController.navigate("add_edit_note/$noteId")
                },
                onAddNoteClick = {
                    navController.navigate("add_edit_note/-1")
                }
            )
        }
        // Add Edit Note Screen
        composable(
            route = "add_edit_note/{noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { navBackStackEntry ->
            val noteId = navBackStackEntry.arguments?.getInt("noteId")
            val factory = AddEditNoteViewModelFactory(noteRepository, noteId)
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