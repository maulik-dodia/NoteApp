package com.noteapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noteapp.data.local.NoteEntity
import com.noteapp.data.repository.NoteRepository
import kotlinx.coroutines.launch

class AddEditNoteViewModel(
    private val noteRepository: NoteRepository,
    val noteId: Int? = null
) : ViewModel() {

    private var noteToBeEdited: NoteEntity? = null

    var isEdit by mutableStateOf(false)
    var noteTitle by mutableStateOf("")
    var noteDesc by mutableStateOf("")
    var noteTitleError by mutableStateOf(false)
    var noteDescError by mutableStateOf(false)

    init {
        noteId?.let { selectedNoteId ->
            if(selectedNoteId != -1) {
                isEdit = true
                viewModelScope.launch {
                    noteRepository.getNoteById(id = selectedNoteId).collect { note ->
                        noteToBeEdited = note
                        noteToBeEdited?.let {
                            noteTitle = it.title
                            noteDesc = it.description
                        }
                    }
                }
            }
        }
    }

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

    fun saveNote(goBack:() -> Unit) {
        viewModelScope.launch {
            noteId?.let { selectedNoteId ->
                if(selectedNoteId != -1) {
                    val note = NoteEntity(id = selectedNoteId, title = noteTitle, description = noteDesc)
                    noteRepository.updateNote(note)
                    goBack()
                    return@launch
                }
            }
            val note = NoteEntity(title = noteTitle, description = noteDesc)
            noteRepository.insertNote(note)
            goBack()
        }
    }

    fun deleteNote(goBack:() -> Unit) {
        viewModelScope.launch {
            noteToBeEdited?.let {
                noteRepository.deleteNote(it)
                goBack()
            }
        }
    }
}