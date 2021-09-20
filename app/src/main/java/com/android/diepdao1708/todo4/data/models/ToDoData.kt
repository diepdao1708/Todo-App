package com.android.diepdao1708.todo4.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "todo_table")
@Parcelize
data class ToDoData (
    @PrimaryKey(autoGenerate = true)
    var todo_id: Int,
    var todo_title: String,
    var todo_description: String,
    var todo_time: String, // hh:mm dd/MM/yyyy
    var todo_timeInMillis: Long, // Calendar.timeInMillis
    var todo_reminder: Boolean,
    var todo_garbage: Boolean
) : Parcelable