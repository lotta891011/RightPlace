package com.example.rightplace.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.rightplace.database.model.Document

@Dao
interface DocumentDao {
    @Query("SELECT * FROM document")
    suspend fun getAll(): List<Document>

    @Insert
    fun insertAll(document: Document)

    @Delete
    fun delete(document: Document)
}