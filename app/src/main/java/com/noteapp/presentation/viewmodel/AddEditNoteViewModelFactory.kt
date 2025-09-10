package com.noteapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.noteapp.data.repository.FirestoreDBRepository
import com.noteapp.data.repository.RoomDBRepository

class AddEditNoteViewModelFactory(
    private val noteId: Int?,
    private val roomRepository: RoomDBRepository,
    private val firestoreRepository: FirestoreDBRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddEditNoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddEditNoteViewModel(
                noteId = noteId,
                roomRepository = roomRepository,
                firestoreRepository = firestoreRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}