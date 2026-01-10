package com.rudra.prayerallthetime.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = viewModel()
) {
    val notificationsEnabled by settingsViewModel.notificationsEnabled.collectAsState()
    val darkModeEnabled by settingsViewModel.darkModeEnabled.collectAsState()
    val locationEnabled by settingsViewModel.locationEnabled.collectAsState()
    val premiumEnabled by settingsViewModel.premiumEnabled.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "General",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )

            ListItem(
                headlineContent = { Text("Notifications") },
                supportingContent = { Text("Enable prayer reminders") },
                leadingContent = { Icon(Icons.Default.Notifications, contentDescription = null) },
                trailingContent = {
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { settingsViewModel.toggleNotifications(it) }
                    )
                }
            )

            ListItem(
                headlineContent = { Text("Dark Mode") },
                supportingContent = { Text("Use Midnight Blue theme") },
                leadingContent = { Icon(Icons.Default.DarkMode, contentDescription = null) },
                trailingContent = {
                    Switch(
                        checked = darkModeEnabled,
                        onCheckedChange = { settingsViewModel.toggleDarkMode(it) }
                    )
                }
            )

            ListItem(
                headlineContent = { Text("Language") },
                supportingContent = { Text("English (US)") },
                leadingContent = { Icon(Icons.Default.Language, contentDescription = null) }
            )

            HorizontalDivider()

            Text(
                text = "Location",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )

            ListItem(
                headlineContent = { Text("Automatic Location") },
                supportingContent = { Text("Update prayer times based on GPS") },
                leadingContent = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                trailingContent = {
                    Switch(
                        checked = locationEnabled,
                        onCheckedChange = { settingsViewModel.toggleLocation(it) }
                    )
                }
            )

            HorizontalDivider()

            Text(
                text = "Premium & App",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(16.dp)
            )

            ListItem(
                headlineContent = { Text("Premium Version") },
                supportingContent = { Text("Unlock all features") },
                leadingContent = { Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.secondary) },
                trailingContent = {
                    Switch(
                        checked = premiumEnabled,
                        onCheckedChange = { settingsViewModel.togglePremium(it) }
                    )
                }
            )

            ListItem(
                headlineContent = { Text("Offline Mode") },
                supportingContent = { Text("Access prayer times without internet") },
                leadingContent = { Icon(Icons.Default.OfflinePin, contentDescription = null) },
                trailingContent = {
                    Switch(
                        checked = false,
                        onCheckedChange = { /* Placeholder */ }
                    )
                }
            )
            
            ListItem(
                headlineContent = { Text("App Version") },
                supportingContent = { Text("1.0.0") }
            )
        }
    }
}
