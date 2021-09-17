package com.android.diepdao1708.todo4.fragments.loinhac

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.android.diepdao1708.todo4.data.viewmodel.ToDoViewModel
import com.android.diepdao1708.todo4.databinding.FragmentLoiNhacBinding
import com.android.diepdao1708.todo4.fragments.SharedViewModel
import com.android.diepdao1708.todo4.fragments.loinhac.adapter.LoiNhacAdapter
import com.android.diepdao1708.todo4.utils.hideKeyboard

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

//        binding.floatingActionButtonLoiNhac.setOnClickListener {
//            findNavController().navigate(R.id.action_loiNhacFragment_to_addFragment)
//        }

        //Hide keyboard
        hideKeyboard(requireActivity())

        return binding.root
    }

    private fun setUpRecyclerView() {
        val recyclerView = binding.recyclerViewLoiNhac
        recyclerView.adapter = adapter
        recyclerView.layoutManager =
            StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)

    }

}