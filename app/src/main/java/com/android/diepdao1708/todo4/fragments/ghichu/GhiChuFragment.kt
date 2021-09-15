package com.android.diepdao1708.todo4.fragments.ghichu

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.diepdao1708.todo4.R
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.data.viewmodel.ToDoViewModel
import com.android.diepdao1708.todo4.databinding.FragmentGhiChuBinding
import com.android.diepdao1708.todo4.fragments.SharedViewModel
import com.android.diepdao1708.todo4.fragments.SwipeToDelete
import com.android.diepdao1708.todo4.fragments.ghichu.adapter.GhiChuAdapter
import com.android.diepdao1708.todo4.utils.hideKeyboard
import com.google.android.material.snackbar.Snackbar


class GhiChuFragment : Fragment() {

    private lateinit var binding: FragmentGhiChuBinding

    private val adapter: GhiChuAdapter by lazy { GhiChuAdapter() }
    private val toDoViewModel: ToDoViewModel by viewModels<ToDoViewModel>()
    private val sharedViewModel: SharedViewModel by viewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGhiChuBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        sharedViewModel.also { binding.sharedViewModel = it }

        //Setup recyclerview
        setUpRecyclerView()

        //Observe LiveData
        toDoViewModel.getGhiChuData.observe(viewLifecycleOwner, Observer { data ->
            sharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_ghiChuFragment_to_addFragment)
        }

        //set menu
        setHasOptionsMenu(true)

        //Hide keyboard
        hideKeyboard(requireActivity())

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.ghi_chu_menu, menu)
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        //Swipe to Delete
        swipeToDelete(recyclerView)
    }

    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.ghichuList[viewHolder.adapterPosition]

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