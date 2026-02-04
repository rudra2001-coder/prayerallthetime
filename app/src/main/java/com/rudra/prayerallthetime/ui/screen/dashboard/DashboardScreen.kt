package com.rudra.prayerallthetime.ui.screen.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
    val nextPrayerArabicName by dashboardViewModel.nextPrayerArabicName.collectAsState()
    val countdown by dashboardViewModel.countdown.collectAsState()
    val nextPrayerMillis by dashboardViewModel.nextPrayerMillis.collectAsState()
    val sunriseTime by dashboardViewModel.sunriseTime.collectAsState()
    val hijriDate by dashboardViewModel.hijriDate.collectAsState()
    val gregorianDate by dashboardViewModel.gregorianDate.collectAsState()
    val cityName by dashboardViewModel.cityName.collectAsState()
    val completionRate by dashboardViewModel.completionRate.collectAsState()
    val currentStreak by dashboardViewModel.currentStreak.collectAsState()
    val weeklyCompletion by dashboardViewModel.weeklyCompletion.collectAsState()
    val completedPrayers by dashboardViewModel.completedPrayers.collectAsState()

    // Worship Tools Data
    val tasbeehCount by dashboardViewModel.tasbeehCount.collectAsState()
    val currentSurah by dashboardViewModel.currentSurah.collectAsState()
    val wuduStatus by dashboardViewModel.wuduStatus.collectAsState()
    val tahajjudTime by dashboardViewModel.tahajjudTimeStr.collectAsState()

    // New Data
    val habits by dashboardViewModel.habits.collectAsState()
    val dailyDua by dashboardViewModel.dailyDua.collectAsState()

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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF0F1B4C)), 
                actions = {
                    IconButton(onClick = { navController.navigate(Screen.Ramadan.route) }) {
                        Icon(Icons.Default.Mosque, contentDescription = "Ramadan", tint = IslamicGold)
                    }
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
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1st: HeroCard
            item {
                PremiumHeroCard(
                    nextPrayerName = nextPrayerName,
                    nextPrayerArabicName = nextPrayerArabicName,
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
                    onPrayerInfoClick = { /* Handle prayer info click */ }
                )
            }

            // Quick Access Section (New)
            item {
                QuickAccessSection(navController)
            }

            // 2: WeeklyStreakTracker
            item {
                WeeklyStreakTracker(
                    streakCount = currentStreak,
                    weeklyCompletion = weeklyCompletion,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Worship Tools Panel
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

            // Prayer Clocks
            item {
                PrayerClocksSection(
                    prayers = prayers,
                    onPrayerClick = { prayer ->
                        dashboardViewModel.togglePrayerState(prayer)
                    },
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            // Daily Motivation
            item {
                DailyMotivationSection(
                    hadithArabic = dashboardViewModel.hadithArabic.collectAsState().value,
                    hadithEnglish = dashboardViewModel.hadithEnglish.collectAsState().value,
                    hadithInfo = dashboardViewModel.hadithInfo.collectAsState().value
                )
            }

            // Dua of the Day
            item {
                DuaOfTheDaySection(dua = dailyDua)
            }

            // Progress Card
            item {
                EnhancedProgressCard(
                    completionPercentage = completionRate,
                    completed = prayers.count { it.isPrayed },
                    total = prayers.size,
                    onAnalyticsClick = { navController.navigate(Screen.Analytics.route) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Goal of the Day
            item {
                GoalOfTheDayCard(
                    habit = habits.firstOrNull(),
                    onActionClick = { habitId ->
                        dashboardViewModel.incrementHabit(habitId)
                    }
                )
            }

            // Quick Insights
            item {
                QuickInsights(
                    insights = listOf(
                        InsightData(
                            title = "Performance",
                            description = if (completionRate > 0.8f) "Excellent consistency today!" else "Keep going to maintain your streak.",
                            icon = Icons.Default.TrendingUp,
                            backgroundColor = Color(0xFFE8F5E9),
                            tintColor = Color(0xFF4CAF50)
                        ),
                        InsightData(
                            title = "Ramadan",
                            description = "Don't forget to check your Ramadan tracker.",
                            icon = Icons.Default.Mosque,
                            backgroundColor = Color(0xFFFFF3E0),
                            tintColor = IslamicGold
                        )
                    )
                )
            }
        }
    }
}

@Composable
fun QuickAccessSection(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuickAccessItem(
            icon = Icons.Default.MenuBook,
            label = "Quran",
            color = Color(0xFF8B4513),
            modifier = Modifier.weight(1f),
            onClick = { navController.navigate(Screen.QuranHadith.route) }
        )
        QuickAccessItem(
            icon = Icons.Default.AutoAwesome,
            label = "Dua",
            color = Color(0xFF4ECDC4),
            modifier = Modifier.weight(1f),
            onClick = { navController.navigate(Screen.Duas.route) }
        )
        QuickAccessItem(
            icon = Icons.Default.Mosque,
            label = "Ramadan",
            color = IslamicGold,
            modifier = Modifier.weight(1f),
            onClick = { navController.navigate(Screen.Ramadan.route) }
        )
    }
}

@Composable
fun QuickAccessItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun DailyMotivationSection(
    hadithArabic: String,
    hadithEnglish: String,
    hadithInfo: String
) {
    Card(
        modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.FormatQuote, contentDescription = null, tint = IslamicGold, modifier = Modifier.size(32.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = hadithArabic,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = Color(0xFF2C3E50)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = hadithEnglish,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = Color.Gray
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "— $hadithInfo",
                style = MaterialTheme.typography.labelSmall,
                color = IslamicGold,
                fontWeight = FontWeight.Bold
            )
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
