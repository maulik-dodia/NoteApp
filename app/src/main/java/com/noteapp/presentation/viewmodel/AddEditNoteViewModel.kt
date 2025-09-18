package com.noteapp.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.noteapp.data.repository.FirestoreDBRepository
import com.noteapp.data.repository.RoomDBRepository
import com.noteapp.domain.model.Note
import com.noteapp.util.NoteConstant.EMPTY_STRING
import com.noteapp.util.NoteConstant.NOTE_NOT_FOUND_ERROR
import com.noteapp.util.getFirestoreError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class AddEditNoteViewModel(
    val noteId: String? = null,
    private val roomRepository: RoomDBRepository,
    private val firestoreRepository: FirestoreDBRepository
) : ViewModel() {

    private val _snackBarEvent = MutableSharedFlow<String>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    var isEdit by mutableStateOf(value = false)
    var noteTitle by mutableStateOf(value = EMPTY_STRING)
    var noteDesc by mutableStateOf(value = EMPTY_STRING)
    var noteTitleError by mutableStateOf(value = false)
    var noteDescError by mutableStateOf(value = false)

    private var noteToBeEdited: Note? = null

    init {
        noteId?.let { selectedNoteId ->
            isEdit = true
            viewModelScope.launch {
                try {
                    noteToBeEdited = firestoreRepository.getNoteById(selectedNoteId) // suspend
                    if (noteToBeEdited == null) {
                        _snackBarEvent.emit(value = NOTE_NOT_FOUND_ERROR)
                        return@launch
                    }
                    noteToBeEdited?.let {
                        noteTitle = it.title
                        noteDesc = it.description
                    }
                } catch (ce: CancellationException) {
                    throw ce
                } catch (ex: FirebaseFirestoreException) {
                    val errorMsg = getFirestoreError(firestoreException = ex)
                    _snackBarEvent.emit(value = errorMsg)
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
            try {
                val title = noteTitle.trim()
                val desc = noteDesc.trim()
                if (!noteId.isNullOrEmpty()) { // Update operation
                    val note = Note(id = noteId, title = title, description = desc)
                    firestoreRepository.updateNote(note)
                } else { // Insert operation
                    val note = Note(title = title, description = desc)
                    firestoreRepository.insertNote(note)
                }
                goBack()
            } catch (ce: CancellationException) {
                throw ce
            } catch (ex: FirebaseFirestoreException) {
                val errorMsg = getFirestoreError(firestoreException = ex)
                _snackBarEvent.emit(value = errorMsg)
            }
        }
    }

    fun deleteNote(goBack:() -> Unit) {
        viewModelScope.launch {
            try {
                val note = noteToBeEdited ?: return@launch
                firestoreRepository.deleteNoteById(id = note.id)
                goBack()
            } catch (ce: CancellationException) {
                throw ce
            } catch (ex: FirebaseFirestoreException) {
                val errorMsg = getFirestoreError(firestoreException = ex)
                _snackBarEvent.emit(value = errorMsg)
            }
        }
    }
}