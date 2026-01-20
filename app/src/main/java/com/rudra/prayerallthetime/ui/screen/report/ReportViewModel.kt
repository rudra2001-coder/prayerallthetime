package com.rudra.prayerallthetime.ui.screen.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.local.PrayerDao
import com.rudra.prayerallthetime.data.local.PrayerRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import javax.inject.Inject

enum class ReportType {
    DAILY, WEEKLY, MONTHLY, CUSTOM
}

data class ReportState(
    val reportType: ReportType = ReportType.DAILY,
    val startDate: LocalDate = LocalDate.now(),
    val endDate: LocalDate = LocalDate.now(),
    val prayerStats: Map<String, Int> = emptyMap(),
    val totalPrayers: Int = 0,
    val completionPercentage: Float = 0f,
    val dailyProgress: List<Pair<String, Float>> = emptyList()
)

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val prayerDao: PrayerDao
) : ViewModel() {

    private val _state = MutableStateFlow(ReportState())
    val state: StateFlow<ReportState> = _state.asStateFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    init {
        updateReport(ReportType.DAILY)
    }

    fun updateReport(type: ReportType, start: LocalDate? = null, end: LocalDate? = null) {
        val now = LocalDate.now()
        val (newStart, newEnd) = when (type) {
            ReportType.DAILY -> now to now
            ReportType.WEEKLY -> now.minusDays(6) to now
            ReportType.MONTHLY -> now.minusDays(29) to now
            ReportType.CUSTOM -> (start ?: now) to (end ?: now)
        }

        _state.value = _state.value.copy(
            reportType = type,
            startDate = newStart,
            endDate = newEnd
        )

        fetchReportData(newStart, newEnd)
    }

    private fun fetchReportData(start: LocalDate, end: LocalDate) {
        viewModelScope.launch {
            prayerDao.getRecordsInRange(start.format(dateFormatter), end.format(dateFormatter))
                .collect { records ->
                    processRecords(records, start, end)
                }
        }
    }

    private fun processRecords(records: List<PrayerRecord>, start: LocalDate, end: LocalDate) {
        val prayerNames = listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha")
        val stats = prayerNames.associateWith { name ->
            records.count { it.prayerName == name && it.isCompleted }
        }

        val totalPossible = ChronoUnit.DAYS.between(start, end.plusDays(1)).toInt() * 5
        val totalCompleted = records.count { it.isCompleted }
        val percentage = if (totalPossible > 0) (totalCompleted.toFloat() / totalPossible) else 0f

        // Prepare daily progress for chart
        val dailyMap = records.groupBy { it.date }
        val dailyProgress = mutableListOf<Pair<String, Float>>()
        var current = start
        while (!current.isAfter(end)) {
            val dateStr = current.format(dateFormatter)
            val dayRecords = dailyMap[dateStr] ?: emptyList()
            val dayCompleted = dayRecords.count { it.isCompleted }
            dailyProgress.add(current.dayOfWeek.name.take(3) to (dayCompleted.toFloat() / 5f))
            current = current.plusDays(1)
        }

        _state.value = _state.value.copy(
            prayerStats = stats,
            totalPrayers = totalCompleted,
            completionPercentage = percentage,
            dailyProgress = dailyProgress
        )
    }
}
