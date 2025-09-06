package com.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.noteapp.data.repository.NoteRepositoryImpl
import com.noteapp.presentation.navigation.NoteNavigation
import com.noteapp.ui.theme.NoteAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = (application as NoteApp).database
        val noteRepository = NoteRepositoryImpl(db.noteDao())

        enableEdgeToEdge()
        setContent {
            NoteAppTheme {
                NoteNavigation(noteRepository)
            }
        }
    }
}