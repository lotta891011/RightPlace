package com.example.rightplace.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.rightplace.database.model.Documents

@Dao
interface DocumentsDao {
    @Query("SELECT * FROM documents")
    suspend fun getAll(): List<Documents>

    @Insert
    fun insertAll(documents: Documents)

    @Delete
    fun delete(documents: Documents)
}