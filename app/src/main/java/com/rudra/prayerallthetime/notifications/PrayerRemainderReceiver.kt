package com.rudra.prayerallthetime.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.rudra.prayerallthetime.R

class PrayerRemainderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prayerName = intent.getStringExtra("PRAYER_NAME") ?: "Prayer"
        
        // 1. Show Visual Notification
        showNotification(context, prayerName)
        
        // 2. Start Athan Audio Service
        val athanIntent = Intent(context, AthanService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(athanIntent)
        } else {
            context.startService(athanIntent)
        }
    }

    private fun showNotification(context: Context, prayerName: String) {
        val channelId = "prayer_reminders"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Prayer Reminders",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifications for daily prayer times"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle("Time for $prayerName")
            .setContentText("It is time for $prayerName prayer. Success awaits!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(prayerName.hashCode(), notification)
    }
}
