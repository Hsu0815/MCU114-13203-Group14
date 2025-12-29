package com.example.text

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class DashboardScreen : AppCompatActivity() {

    // ✅ 全域變數（避免紅線）
    private lateinit var layoutGoals: LinearLayout
    private lateinit var tvProgress: TextView
    private lateinit var sharedPrefs: android.content.SharedPreferences

    private val goals = mutableListOf<String>()
    private var checkedCount = 0

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard_screen)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        sharedPrefs = getSharedPreferences("DailyCheckInPrefs", MODE_PRIVATE)

        val etDailyGoal = findViewById<EditText>(R.id.etDailyGoal)
        val btnSaveGoal = findViewById<Button>(R.id.btnSaveGoal)
        val btnCheckIn = findViewById<Button>(R.id.btnCheckIn)
        val btnHabit = findViewById<Button>(R.id.btnhabit)
        val btnHistory = findViewById<Button>(R.id.btnCheckInHistory)
        val tvReminderTime = findViewById<TextView>(R.id.tvReminderTime)
        updateReminderTimeText(tvReminderTime)


        // ✅ 新增：前往提醒設定
        val btnReminderSettings = findViewById<Button>(R.id.btnSetReminderTime)
        btnReminderSettings.setOnClickListener {
            startActivity(Intent(this, ReminderManager::class.java))
        }

        tvProgress = findViewById(R.id.tvProgress)
        layoutGoals = findViewById(R.id.layoutGoals)

        btnHabit.setOnClickListener {
            startActivity(Intent(this, HabitsScreen::class.java))
        }

        btnHistory.setOnClickListener {
            startActivity(Intent(this, CheckInHistoryScreen::class.java))
        }

        btnSaveGoal.setOnClickListener {
            val goalText = etDailyGoal.text.toString().trim()
            if (goalText.isNotEmpty()) {
                addGoal(goalText)
                etDailyGoal.text.clear()
            }
        }

        // ✅ 今日打卡：不要重複存
        btnCheckIn.setOnClickListener {
            handleCheckIn()
        }
    }

    // -------------------------------
    // 🎯 新增目標
    // -------------------------------
    private fun addGoal(goalText: String) {
        goals.add(goalText)

        val checkBox = CheckBox(this)
        checkBox.text = goalText
        checkBox.textSize = 16f

        checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) checkedCount++ else checkedCount--
            updateProgress()
        }

        layoutGoals.addView(checkBox)
        updateProgress()
    }

    // -------------------------------
    // 📊 更新進度
    // -------------------------------
    private fun updateProgress() {
        tvProgress.text = "目前進度：$checkedCount / ${goals.size}"
    }

    // -------------------------------
    // ✅ 今日打卡處理
    // -------------------------------
    private fun handleCheckIn() {

        // 鎖定已勾選目標
        for (i in 0 until layoutGoals.childCount) {
            val child = layoutGoals.getChildAt(i)
            if (child is CheckBox && child.isChecked) {
                child.isEnabled = false
                child.alpha = 0.5f
            }
        }

        // 設定今日已打卡
        sharedPrefs.edit().putBoolean("isCheckedInToday", true).apply()

        // 儲存打卡紀錄（含目標）
        saveCheckInHistory()

        Toast.makeText(this, "今日已完成打卡！", Toast.LENGTH_SHORT).show()
    }

    // -------------------------------
    // 🗂 儲存打卡紀錄（含完成目標）
    // -------------------------------
    private fun saveCheckInHistory() {
        val prefs = getSharedPreferences("DailyCheckInPrefs", MODE_PRIVATE)

        // ✅ 支援 API 24 的日期寫法
        val date = java.text.SimpleDateFormat(
            "yyyy-MM-dd",
            java.util.Locale.getDefault()
        ).format(java.util.Date())

        val completedGoals = mutableListOf<String>()
        for (i in 0 until layoutGoals.childCount) {
            val child = layoutGoals.getChildAt(i)
            if (child is CheckBox && child.isChecked) {
                completedGoals.add(child.text.toString())
            }
        }

        val goalString = completedGoals.joinToString(",")

        prefs.edit()
            .putString("checkin_$date", goalString)
            .apply()
    }
    private fun updateReminderTimeText(tvReminderTime: TextView) {
        val hour = sharedPrefs.getInt("reminderHour", -1)
        val minute = sharedPrefs.getInt("reminderMinute", -1)

        tvReminderTime.text = if (hour != -1 && minute != -1) {
            "提醒時間：%02d:%02d".format(hour, minute)
        } else {
            "提醒時間：尚未設定"
        }
    }

    override fun onResume() {
        super.onResume()
        val tvReminderTime = findViewById<TextView>(R.id.tvReminderTime)
        updateReminderTimeText(tvReminderTime)
    }


}
