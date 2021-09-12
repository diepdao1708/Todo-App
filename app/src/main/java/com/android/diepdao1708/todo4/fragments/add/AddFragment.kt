package com.android.diepdao1708.todo4.fragments.add

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.android.diepdao1708.todo4.R
import com.android.diepdao1708.todo4.databinding.FragmentAddBinding


class AddFragment : Fragment() {

    private lateinit var binding : FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddBinding.inflate(inflater, container, false)


        //set menu
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
    }


}