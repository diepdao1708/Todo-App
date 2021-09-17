package com.android.diepdao1708.todo4.fragments.loinhac

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.android.diepdao1708.todo4.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar

class LoiNhacFragment : Fragment() {

    private lateinit var binding: FragmentLoiNhacBinding
    private val adapter: LoiNhacAdapter by lazy { LoiNhacAdapter() }
    private val toDoViewModel: ToDoViewModel by viewModels<ToDoViewModel>()
    private val sharedViewModel: SharedViewModel by viewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentLoiNhacBinding.inflate(inflater, container, false)
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
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.loinhacList[viewHolder.adapterPosition]

                deletedItem.todo_garbage = true
                toDoViewModel.updateData(deletedItem)
                adapter.notifyItemRemoved(viewHolder.adapterPosition)

                //Restore Deleted Item
                restoreDeletedData(viewHolder.itemView, deletedItem)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    private fun restoreDeletedData(view: View, deletedItem: ToDoData) {
        val snackbar = Snackbar.make(
            view, "Đã chuyển vào thùng rác!",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Hoàn tác") {
            deletedItem.todo_garbage = false
            toDoViewModel.updateData(deletedItem)
        }
        snackbar.show()
    }
}