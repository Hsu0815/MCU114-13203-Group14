package com.example.text

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.widget.*

class HabitsScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_habits_screen)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val etHabit = findViewById<EditText>(R.id.etHabit)
        val btnSaveHabit = findViewById<Button>(R.id.btnSaveHabit)
        val tvProgress2 = findViewById<TextView>(R.id.tvProgress2)
        val layoutHabits = findViewById<LinearLayout>(R.id.layoutHabits)
        val btnCheckInHabit = findViewById<Button>(R.id.btnCheckInHabit)


        val habits = mutableListOf<String>()
        var habitCheckedCount = 0

        btnSaveHabit.setOnClickListener {
            val habitText = etHabit.text.toString().trim()
            if (habitText.isNotEmpty()) {
                habits.add(habitText)

                val checkBox = CheckBox(this)
                checkBox.text = habitText
                checkBox.textSize = 16f

                checkBox.setOnCheckedChangeListener { _, isChecked ->
                    if (isChecked) habitCheckedCount++ else habitCheckedCount--
                    // 你可以顯示習慣進度在 TextView，例如：
                    // tvHabitProgress.text = "$habitCheckedCount / ${habits.size}"
                }

                layoutHabits.addView(checkBox)
                tvProgress2.text = "目前進度：$habitCheckedCount / ${habits.size}"
                etHabit.text.clear()
            }
        }

        btnCheckInHabit.setOnClickListener {
            for (i in 0 until layoutHabits.childCount) {
                val child = layoutHabits.getChildAt(i)
                if (child is CheckBox && child.isChecked) {
                    child.isEnabled = false // 鎖定已勾選
                    child.alpha = 0.5f     // 視覺提示
                }
            }

            // 更新進度文字（habitCheckedCount 已經在 CheckBox 監聽裡正確統計）
            tvProgress2.text = "目前進度：$habitCheckedCount / ${habits.size}"

            // 不強制鎖定按鈕，按需決定
            // btnCheckInHabit.isEnabled = false
        }


            }
        }

