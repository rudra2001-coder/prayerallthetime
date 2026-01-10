package com.rudra.prayerallthetime.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.components.Badges
import com.rudra.prayerallthetime.ui.components.PrayerCharts
import com.rudra.prayerallthetime.ui.components.Streaks
import com.rudra.prayerallthetime.ui.components.WeeklyStats

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(navController: NavController, prayerViewModel: PrayerViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Spiritual Analytics") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.padding(paddingValues)) {
            item { PrayerCharts(prayerViewModel = prayerViewModel) }
            item { WeeklyStats(prayerViewModel = prayerViewModel) }
            item { Streaks(prayerViewModel = prayerViewModel) }
            item { Badges(prayerViewModel = prayerViewModel) }
        }
    }
}
