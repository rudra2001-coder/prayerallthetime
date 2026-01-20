package com.rudra.prayerallthetime.ui.screen.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val notificationsEnabled by settingsViewModel.notificationsEnabled.collectAsState()
    val darkModeEnabled by settingsViewModel.darkModeEnabled.collectAsState()
    val locationEnabled by settingsViewModel.locationEnabled.collectAsState()
    val premiumEnabled by settingsViewModel.premiumEnabled.collectAsState()

    var showResetDialog by remember { mutableStateOf(false) }

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

            HorizontalDivider()

            Text(
                text = "Danger Zone",
                style = MaterialTheme.typography.labelLarge,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )

            ListItem(
                headlineContent = { Text("Reset Full App", color = Color.Red) },
                supportingContent = { Text("Clear all prayer records, streaks, and settings") },
                leadingContent = { Icon(Icons.Default.DeleteForever, contentDescription = null, tint = Color.Red) },
                modifier = Modifier.padding(bottom = 24.dp),
                trailingContent = {
                    TextButton(onClick = { showResetDialog = true }) {
                        Text("Reset", color = Color.Red)
                    }
                }
            )
        }
    }

    if (showResetDialog) {
        AlertDialog(
            onDismissRequest = { showResetDialog = false },
            title = { Text("Confirm Full Reset") },
            text = { Text("This will permanently delete all your prayer records, habits, and settings. This action cannot be undone.") },
            confirmButton = {
                Button(
                    onClick = {
                        settingsViewModel.resetFullApp {
                            showResetDialog = false
                            navController.navigate(Screen.Dashboard.route) {
                                popUpTo(0)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Reset Everything")
                }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
