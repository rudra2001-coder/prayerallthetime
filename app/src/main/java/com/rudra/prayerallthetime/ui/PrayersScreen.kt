package com.rudra.prayerallthetime.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.rudra.prayerallthetime.data.Prayer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayersScreen(
    prayerViewModel: PrayerViewModel, 
    navController: NavController
) {
    val prayers by prayerViewModel.prayers.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Prayer Timetable") },
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
            items(prayers) { prayer ->
                PrayerListItem(prayer = prayer, onPrayerToggled = {
                    prayerViewModel.togglePrayerState(it)
                })
            }
        }
    }
}

@Composable
fun PrayerListItem(prayer: Prayer, onPrayerToggled: (Prayer) -> Unit) {
    ListItem(
        headlineContent = { Text(prayer.name) },
        supportingContent = { Text(prayer.time) },
        trailingContent = {
            Switch(
                checked = prayer.isPrayed,
                onCheckedChange = { onPrayerToggled(prayer) }
            )
        }
    )
}
