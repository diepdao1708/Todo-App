package com.android.diepdao1708.todo4.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.android.diepdao1708.todo4.data.models.ToDoData

@Dao
interface ToDoDAO {

//    @Query("SELECT * FROM todo_table ORDER BY todo_id ASC")
//    fun getAllData(): LiveData<List<ToDoData>>

    @Query("SELECT * FROM todo_table WHERE todo_garbage == 0")
    fun getGhiChuData(): LiveData<List<ToDoData>>

    @Query("SELECT * FROM todo_table WHERE todo_garbage == 1")
    fun getThungRacData(): LiveData<List<ToDoData>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(toDoData: ToDoData)

    @Update
    suspend fun updateData(toDoData: ToDoData)

    @Delete
    suspend fun deleteData(toDoData: ToDoData)

    @Query("DELETE FROM todo_table")
    suspend fun deleteAllData()

    @Query("SELECT * FROM todo_table WHERE todo_title LIKE :searchQuery")
    fun searchDatabase(searchQuery: String): LiveData<List<ToDoData>>
}