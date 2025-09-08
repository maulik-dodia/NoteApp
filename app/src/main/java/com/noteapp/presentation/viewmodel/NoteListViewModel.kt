package com.noteapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noteapp.data.local.NoteEntity
import com.noteapp.data.repository.NoteRepository
import com.noteapp.util.NoteConstant.ALL_NOTES_DELETED_DESC
import com.noteapp.util.NoteConstant.FAILED_TO_LOAD_DESC
import com.noteapp.util.NoteConstant.NOTE_DELETED_DESC
import com.noteapp.util.NoteConstant.ONE_THOUSAND_LONG
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class NoteListViewModel(private val noteRepository: NoteRepository) : ViewModel() {

    private var _uiState = MutableStateFlow<NoteListUiState>(value = NoteListUiState.Loading)
    val uiState: StateFlow<NoteListUiState> = _uiState

    private val _snackBarEvent = MutableSharedFlow<String>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            delay(timeMillis = ONE_THOUSAND_LONG)
            try {
                noteRepository.getAllNotes().collect { noteList ->
                    _uiState.value = NoteListUiState.Success(noteList)
                }
            } catch (e: Exception) {
                // TODO: Move FAILED_TO_LOAD_DESC to strings.xml later
                _uiState.value = NoteListUiState.Error(message = "$FAILED_TO_LOAD_DESC ${e.message}")
            }
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            noteRepository.deleteAllNotes()
            // TODO: Move ALL_NOTES_DELETED_DESC to strings.xml later
            _snackBarEvent.emit(value = ALL_NOTES_DELETED_DESC)
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)
            // TODO: Move NOTE_DELETED_DESC to strings.xml later
            _snackBarEvent.emit(value = NOTE_DELETED_DESC)
        }
    }
}

sealed class NoteListUiState {
    object Loading : NoteListUiState()
    data class Success(val noteList: List<NoteEntity>) : NoteListUiState()
    data class Error(val message: String) : NoteListUiState()
}