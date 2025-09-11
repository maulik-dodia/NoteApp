package com.noteapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestoreException
import com.noteapp.data.repository.FirestoreDBRepository
import com.noteapp.data.repository.RoomDBRepository
import com.noteapp.domain.model.Note
import com.noteapp.util.NoteConstant.ALL_NOTES_DELETED_MSG
import com.noteapp.util.NoteConstant.GENERIC_ERROR
import com.noteapp.util.NoteConstant.NOTE_DELETED_MSG
import com.noteapp.util.getFirestoreError
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class NoteListViewModel(
    private val roomRepository: RoomDBRepository,
    private val firestoreRepository: FirestoreDBRepository
) : ViewModel() {

    private var _uiState = MutableStateFlow<NoteListUiState>(value = NoteListUiState.Loading)
    val uiState: StateFlow<NoteListUiState> = _uiState

    private val _snackBarEvent = MutableSharedFlow<String>()
    val snackBarEvent = _snackBarEvent.asSharedFlow()

    private var notesJob: Job? = null

    init {
        observeNoteList()
    }

    private fun observeNoteList() {
        notesJob?.cancel()
        notesJob = viewModelScope.launch {
            firestoreRepository.observeNoteList()
                .onStart {
                    _uiState.value = NoteListUiState.Loading
                }
                .catch { ex ->
                    if (ex is CancellationException) throw ex
                    val message = if (ex is FirebaseFirestoreException) {
                        getFirestoreError(firestoreException = ex)
                    } else {
                        GENERIC_ERROR
                    }
                    _uiState.value = NoteListUiState.Error(message)
                }
                .collectLatest { noteList ->
                _uiState.value = NoteListUiState.Success(noteList = noteList)
            }
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            try {
                firestoreRepository.deleteNoteById(id = noteId)
                _snackBarEvent.emit(value = NOTE_DELETED_MSG)
            } catch (ce: CancellationException) {
                throw ce
            } catch (ex: FirebaseFirestoreException) {
                val errorMsg = getFirestoreError(firestoreException = ex)
                _snackBarEvent.emit(value = errorMsg)
            } catch (e: Exception) {
                e.printStackTrace()
                _snackBarEvent.emit(value = GENERIC_ERROR)
            }
        }
    }

    fun deleteAllNotes() {
        viewModelScope.launch {
            try {
                _uiState.value = NoteListUiState.Loading
                firestoreRepository.deleteAllNotes()
                _snackBarEvent.emit(value = ALL_NOTES_DELETED_MSG)
            } catch (ce: CancellationException) {
                throw ce
            } catch (ex: FirebaseFirestoreException) {
                val errorMsg = getFirestoreError(firestoreException = ex)
                _snackBarEvent.emit(value = errorMsg)
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.value = NoteListUiState.Error(message = GENERIC_ERROR)
            }
        }
    }
}

sealed class NoteListUiState {
    object Loading : NoteListUiState()
    data class Success(val noteList: List<Note>) : NoteListUiState()
    data class Error(val message: String) : NoteListUiState()
}