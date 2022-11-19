package com.noteapp.repository

import androidx.annotation.WorkerThread
import com.noteapp.dao.NameDao
import com.noteapp.model.Name
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class NameRepository(private val nameDao: NameDao) {

    val allNames: Flow<List<Name>> = nameDao.getNames()

    @WorkerThread
    fun insert(name: Name) {
        CoroutineScope(Dispatchers.IO).launch {
            nameDao.insert(name)
        }
    }
}