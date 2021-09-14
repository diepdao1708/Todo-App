package com.android.diepdao1708.todo4.data.repository

import androidx.lifecycle.LiveData
import com.android.diepdao1708.todo4.data.ToDoDAO
import com.android.diepdao1708.todo4.data.models.ToDoData

class ToDoRepository(private val toDoDAO: ToDoDAO) {
    val getAllData: LiveData<List<ToDoData>> = toDoDAO.getAllData()

    val getGhiChuData: LiveData<List<ToDoData>> = toDoDAO.getGhiChuData()

    suspend fun insertData(toDoData: ToDoData){
        toDoDAO.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData){
        toDoDAO.updateData(toDoData)
    }

    suspend fun deleteData(toDoData: ToDoData) {
        toDoDAO.deleteData(toDoData)
    }

    suspend fun deleteAllData() {
        toDoDAO.deleteAllData()
    }

    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>> {
        return toDoDAO.searchDatabase(searchQuery)
    }
}