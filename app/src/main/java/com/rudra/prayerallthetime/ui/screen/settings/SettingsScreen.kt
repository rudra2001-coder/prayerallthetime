package com.rudra.prayerallthetime.ui.screen.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.navigation.Screen
import com.rudra.prayerallthetime.ui.theme.IslamicGold

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
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF8F9FA)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            SettingsCategoryHeader("General")

            SettingsItem(
                title = "Notifications",
                subtitle = "Enable prayer reminders",
                icon = Icons.Default.Notifications,
                trailing = {
                    Switch(
                        checked = notificationsEnabled,
                        onCheckedChange = { settingsViewModel.toggleNotifications(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = IslamicGold, checkedTrackColor = IslamicGold.copy(alpha = 0.5f))
                    )
                }
            )

            SettingsItem(
                title = "Dark Mode",
                subtitle = "Use Midnight Blue theme",
                icon = Icons.Default.DarkMode,
                trailing = {
                    Switch(
                        checked = darkModeEnabled,
                        onCheckedChange = { settingsViewModel.toggleDarkMode(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = IslamicGold, checkedTrackColor = IslamicGold.copy(alpha = 0.5f))
                    )
                }
            )

            SettingsCategoryHeader("Data & Location")

            SettingsItem(
                title = "Automatic Location",
                subtitle = "Update prayer times based on GPS",
                icon = Icons.Default.LocationOn,
                trailing = {
                    Switch(
                        checked = locationEnabled,
                        onCheckedChange = { settingsViewModel.toggleLocation(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = IslamicGold, checkedTrackColor = IslamicGold.copy(alpha = 0.5f))
                    )
                }
            )

            SettingsItem(
                title = "Refresh Prayer Times",
                subtitle = "Force-sync with the server",
                icon = Icons.Default.Sync,
                onClick = {
                    navController.previousBackStackEntry?.savedStateHandle?.set("refresh_prayer_times", true)
                    navController.navigateUp()
                }
            )

            SettingsCategoryHeader("Premium & App")

            SettingsItem(
                title = "Premium Version",
                subtitle = "Unlock all features",
                icon = Icons.Default.Star,
                iconColor = IslamicGold,
                trailing = {
                    Switch(
                        checked = premiumEnabled,
                        onCheckedChange = { settingsViewModel.togglePremium(it) },
                        colors = SwitchDefaults.colors(checkedThumbColor = IslamicGold, checkedTrackColor = IslamicGold.copy(alpha = 0.5f))
                    )
                }
            )

            SettingsItem(
                title = "About App",
                subtitle = "Version 1.0.0",
                icon = Icons.Default.Info,
                onClick = { /* Navigate to About */ }
            )

            SettingsCategoryHeader("Danger Zone", Color.Red)

            SettingsItem(
                title = "Reset Full App",
                subtitle = "Clear all prayer records, streaks, and settings",
                icon = Icons.Default.DeleteForever,
                iconColor = Color.Red,
                onClick = { showResetDialog = true }
            )

            Spacer(modifier = Modifier.height(32.dp))
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

@Composable
fun SettingsCategoryHeader(title: String, color: Color = Color(0xFF0F1B4C)) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = Modifier.padding(start = 24.dp, top = 24.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsItem(
    title: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color = Color(0xFF0F1B4C),
    trailing: @Composable (() -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Surface(
        onClick = onClick ?: {},
        enabled = onClick != null,
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clip(RoundedCornerShape(16.dp)),
        color = Color.White,
        tonalElevation = 0.dp
    ) {
        ListItem(
            headlineContent = { Text(title, fontWeight = FontWeight.Bold, color = Color(0xFF2C3E50)) },
            supportingContent = { Text(subtitle, color = Color.Gray, fontSize = 12.sp) },
            leadingContent = { 
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(iconColor.copy(alpha = 0.1f)),
                    contentAlignment = androidx.compose.ui.Alignment.Center
                ) {
                    Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
                }
            },
            trailingContent = trailing,
            colors = ListItemDefaults.colors(containerColor = Color.Transparent)
        )
    }
}
