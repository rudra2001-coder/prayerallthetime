package com.rudra.prayerallthetime.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rudra.prayerallthetime.ui.components.ErrorHeroCard
import com.rudra.prayerallthetime.ui.components.PremiumHeroCard

@Composable
fun PrayerHomeScreen(viewModel: PrayerViewModel = hiltViewModel()) {
    val prayerState by viewModel.prayerState
    
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0F2D))
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (val state = prayerState) {
                is PrayerState.Loading -> {
                    PremiumHeroCard(
                        isLoading = true
                    )
                }
                is PrayerState.Success -> {
                    PremiumHeroCard(
                        nextPrayerName = state.data.nextPrayer.name,
                        countdown = state.data.countdown,
                        sunriseTime = state.data.sunrise,
                        hijriDate = state.data.hijriDate,
                        gregorianDate = state.data.gregorianDate,
                        cityName = state.data.city,
                        prayerTime = state.data.nextPrayer.time,
                        qiblaDirection = state.data.qiblaDirection,
                        prayerProgress = state.data.prayerProgress,
                        isLoading = false,
                        nextPrayerArabicName = state.data.nextPrayer.arabicName
                    )
                }
                is PrayerState.Error -> {
                    ErrorHeroCard(
                        errorMessage = state.message,
                        onRetry = { viewModel.refreshData() }
                    )
                }
            }
        }
    }
}
