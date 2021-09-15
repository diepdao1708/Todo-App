package com.android.diepdao1708.todo4.fragments.update

import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.diepdao1708.todo4.R
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.data.viewmodel.ToDoViewModel
import com.android.diepdao1708.todo4.databinding.FragmentUpdateBinding
import com.android.diepdao1708.todo4.fragments.SharedViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateBinding

    private val toDoViewModel: ToDoViewModel by viewModels<ToDoViewModel>()
    private val sharedViewModel: SharedViewModel by viewModels<SharedViewModel>()

    // https://developer.android.com/guide/navigation/navigation-pass-data
    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUpdateBinding.inflate(inflater, container, false)
        binding.editTextUpdateTitle.setText(args.currentItem.todo_title)
        binding.editTextUpdateDescription.setText(args.currentItem.todo_description)



        //set menu
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_update_save -> {
                updateData()
            }
            R.id.menu_update_delete -> {
                deleteData()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateData(){
        val title = binding.editTextUpdateTitle.text.toString()
        val description = binding.editTextUpdateDescription.text.toString()
        val currentDateTime = LocalDateTime.now()

        val validation = sharedViewModel.verifDataFromUser(title, description)
        if(validation) {
            val updateData = ToDoData(
                args.currentItem.todo_id,
                title,
                description,
                currentDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
                currentDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)),
                false,
                false
            )
            toDoViewModel.updateData(updateData)
            Toast.makeText(requireContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_updateFragment_to_ghiChuFragment)
        } else {
            Toast.makeText(requireContext(), "Vui lòng nhập đủ!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun deleteData(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Có"){  _, _ ->
            val currentDateTime = LocalDateTime.now()
            val deletedData = ToDoData(
                args.currentItem.todo_id,
                args.currentItem.todo_title,
                args.currentItem.todo_description,
                args.currentItem.todo_time,
                args.currentItem.todo_date,
//                currentDateTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)),
//                currentDateTime.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)),
                false,
                true
            )
            toDoViewModel.updateData(deletedData)
            Toast.makeText(requireContext(), "Đã chuyển vào thùng rác!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_ghiChuFragment)
        }
        builder.setNegativeButton("Không") { _, _ -> }
        builder.setTitle("Bạn có chắc chắn muốn xóa không?")
        builder.create().show()
    }
}

