package com.android.diepdao1708.todo4.fragments.ghichu.update

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
import com.android.diepdao1708.todo4.databinding.FragmentUpdateGhiChuBinding
import com.android.diepdao1708.todo4.fragments.SharedViewModel
import com.android.diepdao1708.todo4.service.AddAlarm
import com.android.diepdao1708.todo4.utils.RandomUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class UpdateFragment : Fragment() {

    private lateinit var binding: FragmentUpdateGhiChuBinding
    private val toDoViewModel: ToDoViewModel by viewModels<ToDoViewModel>()
    private val sharedViewModel: SharedViewModel by viewModels<SharedViewModel>()
    private var time: Long = 0
    lateinit var alarmService: AddAlarm
    // https://developer.android.com/guide/navigation/navigation-pass-data
    private val args by navArgs<UpdateFragmentArgs>()

    @SuppressLint("UseRequireInsteadOfGet")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentUpdateGhiChuBinding.inflate(inflater, container, false)
        alarmService = AddAlarm(context!!)
        binding.editTextUpdateTitle.setText(args.currentItem.todo_title)
        binding.editTextUpdateDescription.setText(args.currentItem.todo_description)

        // nếu đang reminder == true thì hiện time & date
        binding.switchReminderUpdate.setChecked(args.currentItem.todo_reminder)
        if(args.currentItem.todo_reminder){
            binding.setTextViewTimeUpdate.visibility = View.VISIBLE
            binding.setTextViewTimeUpdate.setText(args.currentItem.todo_time)
            binding.setTextViewTimeUpdate.setOnClickListener {
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
                        binding.setTextViewTimeUpdate.text = DateFormat.format("hh:mm, dd/MM/yyyy", cal.timeInMillis).toString()
                    }
                    context?.let { it1 -> DatePickerDialog(it1, date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show() }
                }
                TimePickerDialog(context, time, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }
        } else {
            binding.setTextViewTimeUpdate.visibility = View.INVISIBLE
        }

        // set sự kiện reminder
        binding.switchReminderUpdate.setOnCheckedChangeListener { _ , isChecked ->
            if(isChecked) {
                binding.setTextViewTimeUpdate.visibility = View.VISIBLE

                val currentDateTime = LocalDateTime.now()
                binding.setTextViewTimeUpdate.setText(currentDateTime.format(DateTimeFormatter.ofPattern("HH:mm, dd/MM/yyyy")))

                binding.setTextViewTimeUpdate.setOnClickListener {
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
                            binding.setTextViewTimeUpdate.text = DateFormat.format("hh:mm, dd/MM/yyyy", cal.timeInMillis).toString()
                        }
                        context?.let { it1 -> DatePickerDialog(it1, date, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show() }

                    }
                    TimePickerDialog(context, time, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                }

            } else{
                binding.setTextViewTimeUpdate.visibility = View.INVISIBLE
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
        val title = binding.editTextUpdateTitle.text.toString()
        val description = binding.editTextUpdateDescription.text.toString()
        if ( args.currentItem.todo_reminder && !binding.switchReminderUpdate.isChecked) {
            alarmService.cancelAlarm(args.currentItem.todo_RequestCode)
        }
        if (binding.switchReminderUpdate.isChecked && binding.setTextViewTimeUpdate.text.toString() != args.currentItem.todo_time){
            alarmService.cancelAlarm(args.currentItem.todo_RequestCode)
        }

        val validation = sharedViewModel.verifDataFromUser(description)
        if(validation) {
            val updateData = ToDoData(
                args.currentItem.todo_id,
                title,
                description,
                binding.setTextViewTimeUpdate.text.toString(),
                RandomUtil.getRandomInt(),
                binding.switchReminderUpdate.isChecked,
                false,
                time
            )
            toDoViewModel.updateData(updateData)
            if (binding.switchReminderUpdate.isChecked) alarmService.setExactAlarm(time, updateData)
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
                args.currentItem.todo_RequestCode,
                args.currentItem.todo_reminder,
                true,
                args.currentItem.todo_timeInMillis
            )
            toDoViewModel.updateData(deletedData)
            if (args.currentItem.todo_reminder) alarmService.cancelAlarm(args.currentItem.todo_RequestCode)
            Toast.makeText(requireContext(), "Đã chuyển vào thùng rác!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_updateFragment_to_ghiChuFragment)
        }
        builder.setNegativeButton("Không") { _, _ -> }
        builder.setTitle("Bạn có chắc chắn muốn xóa không?")
        builder.create().show()
    }
}

