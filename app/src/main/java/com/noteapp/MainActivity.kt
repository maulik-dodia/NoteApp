package com.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.noteapp.data.repository.NoteRepository
import com.noteapp.presentation.navigation.NoteNavigation
import com.noteapp.presentation.viewmodel.NoteListViewModelFactory
import com.noteapp.ui.theme.NoteAppTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {

    @Inject
    lateinit var noteRepository: NoteRepository

    @Inject
    lateinit var noteListViewModelFactory: NoteListViewModelFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as NoteApp).appComponent.inject(activity = this)
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            NoteAppTheme {
                NoteNavigation(
                    noteRepository = noteRepository,
                    noteListViewModelFactory = noteListViewModelFactory
                )
            }
        }
    }
}