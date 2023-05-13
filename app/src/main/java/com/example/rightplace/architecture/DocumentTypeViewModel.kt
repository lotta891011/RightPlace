package com.example.rightplace.architecture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rightplace.database.AppDatabase
import com.example.rightplace.model.Document
import com.example.rightplace.model.DocumentType
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DocumentTypeViewModel : ViewModel(){
    private lateinit var repository : AppRepository
    val documentTypeLiveData = MutableLiveData<List<DocumentType>>()

    val transactionCompleteLiveData = MutableLiveData<Boolean>()
    fun init(appDatabase: AppDatabase){
        repository = AppRepository(appDatabase)

        viewModelScope.launch {

            val documentTypes = repository.getAllDocumentTypes().collect{ items ->
                documentTypeLiveData.postValue(items)
            }

        }

    }
    fun insertDocumentType(documentType: DocumentType){
        viewModelScope.launch {
            repository.insertDocumentType(documentType)

            transactionCompleteLiveData.postValue(true)
        }


    }
    fun deleteDocumentType(documentType: DocumentType){
        viewModelScope.launch {
            repository.deleteDocumentType(documentType)
        }


    }
    fun updateDocumentType(documentType: DocumentType){
        viewModelScope.launch {
            repository.updateDocumentType(documentType)

            transactionCompleteLiveData.postValue(true)
        }


    }
}