package com.android.diepdao1708.todo4.fragments.ghichu.add

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.*
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.android.diepdao1708.todo4.R
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.data.viewmodel.ToDoViewModel
import com.android.diepdao1708.todo4.databinding.FragmentAddGhiChuBinding
import com.android.diepdao1708.todo4.fragments.SharedViewModel
import com.android.diepdao1708.todo4.service.AddAlarm
import com.android.diepdao1708.todo4.utils.RandomUtil
import kotlinx.android.synthetic.main.fragment_add_ghi_chu.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class AddFragment : Fragment() {

    private lateinit var binding : FragmentAddGhiChuBinding
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

        binding = FragmentAddGhiChuBinding.inflate(inflater, container, false)
        alarmService = AddAlarm(context!!)
        time = Calendar.getInstance().timeInMillis
        binding.switchReminder.setOnCheckedChangeListener { _ , isChecked ->
            if(isChecked) {
                binding.setTextViewTime.visibility = VISIBLE
                val currentDateTime = LocalDateTime.now()
                binding.setTextViewTime.setText(currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy")))

                binding.setTextViewTime.setOnClickListener  {
                    val cal = Calendar.getInstance()
                    val time = TimePickerDialog.OnTimeSetListener { _ , hourOfDay, minute ->
                        cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                        cal.set(Calendar.MINUTE, minute)
                        val date = DatePickerDialog.OnDateSetListener { _ , year, month, dayOfMonth ->
                            cal.set(Calendar.YEAR, year)
                            cal.set(Calendar.MONTH, month)
                            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                            time = cal.timeInMillis
                            binding.setTextViewTime.text = DateFormat.format("HH:mm, dd/MM/yyyy", cal.timeInMillis).toString()
                        }
                        context?.let { it1 -> DatePickerDialog(it1, date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show() }
                    }
                    TimePickerDialog(context, time, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                }

            } else{
                binding.setTextViewTime.visibility = INVISIBLE
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
        val title = binding.editTextTitle.text.toString()
        val description = binding.editTextDescription.text.toString()
        val validation = sharedViewModel.verifDataFromUser(description)
        if (validation){

            val newData = ToDoData(
                0,
                title,
                description,
                binding.setTextViewTime.text.toString(),
                RandomUtil.getRandomInt(),
                binding.switchReminder.isChecked,
                false,
                time
            )
            toDoViewModel.insertData(newData)
            if (binding.switchReminder.isChecked) alarmService.setExactAlarm(time, newData)
            Log.e("time_addFragment", newData.todo_timeInMillis.toString())
            Toast.makeText(requireContext(), "Thêm ghi chú thành công!", Toast.LENGTH_LONG).show()

            // Navigate back
            findNavController().navigate(R.id.action_addFragment_to_ghiChuFragment)

        } else {
            Toast.makeText(requireContext(), "Vui lòng nhập đủ!", Toast.LENGTH_LONG).show()
        }
    }
}