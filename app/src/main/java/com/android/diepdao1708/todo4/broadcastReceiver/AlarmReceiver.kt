package com.android.diepdao1708.todo4.broadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.text.format.DateFormat
import android.util.Log
import com.android.diepdao1708.todo4.service.AlarmService
import com.android.diepdao1708.todo4.utils.Constants

// https://developer.android.com/guide/components/broadcasts
class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        when (intent.action) {
            Constants.ACTION_SET_EXACT -> {
                startAlarmService(context, intent)
            }

        }
    }

    private fun startAlarmService(context: Context, intent: Intent){
        var intentService = Intent(context, AlarmService::class.java)
        intentService.putExtra(Constants.TITLE, intent.getStringExtra(Constants.TITLE))
        intentService.putExtra(Constants.DESCRIPTION, intent.getStringExtra(Constants.DESCRIPTION))
        intentService.putExtra(Constants.REQUESTCODE, intent.getStringExtra(Constants.REQUESTCODE))
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            context.startForegroundService(intentService)
        }
        else{
            context.startService(intentService)
        }
    }

}