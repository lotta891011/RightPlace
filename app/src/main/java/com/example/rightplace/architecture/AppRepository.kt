package com.example.rightplace.architecture

import com.example.rightplace.database.AppDatabase
import com.example.rightplace.model.Document
import com.example.rightplace.model.DocumentType
import com.example.rightplace.model.Space
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class AppRepository(
    private val appDatabase: AppDatabase
    ) {

    suspend fun insertDocument(document: Document){
        appDatabase.documentDao().insertAll(document)

    }
    suspend fun deleteDocument(document: Document){
        appDatabase.documentDao().delete(document)

    }

    suspend fun updateDocument(document: Document){
        appDatabase.documentDao().update(document)

    }
    fun getAllDocuments(): Flow<List<Document>>{
        return appDatabase.documentDao().getAll()

    }

    fun getAllSpaces(): Flow<List<Space>>{
        return appDatabase.spaceDao().getAll()

    }

    suspend fun insertSpace(space: Space){
        appDatabase.spaceDao().insertAll(space)

    }
    suspend fun deleteSpace(space: Space){
        appDatabase.spaceDao().delete(space)

    }

    suspend fun updateSpace(space: Space){
        appDatabase.spaceDao().update(space)

    }

    suspend fun getFiltered(filter : String): List<Document>{
        return withContext(Dispatchers.IO) {
            appDatabase.documentDao().getFiltered(filter)
        }
    }

    suspend fun insertDocumentType(documentType: DocumentType){
        appDatabase.documentTypeDao().insertAll(documentType)

    }
    suspend fun deleteDocumentType(documentType: DocumentType){
        appDatabase.documentTypeDao().delete(documentType)

    }

    suspend fun updateDocumentType(documentType: DocumentType){
        appDatabase.documentTypeDao().update(documentType)

    }
    fun getAllDocumentTypes(): Flow<List<DocumentType>>{
        return appDatabase.documentTypeDao().getAll()

    }

}