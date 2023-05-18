package com.example.rightplace.database.dao

import androidx.room.*
import com.example.rightplace.model.DocumentType
import kotlinx.coroutines.flow.Flow

@Dao
interface DocumentTypeDao {
    @Query("SELECT * FROM documentType")
    fun getAll(): Flow<List<DocumentType>>

    @Insert
    suspend fun insertAll(document_type: DocumentType)

    @Delete
    suspend fun delete(document_type: DocumentType)

    @Update
    suspend fun update(document_type: DocumentType)
}