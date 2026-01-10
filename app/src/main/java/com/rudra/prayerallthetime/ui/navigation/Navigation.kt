package com.rudra.prayerallthetime.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.rudra.prayerallthetime.ui.*
import com.rudra.prayerallthetime.ui.theme.IslamicGold

@Composable
fun PremiumNavigation() {
    val navController = rememberNavController()
    val prayerViewModel: PrayerViewModel = viewModel()
    val settingsViewModel: SettingsViewModel = viewModel()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var showPremiumDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (currentDestination?.route != Screen.Dashboard.route) {
                PremiumTopAppBar(
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        },
        bottomBar = {
            PremiumBottomNavigationBar(
                currentDestination = currentDestination,
                navController = navController,
                onPremiumClick = { showPremiumDialog = true }
            )
        },
        floatingActionButton = {
            if (currentDestination?.route == Screen.Dashboard.route) {
                PremiumFloatingActionButton(navController = navController, prayerViewModel = prayerViewModel)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    prayerViewModel = prayerViewModel,
                    navController = navController
                )
            }

            composable(Screen.Prayers.route) {
                PrayersScreen(
                    prayerViewModel = prayerViewModel,
                    navController = navController
                )
            }

            composable(Screen.QuranHadith.route) {
                QuranHadithScreen(
                    navController = navController,
                    prayerViewModel = prayerViewModel
                )
            }

            composable(Screen.Analytics.route) {
                AnalyticsScreen(navController = navController, prayerViewModel = prayerViewModel)
            }

            composable(Screen.Settings.route) {
                SettingsScreen(navController = navController, settingsViewModel = settingsViewModel)
            }

            composable(Screen.Ramadan.route) {
                RamadanScreen(
                    navController = navController,
                    prayerViewModel = prayerViewModel
                )
            }

            composable(Screen.Worship.route) {
                WorshipScreen(navController = navController, prayerViewModel = prayerViewModel)
            }

            composable(Screen.Family.route) {
                FamilyScreen(navController = navController, prayerViewModel = prayerViewModel)
            }

            composable(Screen.Qibla.route) {
                QiblaScreen(
                    navController = navController,
                    prayerViewModel = prayerViewModel
                )
            }

            // Placeholders for other screens
            composable(Screen.Tasbeeh.route) { WorshipScreen(navController = navController, prayerViewModel = prayerViewModel) }
            composable(Screen.Wudu.route) { Text("Wudu Guide Coming Soon") }
            composable(Screen.Tahajjud.route) { Text("Tahajjud Timer Coming Soon") }
            composable(Screen.Charity.route) { Text("Charity Tracker Coming Soon") }
            composable(Screen.Calendar.route) { Text("Islamic Calendar Coming Soon") }
            composable(Screen.Profile.route) { Text("Profile Coming Soon") }
            composable(Screen.Notifications.route) { Text("Notifications Coming Soon") }
            composable(Screen.Achievements.route) { Text("Achievements Coming Soon") }
            composable(Screen.Streaks.route) { Text("Streaks Coming Soon") }
            composable(Screen.Charts.route) { Text("Charts Coming Soon") }
            composable(Screen.StreakDetails.route) { Text("Streak Details Coming Soon") }
            composable(Screen.RamadanTimer.route) { Text("Ramadan Timer Coming Soon") }
            composable(Screen.Taraweeh.route) { Text("Taraweeh Tracker Coming Soon") }
            composable(Screen.FamilyMember.route) { Text("Family Member Details Coming Soon") }
        }
    }

    if (showPremiumDialog) {
        AlertDialog(
            onDismissRequest = { showPremiumDialog = false },
            title = { Text("Upgrade to Premium") },
            text = { Text("Unlock exclusive features and support the development of this app!") },
            confirmButton = {
                Button(onClick = { showPremiumDialog = false }) {
                    Text("Upgrade Now")
                }
            },
            dismissButton = {
                TextButton(onClick = { showPremiumDialog = false }) {
                    Text("Later")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumTopAppBar(
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val title = currentDestination?.let { destination ->
        Screen.allScreens().find { it.route == destination.route }?.title ?: "Salaam"
    } ?: "Salaam"

    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(IslamicGold, Color(0xFFFFD93D))
                            )
                        )
                        .padding(6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🕌",
                        fontSize = 14.sp
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = Color(0xFF2C3E50)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            scrolledContainerColor = Color.White
        ),
        navigationIcon = {
            IconButton(
                onClick = { navController.navigateUp() }
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color(0xFF2C3E50)
                )
            }
        },
        actions = {
            if (currentDestination?.route != Screen.Settings.route) {
                IconButton(
                    onClick = { navController.navigate(Screen.Settings.route) }
                ) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color(0xFF2C3E50)
                    )
                }
            }
        },
        modifier = Modifier.shadow(
            elevation = 8.dp,
            shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp),
            clip = true
        )
    )
}

@Composable
fun PremiumBottomNavigationBar(
    currentDestination: NavDestination?,
    navController: NavHostController,
    onPremiumClick: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 0.dp,
        modifier = Modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                clip = true
            )
            .navigationBarsPadding()
    ) {
        listOf(
            Screen.Dashboard,
            Screen.Prayers,
            Screen.QuranHadith,
            Screen.Analytics
        ).forEach { screen ->
            val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent,
                    selectedIconColor = IslamicGold,
                    unselectedIconColor = Color(0xFF2C3E50).copy(alpha = 0.5f),
                    selectedTextColor = IslamicGold,
                    unselectedTextColor = Color(0xFF2C3E50).copy(alpha = 0.5f)
                ),
                icon = {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (selected) Color(0xFF2C3E50).copy(alpha = 0.08f)
                                else Color.Transparent
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        screen.icon?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = screen.title,
                                modifier = Modifier.size(24.dp)
                            )
                        } ?: screen.emoji?.let {
                            Text(
                                text = it,
                                fontSize = 20.sp
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 11.sp
                        )
                    )
                }
            )
        }

        // Integrated Premium Button
        NavigationBarItem(
            selected = false,
            onClick = onPremiumClick,
            colors = NavigationBarItemDefaults.colors(
                indicatorColor = Color.Transparent,
                unselectedIconColor = IslamicGold,
                unselectedTextColor = IslamicGold
            ),
            icon = {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(IslamicGold, Color(0xFFFFD93D))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Premium",
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
            },
            label = {
                Text(
                    text = "Premium",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                )
            }
        )
    }
}

@Composable
fun PremiumFloatingActionButton(
    navController: NavHostController,
    prayerViewModel: PrayerViewModel
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(end = 16.dp)
            .navigationBarsPadding()
            .padding(bottom = 16.dp) // Added some extra padding to keep it above the nav bar
    ) {
        if (expanded) {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screen.Qibla.route) },
                containerColor = Color(0xFF4ECDC4)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Explore, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Find Qibla", color = Color.White)
                }
            }

            ExtendedFloatingActionButton(
                onClick = { navController.navigate(Screen.Worship.route) },
                containerColor = Color(0xFF8B4513)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FormatListBulleted, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Tasbeeh", color = Color.White)
                }
            }
        }

        FloatingActionButton(
            onClick = { expanded = !expanded },
            containerColor = IslamicGold,
            modifier = Modifier.size(56.dp)
        ) {
            Icon(
                if (expanded) Icons.Default.Close else Icons.Default.Add,
                contentDescription = null,
                tint = Color.White
            )
        }
    }
}

@Composable
fun Navigation() {
    PremiumNavigation()
}
