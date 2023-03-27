package com.example.rightplace.architecture

import com.example.rightplace.database.AppDatabase
import com.example.rightplace.database.model.DocumentTypes

class AppRepository(
    private val appDatabase: AppDatabase
    ) {

    fun insert(documentTypes: DocumentTypes){
        appDatabase.documentTypeDao().insertAll(documentTypes)

    }
    fun delete(documentTypes: DocumentTypes){
        appDatabase.documentTypeDao().delete(documentTypes)

    }
    suspend fun getAll(): List<DocumentTypes>{
        return appDatabase.documentTypeDao().getAll()

    }
}