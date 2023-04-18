package com.example.rightplace.architecture

import com.example.rightplace.database.AppDatabase
import com.example.rightplace.model.Document
import com.example.rightplace.model.DocumentType
import kotlinx.coroutines.flow.Flow

class AppRepository(
    private val appDatabase: AppDatabase
    ) {

    suspend fun insertDocument(document: Document){
        appDatabase.documentDao().insertAll(document)

    }
    suspend fun deleteDocument(document: Document){
        appDatabase.documentDao().delete(document)

    }
    fun getAllDocuments(): Flow<List<Document>>{
        return appDatabase.documentDao().getAll()

    }
}