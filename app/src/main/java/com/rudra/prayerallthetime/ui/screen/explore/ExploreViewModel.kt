package com.rudra.prayerallthetime.ui.screen.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.local.LocalSettings
import com.rudra.prayerallthetime.data.remote.Element
import com.rudra.prayerallthetime.data.repository.PlacesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val placesRepository: PlacesRepository,
    private val localSettings: LocalSettings
) : ViewModel() {

    private val _mosques = MutableStateFlow<List<Element>>(emptyList())
    val mosques: StateFlow<List<Element>> = _mosques.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun fetchNearbyMosques() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val location = localSettings.userLocation.first()
                if (location != null) {
                    val response = placesRepository.getNearbyMosques(location.first, location.second)
                    _mosques.value = response.elements
                }
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
