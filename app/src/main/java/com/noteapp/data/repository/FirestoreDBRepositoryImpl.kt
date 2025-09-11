package com.noteapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.firestore.toObject
import com.noteapp.domain.model.Note
import com.noteapp.util.NoteConstant.NOTES
import com.noteapp.util.NoteConstant.TIMESTAMP
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreDBRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore): FirestoreDBRepository {

    private val noteCollection = firestore.collection(NOTES)

    override suspend fun observeAllNotes(): Flow<List<Note>> {
        return noteCollection
            .orderBy(TIMESTAMP)
            .snapshots()
            .map { querySnapshot ->
                querySnapshot.documents.mapNotNull { it.toObject<Note>() }
            }
    }

    override suspend fun insertNote(note: Note): String {
        return try {
            val document = noteCollection.document()
            val noteWithId = note.copy(id = document.id)
            document.set(noteWithId).await()
            document.id
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getNoteById(id: String): Note? {
        return noteCollection.document(id).get().await().toObject<Note>()
    }

    override suspend fun updateNote(note: Note) {
        require(note.id.isNotEmpty())
        noteCollection.document(note.id).set(note).await()
    }

    override suspend fun deleteNote(id: String) {
        noteCollection.document(id).delete().await()
    }

    override suspend fun deleteAllNotes() {
        val snapshot = noteCollection.get().await()
        val batch = firestore.batch()
        snapshot.documents.forEach { batch.delete(it.reference) }
        batch.commit().await()
    }
}