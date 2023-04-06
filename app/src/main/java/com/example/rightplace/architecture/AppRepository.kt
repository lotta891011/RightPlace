package com.example.rightplace.architecture

import com.example.rightplace.database.AppDatabase
import com.example.rightplace.database.model.DocumentType

class AppRepository(
    private val appDatabase: AppDatabase
    ) {

    fun insert(documentType: DocumentType){
        appDatabase.documentTypeDao().insertAll(documentType)

    }
    fun delete(documentType: DocumentType){
        appDatabase.documentTypeDao().delete(documentType)

    }
    suspend fun getAll(): List<DocumentType>{
        return appDatabase.documentTypeDao().getAll()

    }
}