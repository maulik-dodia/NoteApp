package com.noteapp.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.noteapp.model.Name
import kotlinx.coroutines.flow.Flow

@Dao
interface NameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(name: Name)

    @Query("SELECT * FROM Name ORDER BY first_name ASC")
    fun getNames(): Flow<List<Name>>
}