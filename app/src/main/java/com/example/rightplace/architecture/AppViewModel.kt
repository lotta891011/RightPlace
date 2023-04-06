package com.example.rightplace.architecture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rightplace.database.AppDatabase
import com.example.rightplace.database.model.DocumentType
import kotlinx.coroutines.launch

class AppViewModel : ViewModel(){
    private lateinit var repository : AppRepository
    val documentTypeLiveData = MutableLiveData<List<DocumentType>>()

    fun init(appDatabase: AppDatabase){
        repository = AppRepository(appDatabase)

        viewModelScope.launch {
            val items = repository.getAll()
            documentTypeLiveData.postValue(items)
        }

    }
    fun insert(documentType: DocumentType){
        repository.insert(documentType)

    }
    fun delete(documentType: DocumentType){
        repository.delete(documentType)

    }
}