package com.rudra.prayerallthetime.ui.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.rudra.prayerallthetime.ui.screen.analytics.AnalyticsScreen
import com.rudra.prayerallthetime.ui.screen.analytics.AnalyticsViewModel
import com.rudra.prayerallthetime.ui.screen.calendar.CalendarScreen
import com.rudra.prayerallthetime.ui.screen.calendar.CalendarViewModel
import com.rudra.prayerallthetime.ui.screen.dashboard.CompleteDashboardScreen
import com.rudra.prayerallthetime.ui.screen.dashboard.DashboardViewModel
import com.rudra.prayerallthetime.ui.screen.explore.ExploreScreen
import com.rudra.prayerallthetime.ui.screen.explore.ExploreViewModel
import com.rudra.prayerallthetime.ui.screen.explore.NearbyMosquesScreen
import com.rudra.prayerallthetime.ui.screen.family.FamilyScreen
import com.rudra.prayerallthetime.ui.screen.family.FamilyViewModel
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerViewModel
import com.rudra.prayerallthetime.ui.screen.prayer.PrayersScreen
import com.rudra.prayerallthetime.ui.screen.qibla.QiblaScreen
import com.rudra.prayerallthetime.ui.screen.qibla.QiblaViewModel
import com.rudra.prayerallthetime.ui.screen.quran.QuranHadithScreen
import com.rudra.prayerallthetime.ui.screen.quran.QuranHadithViewModel
import com.rudra.prayerallthetime.ui.screen.quran.SurahDetailScreen
import com.rudra.prayerallthetime.ui.screen.quran.SurahListScreen
import com.rudra.prayerallthetime.ui.screen.ramadan.RamadanScreen
import com.rudra.prayerallthetime.ui.screen.ramadan.RamadanViewModel
import com.rudra.prayerallthetime.ui.screen.report.ReportScreen
import com.rudra.prayerallthetime.ui.screen.settings.SettingsScreen
import com.rudra.prayerallthetime.ui.screen.settings.SettingsViewModel
import com.rudra.prayerallthetime.ui.screen.tahajjud.TahajjudScreen
import com.rudra.prayerallthetime.ui.screen.tahajjud.TahajjudViewModel
import com.rudra.prayerallthetime.ui.screen.worship.WorshipScreen
import com.rudra.prayerallthetime.ui.screen.worship.WorshipViewModel
import com.rudra.prayerallthetime.ui.screen.wuduguide.WuduGuideScreen
import com.rudra.prayerallthetime.ui.screen.wuduguide.WuduGuideViewModel
import com.rudra.prayerallthetime.ui.screen.charity.CharityScreen
import com.rudra.prayerallthetime.ui.screen.charity.CharityViewModel
import com.rudra.prayerallthetime.ui.screen.profile.ProfileScreen
import com.rudra.prayerallthetime.ui.screen.notifications.NotificationScreen
import com.rudra.prayerallthetime.ui.screen.achievements.AchievementsScreen
import com.rudra.prayerallthetime.ui.screen.community.CommunityScreen
import com.rudra.prayerallthetime.ui.screen.habits.HabitsScreen
import com.rudra.prayerallthetime.ui.screen.habits.HabitsViewModel
import com.rudra.prayerallthetime.ui.screen.duas.DuasScreen
import com.rudra.prayerallthetime.ui.screen.duas.DuasViewModel
import com.rudra.prayerallthetime.ui.theme.*
import com.rudra.prayerallthetime.ui.theme.IslamicGold

@Composable
fun PremiumNavigation() {
    val navController = rememberNavController()
    val prayerViewModel: PrayerViewModel = hiltViewModel()
    val dashboardViewModel: DashboardViewModel = hiltViewModel()
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val quranHadithViewModel: QuranHadithViewModel = hiltViewModel()
    val ramadanViewModel: RamadanViewModel = hiltViewModel()
    val worshipViewModel: WorshipViewModel = hiltViewModel()
    val familyViewModel: FamilyViewModel = hiltViewModel()
    val qiblaViewModel: QiblaViewModel = hiltViewModel()
    val calendarViewModel: CalendarViewModel = hiltViewModel()
    val tahajjudViewModel: TahajjudViewModel = hiltViewModel()
    val wuduGuideViewModel: WuduGuideViewModel = hiltViewModel()
    val habitsViewModel: HabitsViewModel = hiltViewModel()
    val duasViewModel: DuasViewModel = hiltViewModel()
    val charityViewModel: CharityViewModel = hiltViewModel()
    val exploreViewModel: ExploreViewModel = hiltViewModel()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    var showPremiumDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            if (currentDestination?.route !in listOf(Screen.Dashboard.route, Screen.QuranHadith.route, Screen.Prayers.route, Screen.Ramadan.route, Screen.Community.route, Screen.Explore.route)) {
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
                PremiumFloatingActionButton(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Dashboard.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = {
                androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(300)) +
                androidx.compose.animation.slideInHorizontally(
                    initialOffsetX = { 300 },
                    animationSpec = androidx.compose.animation.core.tween(300)
                )
            },
            exitTransition = {
                androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(300)) +
                androidx.compose.animation.slideOutHorizontally(
                    targetOffsetX = { -300 },
                    animationSpec = androidx.compose.animation.core.tween(300)
                )
            },
            popEnterTransition = {
                androidx.compose.animation.fadeIn(animationSpec = androidx.compose.animation.core.tween(300)) +
                androidx.compose.animation.slideInHorizontally(
                    initialOffsetX = { -300 },
                    animationSpec = androidx.compose.animation.core.tween(300)
                )
            },
            popExitTransition = {
                androidx.compose.animation.fadeOut(animationSpec = androidx.compose.animation.core.tween(300)) +
                androidx.compose.animation.slideOutHorizontally(
                    targetOffsetX = { 300 },
                    animationSpec = androidx.compose.animation.core.tween(300)
                )
            }
        ) {
            composable(Screen.Dashboard.route) {
                CompleteDashboardScreen(
                    dashboardViewModel = dashboardViewModel,
                    navController = navController
                )
            }

            composable(Screen.Explore.route) {
                ExploreScreen(navController = navController)
            }

            composable(Screen.Prayers.route) {
                PrayersScreen(
                    prayerViewModel = prayerViewModel,
                    navController = navController
                )
            }
            
            composable(Screen.PrayerTimes.route) {
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
                    viewModel = ramadanViewModel
                )
            }

            composable(Screen.Community.route) {
                CommunityScreen(navController = navController, prayerViewModel = prayerViewModel)
            }

            composable(Screen.Worship.route) {
                WorshipScreen(navController = navController, prayerViewModel = prayerViewModel)
            }

            composable(Screen.Family.route) {
                FamilyScreen(navController = navController, viewModel = familyViewModel)
            }

            composable(Screen.Qibla.route) {
                QiblaScreen(
                    navController = navController,
                    prayerViewModel = prayerViewModel
                )
            }

            composable(Screen.Calendar.route) {
                CalendarScreen(
                    navController = navController,
                    prayerViewModel = prayerViewModel
                )
            }

            composable(Screen.Wudu.route) {
                WuduGuideScreen(navController = navController)
            }

            composable(Screen.Tahajjud.route) {
                TahajjudScreen(navController = navController, prayerViewModel = prayerViewModel)
            }

            composable(Screen.Tasbeeh.route) {
                WorshipScreen(
                    navController = navController,
                    prayerViewModel = prayerViewModel
                )
            }
            
            composable(Screen.Charity.route) { 
                CharityScreen(navController = navController, viewModel = charityViewModel) 
            }
            
            composable(Screen.Profile.route) { 
                ProfileScreen(navController = navController, prayerViewModel = prayerViewModel) 
            }
            
            composable(Screen.Notifications.route) { 
                NotificationScreen(navController = navController) 
            }
            
            composable(Screen.Achievements.route) { 
                AchievementsScreen(navController = navController, prayerViewModel = prayerViewModel) 
            }
            
            composable(Screen.Streaks.route) { 
                AnalyticsScreen(navController = navController, prayerViewModel = prayerViewModel) 
            }
            
            composable(Screen.Charts.route) { 
                AnalyticsScreen(navController = navController, prayerViewModel = prayerViewModel) 
            }
            
            composable(Screen.StreakDetails.route) { 
                AnalyticsScreen(navController = navController, prayerViewModel = prayerViewModel) 
            }
            
            composable(Screen.RamadanTimer.route) { 
                RamadanScreen(navController = navController, viewModel = ramadanViewModel) 
            }
            
            composable(Screen.Taraweeh.route) { 
                RamadanScreen(navController = navController, viewModel = ramadanViewModel)
            }
            
            composable(Screen.FamilyMember.route) { 
                FamilyScreen(navController = navController, viewModel = familyViewModel) 
            }
            
            composable(Screen.Hadith.route) {
                com.rudra.prayerallthetime.ui.screen.hadith.HadithScreen(navController = navController)
            }

            composable(Screen.Report.route) {
                ReportScreen(navController = navController)
            }

            composable(Screen.Habits.route) {
                HabitsScreen(navController = navController, viewModel = habitsViewModel)
            }

            composable(Screen.Duas.route) {
                DuasScreen(navController = navController, viewModel = duasViewModel)
            }
            
            composable(Screen.NearbyMosques.route) {
                NearbyMosquesScreen(navController = navController, viewModel = exploreViewModel)
            }

            composable(Screen.SurahList.route) {
                SurahListScreen(navController = navController)
            }

            composable(
                route = Screen.SurahDetail.route,
                arguments = listOf(navArgument("surahNumber") { type = NavType.IntType })
            ) { backStackEntry ->
                val surahNumber = backStackEntry.arguments?.getInt("surahNumber") ?: 1
                SurahDetailScreen(surahNumber = surahNumber, navController = navController)
            }
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
        Screen.allScreens().find { it.route == destination.route || (it.route.contains("{") && destination.route?.startsWith(it.route.substringBefore("{")) == true) }?.title ?: "Salaam"
    } ?: "Salaam"

    CenterAlignedTopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(EmeraldGreen, EmeraldGreenMedium)
                            )
                        )
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Mosque,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold
                    ),
                    color = TextPrimaryDark
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MidnightBlue,
            scrolledContainerColor = MidnightBlueDark,
            titleContentColor = TextPrimaryDark
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
    val bottomNavItems = Screen.bottomNavItems
    
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        modifier = Modifier
            .shadow(
                elevation = 24.dp,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
                clip = true
            )
            .navigationBarsPadding()
    ) {
        bottomNavItems.forEach { screen ->
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
                    indicatorColor = EmeraldGreen.copy(alpha = 0.15f),
                    selectedIconColor = EmeraldGreen,
                    unselectedIconColor = TextSecondaryDark.copy(alpha = 0.6f),
                    selectedTextColor = EmeraldGreen,
                    unselectedTextColor = TextSecondaryDark.copy(alpha = 0.6f)
                ),
                icon = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(
                                    if (selected) EmeraldGreen.copy(alpha = 0.15f)
                                    else Color.Transparent
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            screen.icon?.let {
                                Icon(
                                    imageVector = screen.icon,
                                    contentDescription = screen.title,
                                    modifier = Modifier.size(24.dp),
                                    tint = if (selected) EmeraldGreen else TextSecondaryDark.copy(alpha = 0.6f)
                                )
                            } ?: screen.emoji?.let {
                                Text(
                                    text = it, 
                                    fontSize = 24.sp,
                                    color = if (selected) EmeraldGreen else TextSecondaryDark.copy(alpha = 0.6f)
                                )
                            }
                        }
                        if (selected) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(4.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldGreen)
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = screen.title,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            )
        }
    }
}

@Composable
fun PremiumFloatingActionButton(navController: NavHostController) {
    FloatingActionButton(
        onClick = { navController.navigate(Screen.Report.route) },
        containerColor = IslamicGold,
        contentColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(bottom = 16.dp)
    ) {
        Icon(Icons.Default.Assessment, contentDescription = "Report")
    }
}
