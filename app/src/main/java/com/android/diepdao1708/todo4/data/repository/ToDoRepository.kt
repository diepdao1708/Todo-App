package com.android.diepdao1708.todo4.data.repository

import androidx.lifecycle.LiveData
import com.android.diepdao1708.todo4.data.ToDoDAO
import com.android.diepdao1708.todo4.data.models.ToDoData

class ToDoRepository(private val toDoDAO: ToDoDAO) {

    val getGhiChuData: LiveData<List<ToDoData>> = toDoDAO.getGhiChuData()

    val getThungRacData: LiveData<List<ToDoData>> = toDoDAO.getThungRacData()

    val getLoiNhacData: LiveData<List<ToDoData>> = toDoDAO.getLoiNhacData()

    val getNhanData: LiveData<List<String>> = toDoDAO.getNhanData("")

    fun getItemNhanData(key: String) : LiveData<List<ToDoData>> = toDoDAO.getItemNhanData(key)

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