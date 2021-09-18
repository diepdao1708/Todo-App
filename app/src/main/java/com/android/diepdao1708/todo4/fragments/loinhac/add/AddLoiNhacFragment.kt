package com.android.diepdao1708.todo4.fragments.loinhac.add

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.diepdao1708.todo4.R
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.data.viewmodel.ToDoViewModel
import com.android.diepdao1708.todo4.databinding.FragmentAddLoiNhacBinding
import com.android.diepdao1708.todo4.fragments.SharedViewModel
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddLoiNhacFragment : Fragment() {

    private lateinit var binding : FragmentAddLoiNhacBinding
    private val toDoViewModel: ToDoViewModel by viewModels<ToDoViewModel>()
    private val sharedViewModel: SharedViewModel by viewModels<SharedViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddLoiNhacBinding.inflate(inflater, container, false)

        binding.switchReminderLoinhac.setOnCheckedChangeListener { buttonView, isChecked ->
            if(isChecked) {
                binding.setTextViewTimeLoinhac.visibility = View.VISIBLE
                binding.setTextViewDateLoinhac.visibility = View.VISIBLE

                val currentDateTime = LocalDateTime.now()
                binding.setTextViewTimeLoinhac.setText(currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm")))

                binding.setTextViewDateLoinhac.setText(currentDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))

                binding.setTextViewTimeLoinhac.setOnClickListener {
                    val cal = Calendar.getInstance()
                    val time = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        cal.set(Calendar.MINUTE, minute)
                        binding.setTextViewTimeLoinhac.text = SimpleDateFormat("HH:mm").format(cal.time)
                    }
                    TimePickerDialog(context, time, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                }

                binding.setTextViewDateLoinhac.setOnClickListener {
                    val cal = Calendar.getInstance()
                    val date = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                        cal.set(Calendar.YEAR, year)
                        cal.set(Calendar.MONTH, month)
                        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                        binding.setTextViewDateLoinhac.text = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(cal.getTime())
                    }
                    context?.let { it1 -> DatePickerDialog(it1, date, cal.get(Calendar.YEAR), cal.get(
                        Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show() }
                }

            } else{
                binding.setTextViewTimeLoinhac.visibility = View.INVISIBLE
                binding.setTextViewDateLoinhac.visibility = View.INVISIBLE
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
        val title = binding.editTextTitleLoinhac.text.toString()
        val description = binding.editTextDescriptionLoinhac.text.toString()
        val validation = sharedViewModel.verifDataFromUser(description)
        if (validation){

            val newData = ToDoData(
                0,
                title,
                description,
                binding.setTextViewTimeLoinhac.text.toString(),
                binding.setTextViewDateLoinhac.text.toString(),
                binding.switchReminderLoinhac.isChecked,
                false
            )
            Log.d("switch", newData.todo_reminder.toString())
            toDoViewModel.insertData(newData)
            Toast.makeText(requireContext(), "Thêm lời nhắc thành công!", Toast.LENGTH_LONG).show()

            // Navigate back
            findNavController().navigate(R.id.action_addLoiNhacFragment_to_loiNhacFragment)

        } else {
            Toast.makeText(requireContext(), "Vui lòng nhập đủ!", Toast.LENGTH_LONG).show()
        }
    }
}