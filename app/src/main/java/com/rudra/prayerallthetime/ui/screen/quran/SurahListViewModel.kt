package com.rudra.prayerallthetime.ui.screen.quran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.SurahSummary
import com.rudra.prayerallthetime.data.repository.QuranRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SurahListViewModel @Inject constructor(
    private val repository: QuranRepository
) : ViewModel() {

    private val _surahList = MutableStateFlow<List<SurahSummary>>(emptyList())
    val surahList: StateFlow<List<SurahSummary>> = _surahList.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init {
        loadSurahList()
    }

    fun loadSurahList() {
        viewModelScope.launch {
            _isLoading.value = true
            _surahList.value = repository.getSurahList()
            _isLoading.value = false
        }
    }
}
