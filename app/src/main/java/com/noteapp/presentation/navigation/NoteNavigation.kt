package com.noteapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.noteapp.presentation.ui.screen.AddEditNoteScreen
import com.noteapp.presentation.ui.screen.NoteListScreen

@Composable
fun NoteNavigation() {
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
            AddEditNoteScreen(navController = navController)
        }
    }
}