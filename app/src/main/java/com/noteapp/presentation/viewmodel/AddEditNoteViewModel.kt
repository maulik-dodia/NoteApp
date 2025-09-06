package com.noteapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noteapp.data.local.NoteEntity
import com.noteapp.data.repository.NoteRepository
import kotlinx.coroutines.launch

class AddEditNoteViewModel(private val noteRepository: NoteRepository) : ViewModel() {

    var noteTitle by mutableStateOf("")
    var noteDesc by mutableStateOf("")
    var noteTitleError by mutableStateOf(false)
    var noteDescError by mutableStateOf(false)

    val isAnyError
        get() = noteTitle.isNotBlank() && noteDesc.isNotBlank() && !noteTitleError && !noteDescError

    fun onTitleChange(newTitle: String) {
        noteTitle = newTitle
        noteTitleError = newTitle.isBlank()
    }

    fun onDescChange(newDesc: String) {
        noteDesc = newDesc
        noteDescError = newDesc.isBlank()
    }

    fun onSaveNote() {
        viewModelScope.launch {
            val note = NoteEntity(title = noteTitle, description = noteDesc)
            noteRepository.insertNote(note)
        }
    }
}