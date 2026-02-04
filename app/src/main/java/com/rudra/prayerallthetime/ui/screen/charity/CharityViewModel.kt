package com.rudra.prayerallthetime.ui.screen.charity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.local.CharityDao
import com.rudra.prayerallthetime.data.local.CharityRecord
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CharityViewModel @Inject constructor(
    private val charityDao: CharityDao
) : ViewModel() {

    private val _zakatAssets = MutableStateFlow(0.0)
    val zakatAssets: StateFlow<Double> = _zakatAssets.asStateFlow()

    private val _zakatLiability = MutableStateFlow(0.0)
    val zakatLiability: StateFlow<Double> = _zakatLiability.asStateFlow()

    private val _calculatedZakat = MutableStateFlow(0.0)
    val calculatedZakat: StateFlow<Double> = _calculatedZakat.asStateFlow()

    val allRecords: StateFlow<List<CharityRecord>> = charityDao.getAllCharityRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalCharity: StateFlow<Double> = charityDao.getTotalCharity()
        .map { it ?: 0.0 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val monthlyCharity: StateFlow<Double> = allRecords.map { records ->
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        records.filter { 
            val cal = Calendar.getInstance().apply { timeInMillis = it.date }
            cal.get(Calendar.MONTH) == currentMonth && cal.get(Calendar.YEAR) == currentYear
        }.sumOf { it.amount }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun updateAssets(amount: Double) {
        _zakatAssets.value = amount
        calculateZakat()
    }

    fun updateLiabilities(amount: Double) {
        _zakatLiability.value = amount
        calculateZakat()
    }

    private fun calculateZakat() {
        val netWealth = _zakatAssets.value - _zakatLiability.value
        // Nisab threshold is usually based on current gold/silver prices. 
        // For simplicity, we assume Nisab is reached if netWealth > 0.
        // Zakat is 2.5% of net wealth.
        _calculatedZakat.value = if (netWealth > 0) netWealth * 0.025 else 0.0
    }

    fun logCharity(amount: Double, type: String, description: String?) {
        viewModelScope.launch {
            charityDao.insertCharityRecord(
                CharityRecord(
                    amount = amount,
                    type = type,
                    description = description
                )
            )
        }
    }

    fun deleteRecord(record: CharityRecord) {
        viewModelScope.launch {
            charityDao.deleteCharityRecord(record)
        }
    }
}
