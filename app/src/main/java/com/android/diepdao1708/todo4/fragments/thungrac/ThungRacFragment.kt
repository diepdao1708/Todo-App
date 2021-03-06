package com.android.diepdao1708.todo4.fragments.thungrac

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.diepdao1708.todo4.R
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.data.viewmodel.ToDoViewModel
import com.android.diepdao1708.todo4.databinding.FragmentThungRacBinding
import com.android.diepdao1708.todo4.fragments.SharedViewModel
import com.android.diepdao1708.todo4.fragments.SwipeToDelete
import com.android.diepdao1708.todo4.fragments.SwipeToDeleteRight
import com.android.diepdao1708.todo4.fragments.thungrac.adapter.ThungRacAdapter
import com.android.diepdao1708.todo4.service.AddAlarm
import com.google.android.material.snackbar.Snackbar
import java.util.*


class ThungRacFragment : Fragment() {

    private lateinit var binding: FragmentThungRacBinding
    private val adapter: ThungRacAdapter by lazy { ThungRacAdapter() }
    private val toDoViewModel: ToDoViewModel by viewModels<ToDoViewModel>()
    private val sharedViewModel: SharedViewModel by viewModels<SharedViewModel>()
    lateinit var alarmService: AddAlarm

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentThungRacBinding.inflate(inflater, container, false)
        alarmService = AddAlarm(context!!)
        binding.lifecycleOwner = this
        sharedViewModel.also { binding.sharedViewModel = it }

        //Setup recyclerview
        setUpRecyclerView()

        //Observe LiveData
        toDoViewModel.getThungRacData.observe(viewLifecycleOwner, Observer { data ->
            sharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        //set menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.thung_rac_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_delete -> {
                deleteAllData()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerViewDelete
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

        // X??a
        swipeToDelete(recyclerView)

        // ????a v??o ghi ch??
        swipeToDeleteRight(recyclerView)
    }

    // X??a
    private fun swipeToDelete(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDelete(){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.thungracList[viewHolder.adapterPosition]
                val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialog)
                builder.setPositiveButton("C??"){  _, _ ->
                    toDoViewModel.deleteData(deletedItem)
                    Toast.makeText(requireContext(), "X??a th??nh c??ng!", Toast.LENGTH_SHORT).show()
                    adapter.notifyItemRemoved(viewHolder.adapterPosition)
                }
                builder.setNegativeButton("Kh??ng") { _, _ ->
                    adapter.notifyDataSetChanged()
                }
                builder.setTitle("B???n c?? ch???c ch???n mu???n x??a kh??ng?")
                builder.create().show()

            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }

    // ????a t??? th??ng r??c v??o ghi ch??
    private fun swipeToDeleteRight(recyclerView: RecyclerView){
        val swipeToDeleteCallback = object : SwipeToDeleteRight(){
            @RequiresApi(Build.VERSION_CODES.KITKAT)
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val deletedItem = adapter.thungracList[viewHolder.adapterPosition]

                deletedItem.todo_garbage = false
                toDoViewModel.updateData(deletedItem)
                if (deletedItem.todo_reminder && Calendar.getInstance().timeInMillis <= deletedItem.todo_timeInMillis) alarmService.setExactAlarm(deletedItem.todo_timeInMillis, deletedItem)
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
            view, "???? chuy???n v??o ghi ch??!",
            Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Ho??n t??c") {
            deletedItem.todo_garbage = true
            if (deletedItem.todo_reminder) alarmService.cancelAlarm(deletedItem.todo_RequestCode)
            toDoViewModel.updateData(deletedItem)
        }
        snackbar.show()
    }

    private fun deleteAllData(){
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialog)
        builder.setPositiveButton("C??"){  _, _ ->
            toDoViewModel.deleteAllData()
            Toast.makeText(requireContext(), "X??a th??nh c??ng!", Toast.LENGTH_SHORT).show()
        }
        builder.setNegativeButton("Kh??ng") { _, _ ->
            adapter.notifyDataSetChanged()
        }
        builder.setTitle("B???n c?? ch???c ch???n mu???n x??a t???t c??? kh??ng?")
        builder.create().show()
    }

}