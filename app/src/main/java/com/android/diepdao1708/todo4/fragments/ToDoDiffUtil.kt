package com.android.diepdao1708.todo4.fragments

import androidx.recyclerview.widget.DiffUtil
import com.android.diepdao1708.todo4.data.models.ToDoData

class ToDoDiffUtil(
    private val oldList: List<ToDoData>,
    private val newList: List<ToDoData>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].todo_id == newList[newItemPosition].todo_id
                && oldList[oldItemPosition].todo_title == newList[newItemPosition].todo_time
                && oldList[oldItemPosition].todo_description == newList[newItemPosition].todo_description
                && oldList[oldItemPosition].todo_time == newList[newItemPosition].todo_time
                && oldList[oldItemPosition].todo_timeInMillis == newList[newItemPosition].todo_timeInMillis
                && oldList[oldItemPosition].todo_time == newList[newItemPosition].todo_time
                && oldList[oldItemPosition].todo_reminder == newList[newItemPosition].todo_reminder
                && oldList[oldItemPosition].todo_garbage == newList[newItemPosition].todo_garbage
    }

}