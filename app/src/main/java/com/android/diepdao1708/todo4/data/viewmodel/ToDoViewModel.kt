package com.android.diepdao1708.todo4.data.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.android.diepdao1708.todo4.data.ToDoDatabase
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel (application: Application) : AndroidViewModel(application){
    private val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()
    private val repository : ToDoRepository = ToDoRepository(toDoDao)

    val getGhiChuData: LiveData<List<ToDoData>> = repository.getGhiChuData

    val getThungRacData: LiveData<List<ToDoData>> = repository.getThungRacData

    val getLoiNhacData: LiveData<List<ToDoData>> = repository.getLoiNhacData

    val getNhanData: LiveData<List<String>> = repository.getNhanData

    fun getItemNhanData(key: String) : LiveData<List<ToDoData>> = repository.getItemNhanData(key)

    fun insertData(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertData(toDoData)
        }
    }

    fun updateData(toDoData: ToDoData){
        viewModelScope.launch(Dispatchers.IO){
            repository.updateData(toDoData)
        }
    }

    fun deleteData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteData(toDoData)
        }
    }

    fun deleteAllData() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllData()
        }
    }

    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>> {
        return repository.searchDatabase(searchQuery)
    }

}