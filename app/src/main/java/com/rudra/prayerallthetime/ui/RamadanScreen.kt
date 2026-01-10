package com.rudra.prayerallthetime.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RamadanScreen(navController: NavController, prayerViewModel: PrayerViewModel) {
    val isRamadan by prayerViewModel.isRamadan.collectAsState()
    val ramadanDay by prayerViewModel.ramadanDay.collectAsState()
    val suhoorTime by prayerViewModel.suhoorTime.collectAsState()
    val iftarTime by prayerViewModel.iftarTime.collectAsState()
    val fastingCountdown by prayerViewModel.fastingCountdown.collectAsState()
    val taraweehCount by prayerViewModel.taraweehCount.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ramadan & Fasting") },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Fasting Status Card
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = if (isRamadan) "Ramadan Day $ramadanDay" else "Voluntary Fasting",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Iftar in",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = fastingCountdown,
                        style = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 40.sp
                        ),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Suhoor ends: $suhoorTime",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "Iftar starts: $iftarTime",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Taraweeh Tracker (Conditional)
            if (isRamadan) {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Taraweeh Tracker",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "$taraweehCount / 20 Rakats",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { prayerViewModel.incrementTaraweeh() }) {
                            Text("Mark 2 Rakats")
                        }
                        Button(onClick = { prayerViewModel.resetTaraweeh() }) {
                            Text("Reset")
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Health Tips
            Text(
                text = "Fasting Health Tips",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            listOf(
                "Stay hydrated between Iftar and Suhoor.",
                "Include slow-digesting carbs in Suhoor.",
                "Break your fast with dates and water.",
                "Avoid overly fried and sugary foods."
            ).forEach { tip ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "• $tip",
                        modifier = Modifier.padding(12.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
