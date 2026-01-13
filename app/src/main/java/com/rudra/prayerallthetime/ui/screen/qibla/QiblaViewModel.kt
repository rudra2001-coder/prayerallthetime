
package com.rudra.prayerallthetime.ui.screen.qibla

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rudra.prayerallthetime.data.LocationService
import com.rudra.prayerallthetime.data.repository.PrayerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class QiblaViewModel @Inject constructor(
    private val prayerRepository: PrayerRepository,
    private val locationService: LocationService
) : ViewModel() {

    private val _qiblaDirection = MutableStateFlow(0f)
    val qiblaDirection: StateFlow<Float> = _qiblaDirection.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _currentLocation = MutableStateFlow<Location?>(null)
    val currentLocation: StateFlow<Location?> = _currentLocation.asStateFlow()

    init {
        loadQiblaDirection()
    }

    fun loadQiblaDirection() {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val location = withContext(Dispatchers.IO) {
                    locationService.getCurrentLocation()
                }

                if (location != null) {
                    _currentLocation.value = location

                    val data = withContext(Dispatchers.IO) {
                        prayerRepository.getPrayerTimes(
                            location.latitude,
                            location.longitude,
                            LocalDate.now()
                        )
                    }

                    // Parse Qibla direction with better error handling
                    val qiblaStr = data.qiblaDirection ?: "0"
                    val cleanQibla = qiblaStr.replace("°", "").trim()

                    _qiblaDirection.value = when {
                        cleanQibla.matches(Regex("-?\\d+(\\.\\d+)?")) -> {
                            var direction = cleanQibla.toFloat()
                            // Normalize to 0-360 range
                            while (direction < 0) direction += 360
                            while (direction >= 360) direction -= 360
                            direction
                        }
                        else -> {
                            // Fallback: Calculate Qibla direction mathematically
                            calculateQiblaDirection(location.latitude, location.longitude)
                        }
                    }
                } else {
                    _errorMessage.value = "Unable to get current location"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.localizedMessage ?: "Unknown error"}"
                // Use last known location or default
                _currentLocation.value?.let { location ->
                    _qiblaDirection.value = calculateQiblaDirection(location.latitude, location.longitude)
                }
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun refreshQiblaDirection() {
        loadQiblaDirection()
    }

    /**
     * Calculate Qibla direction using spherical trigonometry
     * Formula: Q = atan2(sin(λ - λK), cos(φ)*tan(φK) - sin(φ)*cos(λ - λK))
     * Where:
     * φ = latitude of location (radians)
     * λ = longitude of location (radians)
     * φK = latitude of Kaaba (21.4225° N)
     * λK = longitude of Kaaba (39.8262° E)
     */
    private fun calculateQiblaDirection(latitude: Double, longitude: Double): Float {
        val kaabaLat = Math.toRadians(21.4225)  // Kaaba latitude in radians
        val kaabaLon = Math.toRadians(39.8262)  // Kaaba longitude in radians

        val latRad = Math.toRadians(latitude)
        val lonRad = Math.toRadians(longitude)

        val deltaLon = kaabaLon - lonRad

        val y = sin(deltaLon)
        val x = cos(latRad) * tan(kaabaLat) - sin(latRad) * cos(deltaLon)

        var qibla = Math.toDegrees(atan2(y, x)).toFloat()

        // Normalize to 0-360
        if (qibla < 0) qibla += 360f
        if (qibla >= 360) qibla -= 360f

        return qibla
    }

    private fun sin(value: Double): Double = kotlin.math.sin(value)
    private fun cos(value: Double): Double = kotlin.math.cos(value)
    private fun tan(value: Double): Double = kotlin.math.tan(value)
    private fun atan2(y: Double, x: Double): Double = kotlin.math.atan2(y, x)
}