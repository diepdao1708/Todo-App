package com.android.diepdao1708.todo4.service

import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.android.diepdao1708.todo4.R
import com.android.diepdao1708.todo4.activity.App.Companion.CHANNEL_ID
import com.android.diepdao1708.todo4.activity.ReminderActivity
import com.android.diepdao1708.todo4.utils.Constants

class AlarmService : Service(){

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val notiIntent = Intent(this, ReminderActivity::class.java)
        val title = intent.getStringExtra(Constants.TITLE)
        val description = intent.getStringExtra(Constants.DESCRIPTION)
        val pendingIntent = PendingIntent.getActivity(this,0,notiIntent,0)
        val notification = NotificationCompat.Builder(this,CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(description)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()
        startForeground(1,notification)
        return START_STICKY

    }

}