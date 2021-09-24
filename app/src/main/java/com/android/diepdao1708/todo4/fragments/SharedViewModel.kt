package com.android.diepdao1708.todo4.fragments

import android.app.Application
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.android.diepdao1708.todo4.data.models.ToDoData

class SharedViewModel(application: Application) : AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(false)


    fun checkIfDatabaseEmpty(toDoData: List<ToDoData>){
        emptyDatabase.value = toDoData.isEmpty()
    }

    fun checkIfDataEmpty(toDoData: List<String>){
        emptyDatabase.value = toDoData.isEmpty()
    }

    fun verifDataFromUser(desciption: String) : Boolean {
        return if(TextUtils.isEmpty(desciption)){
            return false
        } else !(desciption.isEmpty())
    }
}