package com.example.rightplace.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.rightplace.database.model.DocumentTypes

@Dao
interface DocumentTypesDao {
    @Query("SELECT * FROM documentTypes")
    suspend fun getAll(): List<DocumentTypes>

//    @Query("SELECT * FROM documentType WHERE id IN (:documentTypes)")
//    fun loadAllByIds(documentTypes: IntArray): List<DocumentType>
//
//    @Query("SELECT * FROM  documentType WHERE name LIKE :name ")
//    fun findByName(name: String): DocumentType

    @Insert
    fun insertAll(document_type: DocumentTypes)

    @Delete
    fun delete(document_type: DocumentTypes)
}