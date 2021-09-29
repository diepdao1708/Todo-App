package com.android.diepdao1708.todo4.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.android.diepdao1708.todo4.databinding.ActivityReminderBinding
import com.android.diepdao1708.todo4.service.AlarmService
import com.android.diepdao1708.todo4.utils.Constants
import kotlinx.android.synthetic.main.activity_reminder.*

class ReminderActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReminderBinding
    private var handlerAnimation = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        startPulse()
        val intent = Intent(this, AlarmService::class.java)
        binding.activityRingDismiss.setOnClickListener {
            this.stopService(intent)
            finish()
        }

    }

    private fun startPulse(){
        runnable.run()
    }

    private var runnable = object : Runnable {
        override fun run() {

            imgAnimation1.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(1000)
                .withEndAction {
                    imgAnimation1.scaleX = 1f
                    imgAnimation1.scaleY = 1f
                    imgAnimation1.alpha = 1f
                }

            imgAnimation2.animate().scaleX(4f).scaleY(4f).alpha(0f).setDuration(700)
                .withEndAction {
                    imgAnimation2.scaleX = 1f
                    imgAnimation2.scaleY = 1f
                    imgAnimation2.alpha = 1f
                }

            handlerAnimation.postDelayed(this, 1000)
        }
    }
}