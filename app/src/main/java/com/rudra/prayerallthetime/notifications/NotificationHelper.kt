package com.rudra.prayerallthetime.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.rudra.prayerallthetime.core.config.CalculationMethod
import com.rudra.prayerallthetime.core.config.Madhab
import com.rudra.prayerallthetime.core.prayer.PrayerTimesCalculator
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

class NotificationHelper(private val context: Context) {

    private val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    @SuppressLint("ScheduleExactAlarm")
    fun schedulePrayerNotifications(latitude: Double = 23.6556256, longitude: Double = 90.6257555) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!alarmManager.canScheduleExactAlarms()) {
                // Permission handled in Activity
            }
        }

        val calculator = PrayerTimesCalculator(CalculationMethod.BANGLADESH, Madhab.SHAFI)
        val tz = TimeZone.getDefault().rawOffset / (1000.0 * 60 * 60)
        val today = LocalDate.now()
        val times = calculator.calculatePrayerTimes(today, latitude, longitude, tz)

        val prayers = listOf(
            "Fajr" to times.fajr,
            "Dhuhr" to times.dhuhr,
            "Asr" to times.asr,
            "Maghrib" to times.maghrib,
            "Isha" to times.isha
        )

        val now = System.currentTimeMillis()
        val timeFormat = java.text.SimpleDateFormat("hh:mm a", Locale.getDefault())

        prayers.forEach { (name, timeStr) ->
            try {
                val date = timeFormat.parse(timeStr)
                if (date != null) {
                    val prayerCal = Calendar.getInstance()
                    val timeCal = Calendar.getInstance()
                    timeCal.time = date
                    
                    prayerCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY))
                    prayerCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE))
                    prayerCal.set(Calendar.SECOND, 0)
                    
                    var prayerTimeMillis = prayerCal.timeInMillis

                    // If time already passed today, schedule for tomorrow
                    if (prayerTimeMillis <= now) {
                        prayerCal.add(Calendar.DAY_OF_YEAR, 1)
                        prayerTimeMillis = prayerCal.timeInMillis
                    }

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
                            prayerTimeMillis,
                            pendingIntent
                        )
                    } else {
                        alarmManager.setExact(
                            AlarmManager.RTC_WAKEUP,
                            prayerTimeMillis,
                            pendingIntent
                        )
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
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
