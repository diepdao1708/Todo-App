package com.android.diepdao1708.todo4.fragments.add

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.diepdao1708.todo4.R
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.data.viewmodel.ToDoViewModel
import com.android.diepdao1708.todo4.databinding.FragmentAddBinding
import com.android.diepdao1708.todo4.fragments.SharedViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class AddFragment : Fragment() {

    private lateinit var binding : FragmentAddBinding
    private val toDoViewModel: ToDoViewModel by viewModels<ToDoViewModel>()
    private val sharedViewModel: SharedViewModel by viewModels<SharedViewModel>()

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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save){
            insertDataToDatabase()
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertDataToDatabase() {
        val title = binding.editTextTitle.text.toString()
        val description = binding.editTextDescription.text.toString()
        val currentDateTime = LocalDateTime.now()

        val validation = sharedViewModel.verifDataFromUser(title, description)
        if (validation){
            val newData = ToDoData(
                0,
                title,
                description,
                currentDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                currentDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)),
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