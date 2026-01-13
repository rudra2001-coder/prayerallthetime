package com.rudra.prayerallthetime.ui.screen.hadith

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.HadithItem
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

    private val _hadiths = MutableStateFlow<List<HadithItem>>(emptyList())
    val hadiths: StateFlow<List<HadithItem>> = _hadiths.asStateFlow()

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
                // Fetching a batch of hadiths. Adjust page size/logic as needed.
                val newHadiths = mutableListOf<HadithItem>()
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
