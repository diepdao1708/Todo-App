package com.android.diepdao1708.todo4.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.format.DateFormat
import com.android.diepdao1708.todo4.service.AlarmService
import com.android.diepdao1708.todo4.utils.Constants

// https://developer.android.com/guide/components/broadcasts
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        val timeInMillis = intent.getLongExtra(Constants.EXTRA_EXACT_ALARM_TIME, 0L)
        when (intent.action) {
            Constants.ACTION_SET_EXACT -> {
                startAlarmService(context, intent)
            }

        }
    }

    private fun startAlarmService(context: Context, intent: Intent){
        var intentService = Intent(context, AlarmService::class.java)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(intentService)
        }
        else{
            context.startService(intentService)
        }
    }

    private fun convertDate(timeInMillis: Long): String =
        DateFormat.format("dd/MM/yyyy hh:mm:ss", timeInMillis).toString()

}