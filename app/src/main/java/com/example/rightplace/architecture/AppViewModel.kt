package com.example.rightplace.architecture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rightplace.database.AppDatabase
import com.example.rightplace.model.Document
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AppViewModel : ViewModel(){
    private lateinit var repository : AppRepository
    val documentLiveData = MutableLiveData<List<Document>>()

    val transactionCompleteLiveData = MutableLiveData<Boolean>()
    fun init(appDatabase: AppDatabase){
        repository = AppRepository(appDatabase)

        viewModelScope.launch {
            val items = repository.getAllDocuments().collect{ items ->
                documentLiveData.postValue(items)
            }

        }

    }
    fun insertDocument(document: Document){
        viewModelScope.launch {
            repository.insertDocument(document)

            transactionCompleteLiveData.postValue(true)
        }


    }
    fun deleteDocument(document: Document){
        viewModelScope.launch {
            repository.deleteDocument(document)
        }


    }
    fun updateDocument(document: Document){
        viewModelScope.launch {
            repository.updateDocument(document)

            transactionCompleteLiveData.postValue(true)
        }


    }
}