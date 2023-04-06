package com.example.rightplace.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.rightplace.database.model.DocumentType

@Dao
interface DocumentTypeDao {
    @Query("SELECT * FROM documentType")
    suspend fun getAll(): List<DocumentType>

//    @Query("SELECT * FROM documentType WHERE id IN (:documentTypes)")
//    fun loadAllByIds(documentTypes: IntArray): List<DocumentType>
//
//    @Query("SELECT * FROM  documentType WHERE name LIKE :name ")
//    fun findByName(name: String): DocumentType

    @Insert
    fun insertAll(document_type: DocumentType)

    @Delete
    fun delete(document_type: DocumentType)
}