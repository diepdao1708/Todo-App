package com.android.diepdao1708.todo4.fragments.loinhac

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.diepdao1708.todo4.R
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.data.viewmodel.ToDoViewModel
import com.android.diepdao1708.todo4.databinding.FragmentLoiNhacBinding
import com.android.diepdao1708.todo4.fragments.SharedViewModel
import com.android.diepdao1708.todo4.fragments.SwipeToDelete
import com.android.diepdao1708.todo4.fragments.loinhac.adapter.LoiNhacAdapter
import com.android.diepdao1708.todo4.service.AddAlarm
import com.android.diepdao1708.todo4.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar
import java.util.*

class LoiNhacFragment : Fragment() {

    private lateinit var binding: FragmentLoiNhacBinding
    private val adapter: LoiNhacAdapter by lazy { LoiNhacAdapter() }
    private val toDoViewModel: ToDoViewModel by viewModels<ToDoViewModel>()
    private val sharedViewModel: SharedViewModel by viewModels<SharedViewModel>()
    lateinit var alarmService: AddAlarm

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoiNhacBinding.inflate(inflater, container, false)
        alarmService = AddAlarm(context!!)
        binding.lifecycleOwner = this
        sharedViewModel.also { binding.sharedViewModel = it }

        //Setup recyclerview
        setUpRecyclerView()

        //Observe LiveData
        toDoViewModel.getLoiNhacData.observe(viewLifecycleOwner, Observer { data ->
            sharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        binding.floatingActionButtonLoiNhac.setOnClickListener {
            findNavController().navigate(R.id.action_loiNhacFragment_to_addLoiNhacFragment)
        }

        //Hide keyboard
        hideKeyboard(requireActivity())

        return binding.root
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerViewLoiNhac
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        //Swipe to Delete
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.loinhacList[viewHolder.adapterPosition]

                deletedItem.todo_garbage = true
                if (deletedItem.todo_reminder) alarmService.cancelAlarm(deletedItem.todo_RequestCode)
                toDoViewModel.updateData(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                //Restore Deleted Item
                restoreDeletedData(viewHolder.itemView, deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun restoreDeletedData(view: View, deletedItem: ToDoData) {
        val snackbar = Snackbar.make(
            view, "Đã chuyển vào thùng rác!",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Hoàn tác") {
            deletedItem.todo_garbage = false
            if (deletedItem.todo_reminder && Calendar.getInstance().timeInMillis <= deletedItem.todo_timeInMillis) alarmService.setExactAlarm(deletedItem.todo_timeInMillis, deletedItem)
            toDoViewModel.updateData(deletedItem)
        }
        snackbar.show()
    }
}