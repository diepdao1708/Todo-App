package com.android.diepdao1708.todo4.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.diepdao1708.todo4.databinding.ActivityReminderBinding
import com.android.diepdao1708.todo4.service.AlarmService

class ReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReminderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.activityRingDismiss.setOnClickListener {
            val intent = Intent(this, AlarmService::class.java)
            this.stopService(intent)
            finish()
        }

    }
}