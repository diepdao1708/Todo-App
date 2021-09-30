package com.android.diepdao1708.todo4.notification

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import timber.log.Timber

class App : Application() {

    companion object{
        const val CHANNEL_ID = "ALARM CHANNEL"
    }
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        // phai co khi ung dung chay android 8 tro len
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, "Alarm Channel", NotificationManager.IMPORTANCE_HIGH)
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
            val attribute = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            channel.setSound(uri, attribute)
            val notificationManager: NotificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}