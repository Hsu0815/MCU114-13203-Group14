package com.example.text

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.app.NotificationManager
import android.app.NotificationChannel
import androidx.core.app.NotificationCompat
import android.os.Build

class CheckInReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {

        // 1️⃣ 讀取當天打卡狀態
        val sharedPrefs = context.getSharedPreferences("DailyCheckInPrefs", Context.MODE_PRIVATE)
        val isCheckedInToday = sharedPrefs.getBoolean("isCheckedInToday", false)

        // 2️⃣ 如果當天已打卡 → 不發通知
        if (isCheckedInToday) return

        // 3️⃣ 發送通知
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "daily_checkin_channel"

        // 4️⃣ Android 8+ 建立通知頻道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "每日打卡提醒",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        // 5️⃣ 建立通知
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("打卡提醒")
            .setContentText("今天還沒打卡喔，快去完成目標！")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // 請確認有此 icon
            .setAutoCancel(true)
            .build()

        // 6️⃣ 發送通知（這裡就不會紅線了）
        notificationManager.notify(1, notification)
    }
}
