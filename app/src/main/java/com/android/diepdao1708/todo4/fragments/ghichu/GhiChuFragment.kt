package com.android.diepdao1708.todo4.fragments.ghichu

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.android.diepdao1708.todo4.R
import com.android.diepdao1708.todo4.databinding.FragmentGhiChuBinding
import kotlinx.android.synthetic.main.activity_main.*


class GhiChuFragment : Fragment() {

    private lateinit var binding: FragmentGhiChuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGhiChuBinding.inflate(inflater, container, false)

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_ghiChuFragment_to_addFragment)
        }


        //set menu
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.ghi_chu_menu, menu)
    }


}