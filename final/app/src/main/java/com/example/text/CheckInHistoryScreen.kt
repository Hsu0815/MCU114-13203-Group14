package com.example.text

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class CheckInHistoryScreen : AppCompatActivity() {

    private lateinit var layoutHistory: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ✅ 只呼叫一次
        setContentView(R.layout.activity_check_in_history)

        // ✅ 只用全域變數，不要再宣告 val layoutHistory
        layoutHistory = findViewById(R.id.layoutHistory)

        val prefs = getSharedPreferences("DailyCheckInPrefs", MODE_PRIVATE)
        val allData = prefs.all

        // 只取 checkin_ 開頭的資料
        val historyList = allData
            .filterKeys { it.startsWith("checkin_") }
            .toList()
            .sortedByDescending { it.first } // 日期新 → 舊

        if (historyList.isEmpty()) {
            addText("尚無打卡紀錄", isTitle = true)
            return
        }

        for ((key, value) in historyList) {
            val date = key.removePrefix("checkin_")
            val goals = value.toString()
                .split(",")
                .filter { it.isNotBlank() }

            // 📅 日期
            addText("📅 $date", isTitle = true)

            // ✔ 目標（若當天沒勾選任何目標，也顯示提示）
            if (goals.isEmpty()) {
                addText("（當天沒有勾選完成目標）")
            } else {
                goals.forEach { addText("✔ $it") }
            }

            addDivider()
        }
    }

    private fun addText(text: String, isTitle: Boolean = false) {
        val tv = TextView(this)
        tv.text = text
        tv.textSize = if (isTitle) 18f else 16f
        tv.setPadding(8, 8, 8, 8)
        layoutHistory.addView(tv)
    }

    private fun addDivider() {
        val divider = View(this)
        divider.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            2
        )
        divider.setBackgroundColor(0xFFDDDDDD.toInt())
        layoutHistory.addView(divider)
    }
}
