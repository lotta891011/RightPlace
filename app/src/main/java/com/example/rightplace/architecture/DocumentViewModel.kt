package com.example.rightplace.architecture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rightplace.database.AppDatabase
import com.example.rightplace.model.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DocumentViewModel : ViewModel(){
    private lateinit var repository : AppRepository
    val documentLiveData = MutableLiveData<List<Document>?>()

    val transactionCompleteLiveData = MutableLiveData<Boolean>()
    fun init(appDatabase: AppDatabase){
        repository = AppRepository(appDatabase)


    }

    val liveData : MutableLiveData<List<Document>?>
        get() = documentLiveData

    suspend fun getFilteredUser(filter: String): List<Document> {
        return withContext(Dispatchers.IO) {
            repository.getFiltered(filter)
        }
    }

    // to set the filterquery from the fragment/activity
    fun setFilterQuery(query: String) {
        viewModelScope.launch {
            documentLiveData.postValue(getFilteredUser(query))
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