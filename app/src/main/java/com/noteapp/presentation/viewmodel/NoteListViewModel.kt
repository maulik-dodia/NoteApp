package com.noteapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noteapp.data.local.NoteEntity
import com.noteapp.data.repository.NoteRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteListViewModel(private val noteRepository: NoteRepository) : ViewModel() {

    private var _uiState = MutableStateFlow<NoteListUiState>(NoteListUiState.Loading)
    val uiState: StateFlow<NoteListUiState> = _uiState

    init {
        viewModelScope.launch {
            delay(2000)
            try {
                noteRepository.getAllNotes().collect { noteList ->
                    _uiState.value = NoteListUiState.Success(noteList)
                }
            } catch (e: Exception) {
                _uiState.value = NoteListUiState.Error("Failed to load notes: ${e.message}")
            }
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            noteRepository.deleteAllNotes()
            _uiState.value = NoteListUiState.Success(emptyList())
        }
    }

    fun deleteNote(note: NoteEntity) {
        viewModelScope.launch {
            noteRepository.deleteNote(note)
        }
    }
}

sealed class NoteListUiState {
    object Loading : NoteListUiState()
    data class Success(val noteList: List<NoteEntity>) : NoteListUiState()
    data class Error(val message: String) : NoteListUiState()
}