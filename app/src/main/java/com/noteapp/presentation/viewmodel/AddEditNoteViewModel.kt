package com.noteapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noteapp.data.local.NoteEntity
import com.noteapp.data.repository.NoteRepository
import com.noteapp.util.NoteConstant.EMPTY_STRING
import com.noteapp.util.NoteConstant.MINUS_ONE
import kotlinx.coroutines.launch

class AddEditNoteViewModel(
    private val noteRepository: NoteRepository,
    val noteId: Int? = null
) : ViewModel() {

    private var noteToBeEdited: NoteEntity? = null

    var isEdit by mutableStateOf(false)
    var noteTitle by mutableStateOf(EMPTY_STRING)
    var noteDesc by mutableStateOf(EMPTY_STRING)
    var noteTitleError by mutableStateOf(false)
    var noteDescError by mutableStateOf(false)

    init {
        noteId?.let { selectedNoteId ->
            if(selectedNoteId != MINUS_ONE) {
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

    val isUserInputValid
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
                if(selectedNoteId != MINUS_ONE) {
                    val note = NoteEntity(
                        id = selectedNoteId,
                        title = noteTitle.trim(),
                        description = noteDesc.trim()
                    )
                    noteRepository.updateNote(note)
                    goBack()
                    return@launch
                }
            }
            val note = NoteEntity(title = noteTitle.trim(), description = noteDesc.trim())
            noteRepository.insertNote(note)
            goBack()
        }
    }

    fun deleteNote(goBack:() -> Unit) {
        viewModelScope.launch {
            noteToBeEdited?.let {
                noteRepository.deleteNote(note = it)
                goBack()
            }
        }
    }
}