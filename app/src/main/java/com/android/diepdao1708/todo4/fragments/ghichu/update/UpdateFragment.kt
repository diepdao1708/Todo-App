package com.android.diepdao1708.todo4.fragments.ghichu.update

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.diepdao1708.todo4.R
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.data.viewmodel.ToDoViewModel
import com.android.diepdao1708.todo4.databinding.FragmentUpdateGhiChuBinding
import com.android.diepdao1708.todo4.fragments.SharedViewModel
import java.text.SimpleDateFormat
import java.util.*


class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateGhiChuBinding

    private val toDoViewModel: ToDoViewModel by viewModels<ToDoViewModel>()
    private val sharedViewModel: SharedViewModel by viewModels<SharedViewModel>()

    // https://developer.android.com/guide/navigation/navigation-pass-data
    private val args by navArgs<UpdateFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUpdateGhiChuBinding.inflate(inflater, container, false)
        binding.editTextUpdateTitle.setText(args.currentItem.todo_title)
        binding.editTextUpdateDescription.setText(args.currentItem.todo_description)

        // nếu đang reminder == true thì hiện time & date
        binding.switchReminderUpdate.setChecked(args.currentItem.todo_reminder)
        if(args.currentItem.todo_reminder){
            binding.setTextViewTimeUpdate.visibility = View.VISIBLE
            binding.setTextViewDateUpdate.visibility = View.VISIBLE
            binding.setTextViewTimeUpdate.setText(args.currentItem.todo_time)
            binding.setTextViewDateUpdate.setText(args.currentItem.todo_date)
        } else {
            binding.setTextViewTimeUpdate.visibility = View.INVISIBLE
            binding.setTextViewDateUpdate.visibility = View.INVISIBLE
        }

        // set sự kiện reminder
        binding.switchReminderUpdate.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                binding.setTextViewTimeUpdate.visibility = View.VISIBLE
                binding.setTextViewDateUpdate.visibility = View.VISIBLE

                binding.setTextViewTimeUpdate.setOnClickListener {
                    val cal = Calendar.getInstance()
                    val time = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        cal.set(Calendar.MINUTE, minute)
                        binding.setTextViewTimeUpdate.text = SimpleDateFormat("HH:mm").format(cal.time)
                    }
                    TimePickerDialog(context, time, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                }

                binding.setTextViewDateUpdate.setOnClickListener {
                    val cal = Calendar.getInstance()
                    val date = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, month)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        binding.setTextViewDateUpdate.text = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(cal.getTime())
                    }
                    context?.let { it1 -> DatePickerDialog(it1, date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show() }
                }

            } else{
                binding.setTextViewTimeUpdate.visibility = View.INVISIBLE
                binding.setTextViewDateUpdate.visibility = View.INVISIBLE
            }
        }

        //set menu
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

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

    private fun updateData(){
        val title = binding.editTextUpdateTitle.text.toString()
        val description = binding.editTextUpdateDescription.text.toString()

        val validation = sharedViewModel.verifDataFromUser(description)
        if(validation) {
            val updateData = ToDoData(
                args.currentItem.todo_id,
                title,
                description,
                binding.setTextViewTimeUpdate.text.toString(),
                binding.setTextViewDateUpdate.text.toString(),
                binding.switchReminderUpdate.isChecked,
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

    private fun deleteData(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Có"){  _, _ ->
            val deletedData = ToDoData(
                args.currentItem.todo_id,
                args.currentItem.todo_title,
                args.currentItem.todo_description,
                args.currentItem.todo_time,
                args.currentItem.todo_date,
                args.currentItem.todo_reminder,
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

