package com.rudra.prayerallthetime.ui.screen.hadith

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.local.HadithEntity
import com.rudra.prayerallthetime.data.repository.HadithRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HadithViewModel @Inject constructor(
    private val repository: HadithRepository
) : ViewModel() {

    private val _hadiths = MutableStateFlow<List<HadithEntity>>(emptyList())
    val hadiths: StateFlow<List<HadithEntity>> = _hadiths.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var currentPage = 1

    init {
        loadHadiths()
    }

    private fun loadHadiths() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val newHadiths = mutableListOf<HadithEntity>()
                repeat(5) {
                    val h = repository.getRandomHadith()
                    if (h != null) newHadiths.add(h)
                }
                _hadiths.value = _hadiths.value + newHadiths
            } catch (e: Exception) {
                // Handle error
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadMore() {
        currentPage++
        loadHadiths()
    }
}
