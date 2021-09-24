package com.android.diepdao1708.todo4.fragments.nhan

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.data.viewmodel.ToDoViewModel
import com.android.diepdao1708.todo4.databinding.FragmentItemNhanBinding
import com.android.diepdao1708.todo4.fragments.SharedViewModel
import com.android.diepdao1708.todo4.fragments.SwipeToDelete
import com.android.diepdao1708.todo4.fragments.nhan.adapter.ItemAdapter
import com.google.android.material.snackbar.Snackbar

class ItemNhanFragment : Fragment() {

    private lateinit var binding: FragmentItemNhanBinding
    private val adapter: ItemAdapter by lazy { ItemAdapter() }
    private val toDoViewModel: ToDoViewModel by viewModels<ToDoViewModel>()
    private val args by navArgs<ItemNhanFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemNhanBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        //Setup recyclerview
        setUpRecyclerView()

        toDoViewModel.getItemNhanData(args.currentItemTitle).observe(viewLifecycleOwner, Observer { data ->
            adapter.setData(data)
        })

        return binding.root
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerViewItem
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        //Swipe to Delete
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.itemList[viewHolder.adapterPosition]

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