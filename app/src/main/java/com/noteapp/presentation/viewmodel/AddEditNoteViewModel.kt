package com.noteapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noteapp.data.repository.FirestoreDBRepository
import com.noteapp.data.repository.RoomDBRepository
import com.noteapp.domain.model.Note
import com.noteapp.util.NoteConstant.EMPTY_STRING
import com.noteapp.util.NoteConstant.GENERIC_ERROR
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AddEditNoteViewModel(
    val noteId: String? = null,
    private val roomRepository: RoomDBRepository,
    private val firestoreRepository: FirestoreDBRepository
) : ViewModel() {

    private val _snackBarEvent = MutableSharedFlow<String>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    private var noteToBeEdited: Note? = null

    var isEdit by mutableStateOf(false)
    var noteTitle by mutableStateOf(EMPTY_STRING)
    var noteDesc by mutableStateOf(EMPTY_STRING)
    var noteTitleError by mutableStateOf(false)
    var noteDescError by mutableStateOf(false)

    init {
        noteId?.let { selectedNoteId ->
            /*if(selectedNoteId != MINUS_ONE) {*/
                isEdit = true
                viewModelScope.launch {
                    /*roomRepository.getNoteById(id = selectedNoteId).collect { note ->
                        noteToBeEdited = note
                        noteToBeEdited?.let {
                            noteTitle = it.title
                            noteDesc = it.description
                        }
                    }*/
                    noteToBeEdited = firestoreRepository.getNoteById(selectedNoteId)
                    noteToBeEdited?.let {
                        noteTitle = it.title
                        noteDesc = it.description
                    }
                }
            /*}*/
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

    // TODO Remove when implement single source of truth
    /*fun saveNote(goBack:() -> Unit) {
        viewModelScope.launch {
            noteId?.let { selectedNoteId ->
                if(selectedNoteId != MINUS_ONE) {
                    val note = NoteEntity(
                        id = selectedNoteId,
                        title = noteTitle.trim(),
                        description = noteDesc.trim()
                    )
                    roomDBRepository.updateNote(note)
                    goBack()
                    return@launch
                }
            }
            val note = NoteEntity(title = noteTitle.trim(), description = noteDesc.trim())
            roomDBRepository.insertNote(note)
            goBack()
        }
    }*/

    fun saveNoteFirestore(goBack:() -> Unit) {
        viewModelScope.launch {
            try {
                // Update operation
                noteId?.let { selectedNoteId ->
                    val note = Note(
                        id = selectedNoteId,
                        title = noteTitle.trim(),
                        description = noteDesc.trim()
                    )
                    firestoreRepository.updateNote(note)
                    goBack()
                    return@launch
                }
                // Insert operation
                val note = Note(title = noteTitle.trim(), description = noteDesc.trim())
                firestoreRepository.insertNote(note = note)
                goBack()
            } catch (e: Exception) {
               _snackBarEvent.emit(value = GENERIC_ERROR)
            }
        }
    }

    fun deleteNote(goBack:() -> Unit) {
        viewModelScope.launch {
            noteToBeEdited?.let {
                //roomRepository.deleteNote(note = it)
                firestoreRepository.deleteNote(id = it.id)
                goBack()
            }
        }
    }
}