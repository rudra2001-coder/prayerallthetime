package com.rudra.prayerallthetime.ui.screen.ramadan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.local.LocalSettings
import com.rudra.prayerallthetime.data.local.PrayerDao
import com.rudra.prayerallthetime.data.local.TaraweehRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class RamadanViewModel @Inject constructor(
    private val localSettings: LocalSettings,
    private val prayerDao: PrayerDao
) : ViewModel() {

    private val _isRamadan = MutableStateFlow(true)
    val isRamadan: StateFlow<Boolean> = _isRamadan.asStateFlow()

    private val _ramadanDay = MutableStateFlow(10)
    val ramadanDay: StateFlow<Int> = _ramadanDay.asStateFlow()

    private val _suhoorTime = MutableStateFlow("04:30 AM")
    val suhoorTime: StateFlow<String> = _suhoorTime.asStateFlow()

    private val _iftarTime = MutableStateFlow("06:15 PM")
    val iftarTime: StateFlow<String> = _iftarTime.asStateFlow()

    private val _fastingCountdown = MutableStateFlow("12:34:56")
    val fastingCountdown: StateFlow<String> = _fastingCountdown.asStateFlow()

    private val _taraweehCount = MutableStateFlow(0)
    val taraweehCount: StateFlow<Int> = _taraweehCount.asStateFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val todayStr = LocalDate.now().format(dateFormatter)

    init {
        loadLocalTaraweeh()
    }

    private fun loadLocalTaraweeh() {
        viewModelScope.launch {
            val record = prayerDao.getTaraweehForDate(todayStr)
            _taraweehCount.value = record?.rakatCount ?: 0
        }
    }

    fun incrementTaraweeh() {
        viewModelScope.launch {
            val newCount = (_taraweehCount.value + 2).coerceAtMost(20)
            _taraweehCount.value = newCount
            prayerDao.insertTaraweeh(TaraweehRecord(todayStr, newCount))
        }
    }

    fun resetTaraweeh() {
        viewModelScope.launch {
            _taraweehCount.value = 0
            prayerDao.insertTaraweeh(TaraweehRecord(todayStr, 0))
        }
    }
}
