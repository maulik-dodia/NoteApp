package com.noteapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.noteapp.data.repository.NoteRepository

class AddEditNoteViewModelFactory(
    private val noteId: Int?,
    private val repository: NoteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditNoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddEditNoteViewModel(noteRepository = repository, noteId = noteId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}