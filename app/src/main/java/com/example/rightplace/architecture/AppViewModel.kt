package com.example.rightplace.architecture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rightplace.database.AppDatabase
import com.example.rightplace.database.model.DocumentTypes
import kotlinx.coroutines.launch

class AppViewModel : ViewModel(){
    private lateinit var repository : AppRepository
    val documentTypesLiveData = MutableLiveData<List<DocumentTypes>>()

    fun init(appDatabase: AppDatabase){
        repository = AppRepository(appDatabase)

        viewModelScope.launch {
            val items = repository.getAll()
            documentTypesLiveData.postValue(items)
        }

    }
    fun insert(documentTypes: DocumentTypes){
        repository.insert(documentTypes)

    }
    fun delete(documentTypes: DocumentTypes){
        repository.delete(documentTypes)

    }
}