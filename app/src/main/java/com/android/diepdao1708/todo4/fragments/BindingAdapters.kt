package com.android.diepdao1708.todo4.fragments

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.fragments.ghichu.GhiChuFragmentDirections

class BindingAdapters {
    companion object {
        @BindingAdapter("android:emptyDatabase")
        @JvmStatic
        fun emptyDatabase(view: View, emptyDatabase: MutableLiveData<Boolean>) {
            when (emptyDatabase.value) {
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("android:reminder")
        @JvmStatic
        fun reminder(view: View, reminder: Boolean) {
            when (reminder) {
                true -> view.visibility = View.VISIBLE
                false -> view.visibility = View.INVISIBLE
            }
        }

        @BindingAdapter("android:sendDataToUpdateFragment")
        @JvmStatic
        fun sendDataToUpdateFragment(view: ConstraintLayout, currentItem: ToDoData){
            view.setOnClickListener {
                val action = GhiChuFragmentDirections.actionGhiChuFragmentToUpdateFragment(currentItem)
                view.findNavController().navigate(action)
            }
        }


    }
}