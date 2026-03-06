package com.rudra.prayerallthetime.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.core.app.NotificationCompat
import com.rudra.prayerallthetime.R

class PrayerRemainderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val prayerName = intent.getStringExtra("PRAYER_NAME") ?: "Prayer"
        val isReminder = intent.getBooleanExtra("IS_REMINDER", false)
        val vibrationEnabled = intent.getBooleanExtra("VIBRATION_ENABLED", true)
        val soundEnabled = intent.getBooleanExtra("SOUND_ENABLED", true)
        
        // Show notification
        showNotification(context, prayerName, isReminder)
        
        // Vibrate if enabled
        if (vibrationEnabled) {
            vibrate(context)
        }
        
        // Start Athan Audio Service if sound is enabled and it's prayer time (not reminder)
        if (soundEnabled && !isReminder) {
            val athanIntent = Intent(context, AthanService::class.java)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(athanIntent)
            } else {
                context.startService(athanIntent)
            }
        }
    }

    private fun showNotification(context: Context, prayerName: String, isReminder: Boolean) {
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
        
        val (title, message) = if (isReminder) {
            "🕌 $prayerName in 10 minutes" to "Prepare for $prayerName prayer"
        } else {
            "🕌 Time for $prayerName" to "It is time for $prayerName prayer. Success awaits!"
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        val requestCode = if (isReminder) {
            "${prayerName}_reminder".hashCode()
        } else {
            prayerName.hashCode()
        }
        notificationManager.notify(requestCode, notification)
    }
    
    private fun vibrate(context: Context) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
        
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createWaveform(longArrayOf(0, 500, 200, 500), -1))
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(longArrayOf(0, 500, 200, 500), -1)
        }
    }
}
