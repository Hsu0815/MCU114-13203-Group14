package com.example.text

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ResetCheckInReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val sharedPrefs = context.getSharedPreferences("DailyCheckInPrefs", Context.MODE_PRIVATE)
        sharedPrefs.edit().putBoolean("isCheckedInToday", false).apply()
    }
}
