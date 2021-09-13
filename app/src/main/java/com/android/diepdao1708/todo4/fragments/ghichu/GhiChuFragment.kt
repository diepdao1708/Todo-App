package com.android.diepdao1708.todo4.fragments.ghichu

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.diepdao1708.todo4.R
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.data.viewmodel.ToDoViewModel
import com.android.diepdao1708.todo4.databinding.FragmentGhiChuBinding
import com.android.diepdao1708.todo4.fragments.SharedViewModel
import com.android.diepdao1708.todo4.fragments.ghichu.adapter.GhiChuAdapter
import kotlinx.android.synthetic.main.activity_main.*


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

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_ghiChuFragment_to_addFragment)
        }

        //Setup recyclerview
        setUpRecyclerView()

        //Observe LiveData
        toDoViewModel.getAllData.observe(viewLifecycleOwner, Observer { data ->
//            sharedViewModel.checkIfDatabaseEmpty(data)
            adapter.setData(data)
        })

        //set menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.ghi_chu_menu, menu)
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerView
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

    }
}