package com.android.diepdao1708.todo4.fragments.add

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
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
import java.text.SimpleDateFormat
import java.util.*


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

        binding.switchReminder.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                binding.setTextViewTime.visibility = VISIBLE
                binding.setTextViewDate.visibility = VISIBLE

                binding.setTextViewTime.setOnClickListener {
                    val cal = Calendar.getInstance()
                    val time = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        cal.set(Calendar.MINUTE, minute)
                        binding.setTextViewTime.text = SimpleDateFormat("HH:mm").format(cal.time)
                    }
                    TimePickerDialog(context, time, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                }

                binding.setTextViewDate.setOnClickListener {
                    val cal = Calendar.getInstance()
                    val date = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, month)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        binding.setTextViewDate.text = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(cal.getTime())
                    }
                    context?.let { it1 -> DatePickerDialog(it1, date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show() }
                }

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
                binding.setTextViewTime.text.toString(),
                binding.setTextViewDate.text.toString(),
                binding.switchReminder.isChecked,
                false
            )
            Log.d("switch", newData.todo_reminder.toString())
            toDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Thêm ghi chú thành công!", Toast.LENGTH_LONG).show()

            // Navigate back
            findNavController().navigate(R.id.action_addFragment_to_ghiChuFragment)

        } else {
            Toast.makeText(requireContext(), "Vui lòng nhập đủ!", Toast.LENGTH_LONG).show()
        }
    }
}