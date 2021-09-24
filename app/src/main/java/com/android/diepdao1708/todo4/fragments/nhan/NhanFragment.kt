package com.android.diepdao1708.todo4.fragments.nhan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.diepdao1708.todo4.R
import com.android.diepdao1708.todo4.data.viewmodel.ToDoViewModel
import com.android.diepdao1708.todo4.databinding.FragmentNhanBinding
import com.android.diepdao1708.todo4.fragments.SharedViewModel
import com.android.diepdao1708.todo4.fragments.nhan.adapter.NhanAdapter

class NhanFragment : Fragment(), NhanAdapter.OnItemClickListener {

    private lateinit var binding: FragmentNhanBinding
    private val toDoViewModel: ToDoViewModel by viewModels<ToDoViewModel>()
    private val adapter: NhanAdapter by lazy { NhanAdapter(this) }
    private val sharedViewModel: SharedViewModel by viewModels<SharedViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentNhanBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        sharedViewModel.also { binding.sharedViewModel = it }

        //Setup recyclerview
        setUpRecyclerView()

        //Observe LiveData
        toDoViewModel.getNhanData.observe(viewLifecycleOwner, Observer { data ->
            sharedViewModel.checkIfDataEmpty(data)
            adapter.setData(data)
        })

        return binding.root
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerViewNhan
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
    }

    override fun onItemClick(position: Int, title: String) {
        val action = NhanFragmentDirections.actionNhanFragmentToItemNhanFragment(title)
        findNavController().navigate(action)
    }

}