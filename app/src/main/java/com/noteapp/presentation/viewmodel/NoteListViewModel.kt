package com.noteapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noteapp.data.repository.FirestoreDBRepository
import com.noteapp.data.repository.RoomDBRepository
import com.noteapp.domain.model.Note
import com.noteapp.util.NoteConstant.ALL_NOTES_DELETED_DESC
import com.noteapp.util.NoteConstant.GENERIC_ERROR
import com.noteapp.util.NoteConstant.NOTE_DELETED_DESC
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NoteListViewModel(
    private val roomRepository: RoomDBRepository,
    private val firestoreRepository: FirestoreDBRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow<NoteListUiState>(value = NoteListUiState.Loading)
    val uiState: StateFlow<NoteListUiState> = _uiState

    private val _snackBarEvent = MutableSharedFlow<String>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    init {
        observeAllNotesFirestore()
        // TODO Remove when implement single source of truth
        /*viewModelScope.launch {
            try {
                noteRepository.getAllNotes().collect { noteList ->
                    _uiState.value = NoteListUiState.Success(noteList)
                }
            } catch (e: Exception) {
                _uiState.value = NoteListUiState.Error(message = "$FAILED_TO_LOAD_DESC ${e.message}")
            }
        }*/
    }

    private fun observeAllNotesFirestore() {
        viewModelScope.launch {
            firestoreRepository.observeAllNotes().collect { notes ->
                _uiState.value = NoteListUiState.Success(notes)
            }
        }
    }

    // TODO Remove when implement single source of truth
    /*fun deleteNote(note: Note) {
        viewModelScope.launch {
            //noteRepository.deleteNote(note)
            _snackBarEvent.emit(value = NOTE_DELETED_DESC)
        }
    }*/

    fun deleteNoteFirestore(noteId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = NoteListUiState.Loading
                firestoreRepository.deleteNote(id = noteId)
                _snackBarEvent.emit(value = NOTE_DELETED_DESC)
            } catch (e: Exception) {
                _uiState.value = NoteListUiState.Error(message = "$GENERIC_ERROR ${e.message}")
            }
        }
    }

    // TODO Remove when implement single source of truth
    /*fun deleteAllNotes() {
        viewModelScope.launch {
            noteRepository.deleteAllNotes()
            _snackBarEvent.emit(value = ALL_NOTES_DELETED_DESC)
        }
    }*/

    fun deleteAllNotesFirestore() {
        viewModelScope.launch {
            try {
                _uiState.value = NoteListUiState.Loading
                firestoreRepository.deleteAllNotes()
                _snackBarEvent.emit(value = ALL_NOTES_DELETED_DESC)
            } catch (e: Exception) {
                _uiState.value = NoteListUiState.Error(message = "$GENERIC_ERROR ${e.message}")
            }
        }
    }
}

sealed class NoteListUiState {
    object Loading : NoteListUiState()
    data class Success(val noteList: List<Note>) : NoteListUiState()
    data class Error(val message: String) : NoteListUiState()
}