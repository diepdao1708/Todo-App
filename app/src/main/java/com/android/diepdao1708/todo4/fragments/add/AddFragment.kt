package com.android.diepdao1708.todo4.fragments.add

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.diepdao1708.todo4.R
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.data.viewmodel.ToDoViewModel
import com.android.diepdao1708.todo4.databinding.FragmentAddBinding
import com.android.diepdao1708.todo4.fragments.SharedViewModel



class AddFragment : Fragment() {

    private lateinit var binding : FragmentAddBinding
    private val toDoViewModel: ToDoViewModel by viewModels<ToDoViewModel>()
    private val sharedViewModel: SharedViewModel by viewModels<SharedViewModel>()


    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentAddBinding.inflate(inflater, container, false)

        Log.d("switch", binding.switchReminder.isChecked.toString())

        binding.switchReminder.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                binding.setTextViewTime.visibility = VISIBLE
                binding.setTextViewDate.visibility = VISIBLE
            } else{
                binding.setTextViewTime.visibility = INVISIBLE
                binding.setTextViewDate.visibility = INVISIBLE
            }
        }
        //set menu
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save){
            insertDataToDatabase()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun insertDataToDatabase() {
        val title = binding.editTextTitle.text.toString()
        val description = binding.editTextDescription.text.toString()
        val validation = sharedViewModel.verifDataFromUser(description)



        if (validation){

            val newData = ToDoData(
                0,
                title,
                description,
                "",
                "",
                false,
                false
            )

            toDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Thêm ghi chú thành công!", Toast.LENGTH_LONG).show()

            // Navigate back
            findNavController().navigate(R.id.action_addFragment_to_ghiChuFragment)

        } else {
            Toast.makeText(requireContext(), "Vui lòng nhập đủ!", Toast.LENGTH_LONG).show()
        }
    }
}