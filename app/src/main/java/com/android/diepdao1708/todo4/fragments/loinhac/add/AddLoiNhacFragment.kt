package com.android.diepdao1708.todo4.fragments.loinhac.add

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
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
import com.android.diepdao1708.todo4.service.AddAlarm
import com.android.diepdao1708.todo4.utils.RandomUtil
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddLoiNhacFragment : Fragment() {

    private lateinit var binding : FragmentAddLoiNhacBinding
    private val toDoViewModel: ToDoViewModel by viewModels<ToDoViewModel>()
    private val sharedViewModel: SharedViewModel by viewModels<SharedViewModel>()
    lateinit var alarmService: AddAlarm
    private var time: Long = 0
    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("WrongConstant", "UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddLoiNhacBinding.inflate(inflater, container, false)
        alarmService = AddAlarm(context!!)
        binding.switchReminderLoinhac.setOnCheckedChangeListener { _ , isChecked ->
            if(isChecked) {
                binding.setTextViewTimeLoinhac.visibility = View.VISIBLE

                val currentDateTime = LocalDateTime.now()
                binding.setTextViewTimeLoinhac.setText(currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy")))

                binding.setTextViewTimeLoinhac.setOnClickListener {
                    val cal = Calendar.getInstance()
                    val time = TimePickerDialog.OnTimeSetListener { _ , hourOfDay, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        cal.set(Calendar.MINUTE, minute)
                        val date = DatePickerDialog.OnDateSetListener { _ , year, month, dayOfMonth ->
                            cal.set(Calendar.YEAR, year)
                            cal.set(Calendar.MONTH, month)
                            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            Log.e("time", cal.timeInMillis.toString())
                            time = cal.timeInMillis
                            binding.setTextViewTimeLoinhac.text = DateFormat.format("hh:mm, dd/MM/yyyy", cal.timeInMillis).toString()
                        }
                        context?.let { it1 -> DatePickerDialog(it1, date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show() }

                    }
                    TimePickerDialog(context, time, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                }

            } else{
                binding.setTextViewTimeLoinhac.visibility = View.INVISIBLE
            }
        }

        //set menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_menu, menu)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_save){
            insertDataToDatabase()
        }

        return super.onOptionsItemSelected(item)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
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
                RandomUtil.getRandomInt(),
                binding.switchReminderLoinhac.isChecked,
                false,
                time
            )
            toDoViewModel.insertData(newData)
            if (binding.switchReminderLoinhac.isChecked) alarmService.setExactAlarm(time, newData)
            Toast.makeText(requireContext(), "Thêm lời nhắc thành công!", Toast.LENGTH_LONG).show()

            // Navigate back
            findNavController().navigate(R.id.action_addLoiNhacFragment_to_loiNhacFragment)

        } else {
            Toast.makeText(requireContext(), "Vui lòng nhập đủ!", Toast.LENGTH_LONG).show()
        }
    }
}