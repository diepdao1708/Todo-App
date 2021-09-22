package com.android.diepdao1708.todo4.fragments.loinhac.update

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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.android.diepdao1708.todo4.R
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.data.viewmodel.ToDoViewModel
import com.android.diepdao1708.todo4.databinding.FragmentUpdateLoiNhacBinding
import com.android.diepdao1708.todo4.fragments.SharedViewModel
import com.android.diepdao1708.todo4.service.AddAlarm
import com.android.diepdao1708.todo4.utils.RandomUtil
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class UpdateLoiNhacFragment : Fragment() {

    private lateinit var binding: FragmentUpdateLoiNhacBinding
    private val toDoViewModel: ToDoViewModel by viewModels<ToDoViewModel>()
    private val sharedViewModel: SharedViewModel by viewModels<SharedViewModel>()
    private var time: Long = 0
    lateinit var alarmService: AddAlarm
    private val args by navArgs<UpdateLoiNhacFragmentArgs>()

    @SuppressLint("UseRequireInsteadOfGet")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUpdateLoiNhacBinding.inflate(inflater, container, false)
        alarmService = AddAlarm(context!!)
        binding.editTextUpdateTitleLoiNhac.setText(args.currentItemLoiNhac.todo_title)
        binding.editTextUpdateDescriptionLoiNhac.setText(args.currentItemLoiNhac.todo_description)

        // nếu đang reminder == true thì hiện time & date
        binding.switchReminderUpdateLoiNhac.setChecked(args.currentItemLoiNhac.todo_reminder)
        if(args.currentItemLoiNhac.todo_reminder){
            binding.setTextViewTimeUpdateLoiNhac.visibility = View.VISIBLE
            binding.setTextViewTimeUpdateLoiNhac.setText(args.currentItemLoiNhac.todo_time)
            binding.setTextViewTimeUpdateLoiNhac.setOnClickListener {
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
                        binding.setTextViewTimeUpdateLoiNhac.text = DateFormat.format("hh:mm, dd/MM/yyyy", cal.timeInMillis).toString()
                    }
                    context?.let { it1 -> DatePickerDialog(it1, date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show() }
                }
                TimePickerDialog(context, time, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }
        } else {
            binding.setTextViewTimeUpdateLoiNhac.visibility = View.INVISIBLE
        }

        // set sự kiện reminder
        binding.switchReminderUpdateLoiNhac.setOnCheckedChangeListener { _ , isChecked ->
            if(isChecked) {
                binding.setTextViewTimeUpdateLoiNhac.visibility = View.VISIBLE

                val currentDateTime = LocalDateTime.now()
                binding.setTextViewTimeUpdateLoiNhac.setText(currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy")))

                binding.setTextViewTimeUpdateLoiNhac.setOnClickListener {
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
                            binding.setTextViewTimeUpdateLoiNhac.text = DateFormat.format("hh:mm, dd/MM/yyyy", cal.timeInMillis).toString()
                        }
                        context?.let { it1 -> DatePickerDialog(it1, date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show() }

                    }
                    TimePickerDialog(context, time, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                }


            } else{
                binding.setTextViewTimeUpdateLoiNhac.visibility = View.INVISIBLE
            }
        }

        //set menu
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.update_menu, menu)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
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

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun updateData(){
        val title = binding.editTextUpdateTitleLoiNhac.text.toString()
        val description = binding.editTextUpdateDescriptionLoiNhac.text.toString()

        if (args.currentItemLoiNhac.todo_reminder && !binding.switchReminderUpdateLoiNhac.isChecked){
            alarmService.cancelAlarm(args.currentItemLoiNhac.todo_RequestCode)
        }

        if (binding.switchReminderUpdateLoiNhac.isChecked && binding.setTextViewTimeUpdateLoiNhac.text.toString() != args.currentItemLoiNhac.todo_time){
            alarmService.cancelAlarm(args.currentItemLoiNhac.todo_RequestCode)
        }

        val validation = sharedViewModel.verifDataFromUser(description)
        if(validation) {
            val updateData = ToDoData(
                args.currentItemLoiNhac.todo_id,
                title,
                description,
                binding.setTextViewTimeUpdateLoiNhac.text.toString(),
                RandomUtil.getRandomInt(),
                binding.switchReminderUpdateLoiNhac.isChecked,
                false,
                time
            )
            toDoViewModel.updateData(updateData)
            if (binding.switchReminderUpdateLoiNhac.isChecked) alarmService.setExactAlarm(time, updateData)
            Toast.makeText(requireContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show()

            findNavController().navigate(R.id.action_updateLoiNhacFragment_to_loiNhacFragment)
        } else {
            Toast.makeText(requireContext(), "Vui lòng nhập đủ!", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun deleteData(){
        val builder = AlertDialog.Builder(requireContext())
        builder.setPositiveButton("Có"){  _, _ ->
            val deletedData = ToDoData(
                args.currentItemLoiNhac.todo_id,
                args.currentItemLoiNhac.todo_title,
                args.currentItemLoiNhac.todo_description,
                args.currentItemLoiNhac.todo_time,
                args.currentItemLoiNhac.todo_RequestCode,
                args.currentItemLoiNhac.todo_reminder,
                true,
                args.currentItemLoiNhac.todo_timeInMillis
            )
            toDoViewModel.updateData(deletedData)
            if (args.currentItemLoiNhac.todo_reminder) alarmService.cancelAlarm(args.currentItemLoiNhac.todo_RequestCode)
            Toast.makeText(requireContext(), "Đã chuyển vào thùng rác!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateLoiNhacFragment_to_loiNhacFragment)
        }
        builder.setNegativeButton("Không") { _, _ -> }
        builder.setTitle("Bạn có chắc chắn muốn xóa không?")
        builder.create().show()
    }
}