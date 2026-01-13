package com.rudra.prayerallthetime.ui.screen.worship

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.local.PrayerDao
import com.rudra.prayerallthetime.data.local.TasbeehRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class WorshipViewModel @Inject constructor(
    private val prayerDao: PrayerDao
) : ViewModel() {

    private val _tasbeehCount = MutableStateFlow(0)
    val tasbeehCount: StateFlow<Int> = _tasbeehCount.asStateFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val todayStr = LocalDate.now().format(dateFormatter)

    init {
        loadLocalTasbeeh()
    }

    private fun loadLocalTasbeeh() {
        viewModelScope.launch {
            val record = prayerDao.getTasbeehForDate(todayStr)
            _tasbeehCount.value = record?.totalCount ?: 0
        }
    }

    fun incrementTasbeeh() {
        viewModelScope.launch {
            val newCount = _tasbeehCount.value + 1
            _tasbeehCount.value = newCount
            prayerDao.insertTasbeeh(TasbeehRecord(todayStr, newCount))
        }
    }

    fun resetTasbeeh() {
        viewModelScope.launch {
            _tasbeehCount.value = 0
            prayerDao.insertTasbeeh(TasbeehRecord(todayStr, 0))
        }
    }
}
