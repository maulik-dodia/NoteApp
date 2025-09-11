package com.noteapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.noteapp.data.repository.FirestoreDBRepository
import com.noteapp.data.repository.RoomDBRepository
import com.noteapp.presentation.ui.screen.AddEditNoteScreen
import com.noteapp.presentation.ui.screen.NoteListScreen
import com.noteapp.presentation.viewmodel.AddEditNoteViewModel
import com.noteapp.presentation.viewmodel.AddEditNoteViewModelFactory
import com.noteapp.presentation.viewmodel.NoteListViewModel
import com.noteapp.presentation.viewmodel.NoteListViewModelFactory
import com.noteapp.util.NoteConstant.ADD_EDIT_NOTE
import com.noteapp.util.NoteConstant.NOTE_ID
import com.noteapp.util.NoteConstant.NOTE_LIST

@Composable
fun NoteNavigation(roomRepository: RoomDBRepository,
                   firestoreRepository: FirestoreDBRepository,
                   noteListViewModelFactory: NoteListViewModelFactory) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = NOTE_LIST) {
        // Note List Screen
        composable(route = NOTE_LIST) { navBackStackEntry ->
            val noteListViewModel: NoteListViewModel = viewModel(
                viewModelStoreOwner = navBackStackEntry,
                factory = noteListViewModelFactory
            )
            NoteListScreen(
                navController = navController,
                viewModel = noteListViewModel,
                onNoteClick = { noteId ->
                    navController.navigate(route = "$ADD_EDIT_NOTE?${NOTE_ID}=$noteId")
                },
                onAddNoteClick = {
                    navController.navigate(route = ADD_EDIT_NOTE)
                }
            )
        }
        // Add Edit Note Screen
        composable(
            route = "$ADD_EDIT_NOTE?${NOTE_ID}={$NOTE_ID}",
            arguments = listOf(
                navArgument(name = NOTE_ID) {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) { navBackStackEntry ->
            val noteId = navBackStackEntry.arguments?.getString(NOTE_ID)
            val factory = AddEditNoteViewModelFactory(
                noteId = noteId,
                roomRepository = roomRepository,
                firestoreRepository = firestoreRepository
            )
            val addEditNoteViewModel: AddEditNoteViewModel = viewModel(
                factory = factory,
                viewModelStoreOwner = navBackStackEntry
            )
            AddEditNoteScreen(
                navController = navController,
                viewModel = addEditNoteViewModel
            )
        }
    }
}