package com.example.rightplace.database.dao

import androidx.room.*
import com.example.rightplace.model.Document
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentDao {
    @Query("SELECT * FROM document")
    fun getAll(): Flow<List<Document>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(document: Document)

    @Update
    suspend fun update(document: Document)

    @Delete
    suspend fun delete(document: Document)
}