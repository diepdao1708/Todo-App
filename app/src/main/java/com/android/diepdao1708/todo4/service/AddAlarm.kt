package com.android.diepdao1708.todo4.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.android.diepdao1708.todo4.broadcastReceiver.AlarmReceiver
import com.android.diepdao1708.todo4.data.models.ToDoData
import com.android.diepdao1708.todo4.utils.Constants

class AddAlarm (private val context: Context){

    private val alarmManager: AlarmManager =
        context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun cancelAlarm(reCode: Int){
        val intent = getIntent().apply { action = Constants.ACTION_SET_EXACT }
        val pendingIntent = PendingIntent.getBroadcast(context, reCode, intent, 0)
        alarmManager.cancel(pendingIntent)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    fun setExactAlarm(timeInMillis: Long, todoData: ToDoData){ // gửi todoData -> AlarmReceiver -> AlarmService -> ReminderActivity
        setAlarm(
            todoData.todo_timeInMillis,
            getPendingIntent(
                getIntent().apply {
                    action = Constants.ACTION_SET_EXACT
                    putExtra(Constants.TITLE, todoData.todo_title)
                    putExtra(Constants.DESCRIPTION, todoData.todo_description)
                    putExtra(Constants.REQUESTCODE, todoData.todo_RequestCode.toString())
                    Log.e("time_addAlarm", timeInMillis.toString())
                }, todoData
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent) {
        alarmManager.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            } else {
                alarmManager.setExact(
                    AlarmManager.RTC_WAKEUP,
                    timeInMillis,
                    pendingIntent
                )
            }
        }
    }

    private fun getPendingIntent(intent: Intent, todoData: ToDoData) =
        PendingIntent.getBroadcast(
            context,
            todoData.todo_RequestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

    private fun getIntent() = Intent(context, AlarmReceiver::class.java)

}