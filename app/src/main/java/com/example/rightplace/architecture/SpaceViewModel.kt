package com.example.rightplace.architecture

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rightplace.database.AppDatabase
import com.example.rightplace.model.Space
import kotlinx.coroutines.launch

class SpaceViewModel : ViewModel(){
    private lateinit var repository : AppRepository
    val spaceLiveData = MutableLiveData<List<Space>>()

    private val transactionCompleteLiveData = MutableLiveData<Boolean>()
    fun init(appDatabase: AppDatabase){
        repository = AppRepository(appDatabase)

        viewModelScope.launch {

            repository.getAllSpaces().collect{ items ->
                spaceLiveData.postValue(items)
            }

        }

    }
    fun insertSpace(space: Space){
        viewModelScope.launch {
            repository.insertSpace(space)

            transactionCompleteLiveData.postValue(true)
        }


    }
    fun deleteSpace(space: Space){
        viewModelScope.launch {
            repository.deleteSpace(space)
        }


    }
    fun updateSpace(space: Space){
        viewModelScope.launch {
            repository.updateSpace(space)

            transactionCompleteLiveData.postValue(true)
        }


    }
}