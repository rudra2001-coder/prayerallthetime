package com.rudra.prayerallthetime.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.batoulapps.adhan.CalculationMethod
import com.batoulapps.adhan.Coordinates
import com.batoulapps.adhan.PrayerTimes
import com.batoulapps.adhan.data.DateComponents
import java.util.Date

class NotificationHelper(private val context: Context) {

    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("ScheduleExactAlarm")
    fun schedulePrayerNotifications(latitude: Double = 23.6556256, longitude: Double = 90.6257555) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                // Requesting permission is handled in MainActivity
            }
        }

        val coordinates = Coordinates(latitude, longitude)
        val dateComponents = DateComponents.from(Date())
        val params = CalculationMethod.MUSLIM_WORLD_LEAGUE.parameters
        val prayerTimes = PrayerTimes(coordinates, dateComponents, params)

        val prayers = listOf(
            "Fajr" to prayerTimes.fajr,
            "Dhuhr" to prayerTimes.dhuhr,
            "Asr" to prayerTimes.asr,
            "Maghrib" to prayerTimes.maghrib,
            "Isha" to prayerTimes.isha
        )

        val now = System.currentTimeMillis()

        prayers.forEach { (name, time) ->
            if (time.time > now) {
                val intent = Intent(context, PrayerRemainderReceiver::class.java).apply {
                    putExtra("PRAYER_NAME", name)
                }
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
                    name.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        time.time,
                        pendingIntent
                    )
                } else {
                    alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        time.time,
                        pendingIntent
                    )
                }
            }
        }
    }

    fun cancelAllNotifications() {
        val prayers = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
        prayers.forEach { name ->
            val intent = Intent(context, PrayerRemainderReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                name.hashCode(),
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }
}
