package com.example.text

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import java.util.Calendar

class ReminderManager : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder_manager)

        // ✅ 一定要先宣告
        val prefs = getSharedPreferences("DailyCheckInPrefs", MODE_PRIVATE)

        val tvReminderTime = findViewById<TextView>(R.id.tvReminderTime)
        val btnSetReminderTime = findViewById<Button>(R.id.btnSetReminderTime)
        val btnConfirmReminder = findViewById<Button>(R.id.btnConfirmReminder)

        btnSetReminderTime.setOnClickListener {

            val now = Calendar.getInstance()

            val dialog = TimePickerDialog(
                this,
                { _: TimePicker, hour: Int, minute: Int ->

                    // 顯示在本頁
                    tvReminderTime.text = "提醒時間：%02d:%02d".format(hour, minute)

                    // ✅ 存進 SharedPreferences
                    prefs.edit()
                        .putInt("reminderHour", hour)
                        .putInt("reminderMinute", minute)
                        .apply()
                },
                now.get(Calendar.HOUR_OF_DAY),
                now.get(Calendar.MINUTE),
                true
            )

            dialog.show()
        }

        btnConfirmReminder.setOnClickListener {
            startActivity(Intent(this, DashboardScreen::class.java))
            finish()
        }
    }
}
