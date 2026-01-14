package com.rudra.prayerallthetime.ui.screen.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.rudra.prayerallthetime.data.Prayer
import com.rudra.prayerallthetime.ui.navigation.Screen
import com.rudra.prayerallthetime.ui.screen.dashboard.components.*
import com.rudra.prayerallthetime.ui.theme.IslamicGold
import com.rudra.prayerallthetime.ui.theme.PrayerAllTheTimeTheme

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CompleteDashboardScreen(
    dashboardViewModel: DashboardViewModel = hiltViewModel(),
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val prayers by dashboardViewModel.prayers.collectAsState()
    val nextPrayerName by dashboardViewModel.nextPrayerName.collectAsState()
    val countdown by dashboardViewModel.countdown.collectAsState()
    val nextPrayerMillis by dashboardViewModel.nextPrayerMillis.collectAsState()
    val sunriseTime by dashboardViewModel.sunriseTime.collectAsState()
    val hijriDate by dashboardViewModel.hijriDate.collectAsState()
    val gregorianDate by dashboardViewModel.gregorianDate.collectAsState()
    val cityName by dashboardViewModel.cityName.collectAsState()
    val completionRate by dashboardViewModel.completionRate.collectAsState()
    val currentStreak by dashboardViewModel.currentStreak.collectAsState()
    val completedPrayers by dashboardViewModel.completedPrayers.collectAsState()

    // Worship Tools Data
    val tasbeehCount by dashboardViewModel.tasbeehCount.collectAsState()
    val currentSurah by dashboardViewModel.currentSurah.collectAsState()
    val wuduStatus by dashboardViewModel.wuduStatus.collectAsState()
    val tahajjudTime by dashboardViewModel.tahajjudTimeStr.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(IslamicGold, Color(0xFFFFD93D))
                                    )
                                )
                                .padding(8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "🕌", fontSize = 18.sp)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Prayer Tracker Pro",
                                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                text = "Salaam, Welcome Back",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F1B4C)), // Unified with HeroCard
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Notifications.route) }) {
                        BadgedBox(
                            badge = { Badge(containerColor = Color(0xFFFF6B6B)) { Text("3") } }
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = "Notifications", tint = Color.White)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(Color(0xFFF8F9FA)),
            contentPadding = PaddingValues(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // 1st: HeroCard
            item {
                PremiumHeroCard(
                    nextPrayerName = nextPrayerName,
                    countdown = countdown,
                    nextPrayerMillis = nextPrayerMillis,
                    sunriseTime = sunriseTime,
                    hijriDate = hijriDate,
                    gregorianDate = gregorianDate,
                    cityName = cityName,
                    prayerTime = dashboardViewModel.getNextPrayerTime(),
                    isAlarmSet = dashboardViewModel.isAlarmSet(),
                    qiblaDirection = try { dashboardViewModel.getQiblaDirection().toInt().toString() + "°" } catch (e: Exception) { "90°" },
                    prayerProgress = completionRate,
                    onDetectLocationClick = { dashboardViewModel.refreshLocation() },
                    onAlarmClick = { dashboardViewModel.toggleAlarm() },
                    onCalendarClick = { navController.navigate(Screen.Calendar.route) },
                    onLocationClick = { navController.navigate(Screen.Qibla.route) },
                    onPrayerInfoClick = { /* Handle prayer info click */ },
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }

            // 2: WeeklyStreakTracker
            item {
                WeeklyStreakTracker(
                    streakCount = currentStreak,
                    weeklyCompletion = listOf(true, true, true, true, true, false, true), // Sample data
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // 3: PremiumStreaksCard (Interactive)
            item {
                PremiumStreaksCard(
                    currentStreakData = StreakData(
                        id = 1,
                        name = "Daily Prayer Streak",
                        description = "Consecutive days of completing all 5 prayers",
                        days = currentStreak,
                        color = Color(0xFFFF6B6B),
                        icon = Icons.Default.Fireplace,
                        nextMilestone = 30
                    ),
                    completedPrayers = completedPrayers,
                    onTogglePrayer = { prayerName ->
                        dashboardViewModel.togglePrayerByName(prayerName)
                    },
                    onAddDay = {
                        dashboardViewModel.addTodayToStreak()
                    },
                    onViewAll = { navController.navigate(Screen.Streaks.route) },
                    onStreakClick = { streak ->
                        navController.navigate(Screen.StreakDetails.route + "/${streak.id}")
                    }
                )
            }

            // 4: Worship Tools Panel
            item {
                PremiumWorshipToolsPanel(
                    tasbeehCount = tasbeehCount,
                    currentSurah = currentSurah,
                    wuduStatus = wuduStatus,
                    tahajjudTime = tahajjudTime,
                    onTasbeehClick = { navController.navigate(Screen.Tasbeeh.route) },
                    onQiblaClick = { navController.navigate(Screen.Qibla.route) },
                    onQuranClick = { navController.navigate(Screen.QuranHadith.route) },
                    onWuduClick = { navController.navigate(Screen.Wudu.route) },
                    onTahajjudClick = { navController.navigate(Screen.Tahajjud.route) },
                    onPrayerTimesClick = { navController.navigate(Screen.PrayerTimes.route) }
                )
            }

            // 5: PrayerClocksSection
            item {
                PrayerClocksSection(
                    prayers = prayers,
                    onPrayerClick = { prayer ->
                        dashboardViewModel.togglePrayerState(prayer)
                    },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            // 6: PrayerTimeline
            item {
                Card(
                    modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    PrayerTimeline(prayers = prayers)
                }
            }

            // 7: EnhancedProgressCard
            item {
                EnhancedProgressCard(
                    completionPercentage = completionRate,
                    completed = prayers.count { it.isPrayed },
                    total = prayers.size,
                    onAnalyticsClick = { navController.navigate(Screen.Analytics.route) },
                    modifier = Modifier.padding(horizontal = 0.dp)
                )
            }

            // 8: QuickInsights
            item {
                QuickInsights(
                    insights = listOf(
                        InsightData(
                            title = "Performance Insight",
                            description = "You're most consistent with Fajr prayers this week. Keep it up!",
                            icon = Icons.Default.TipsAndUpdates,
                            backgroundColor = Color(0xFFE3F2FD),
                            tintColor = Color(0xFF1976D2)
                        ),
                        InsightData(
                            title = "Improvement Suggestion",
                            description = "Try to log your Dhuhr prayer immediately after performing it to maintain accuracy.",
                            icon = Icons.Default.Lightbulb,
                            backgroundColor = Color(0xFFFFF3E0),
                            tintColor = Color(0xFFF57C00)
                        ),
                        InsightData(
                            title = "Streak Milestone",
                            description = "You're 3 days away from beating your personal best streak of 30 days!",
                            icon = Icons.Default.Star,
                            backgroundColor = Color(0xFFF3E5F5),
                            tintColor = Color(0xFF9C27B0)
                        )
                    )
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    val navController = rememberNavController()
    PrayerAllTheTimeTheme {
        Surface {
            Text("Dashboard Screen Preview", modifier = Modifier.padding(16.dp))
        }
    }
}
