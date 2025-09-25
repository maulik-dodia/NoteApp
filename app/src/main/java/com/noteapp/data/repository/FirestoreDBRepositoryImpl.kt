package com.noteapp.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.toObject
import com.noteapp.domain.model.Note
import com.noteapp.util.NoteConstant.NOTES
import com.noteapp.util.NoteConstant.TITLE
import com.noteapp.util.NoteConstant.USER_ID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreDBRepositoryImpl @Inject constructor(private val firestore: FirebaseFirestore): FirestoreDBRepository {

    private val noteCollection = firestore.collection(NOTES)

    override suspend fun searchNoteByTitle(userId: String?, query: String, limit: Long): Flow<List<Note>> {
        return flow {
            var ref: Query = noteCollection
            /*
            userId is to identify whose notes we are fetching,
            if given null, it will fetch all the notes irrespective of user.
            Pass userId to avoid any user conflicts
            */
            if (!userId.isNullOrEmpty()) {
                ref = ref.whereEqualTo(USER_ID, userId)
            }
            val ordered = ref.orderBy(TITLE)
            val snap = if (query.isBlank()) {
                ordered.limit(limit).get().await()
            } else {
                val end = query + '\uf8ff'
                ordered.startAt(query).endAt(end).limit(limit).get().await()
            }
            val list = snap.documents.mapNotNull {
                it.toObject(Note::class.java)?.copy(id = it.id)
            }
            emit(value = list)
        }
    }

    override suspend fun insertNote(note: Note): String {
        val document = noteCollection.document()
        val noteWithId = note.copy(id = document.id)
        document.set(noteWithId).await()
        return document.id
    }

    override suspend fun getNoteById(id: String): Note? {
        return noteCollection.document(id).get().await().toObject<Note>()
    }

    override suspend fun updateNote(note: Note) {
        require(note.id.isNotEmpty())
        noteCollection.document(note.id).set(note).await()
    }

    override suspend fun deleteNoteById(id: String) {
        noteCollection.document(id).delete().await()
    }

    override suspend fun deleteAllNotes() {
        val snapshot = noteCollection.get().await()
        val batch = firestore.batch()
        snapshot.documents.forEach { batch.delete(it.reference) }
        batch.commit().await()
    }
}