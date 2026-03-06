package com.rudra.prayerallthetime.ui.screen.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
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
import com.rudra.prayerallthetime.ui.theme.*
import com.rudra.prayerallthetime.ui.theme.ExtendedTypography

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
    
    // Animation states
    var isContentVisible by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        isContentVisible = true
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(AppGradients.goldGradient)
                                .padding(10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mosque,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Prayer Tracker Pro",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            )
                            Text(
                                text = "As-salamu alaykum",
                                style = MaterialTheme.typography.labelMedium,
                                color = TextSecondaryDark
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MidnightBlue,
                    scrolledContainerColor = MidnightBlueDark
                ),
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Screen.Ramadan.route) },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(IslamicGold.copy(alpha = 0.15f))
                            .padding(4.dp)
                    ) {
                        Icon(
                            Icons.Default.Mosque, 
                            contentDescription = "Ramadan", 
                            tint = IslamicGold,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { navController.navigate(Screen.Notifications.route) },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                            .padding(4.dp)
                    ) {
                        BadgedBox(
                            badge = { 
                                Badge(
                                    containerColor = ErrorColor,
                                    contentColor = Color.White
                                ) { Text("3", fontSize = 10.sp) } 
                            }
                        ) {
                            Icon(
                                Icons.Default.Notifications, 
                                contentDescription = "Notifications", 
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        },
        containerColor = MidnightBlue
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(MidnightBlue),
            contentPadding = PaddingValues(bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // 1st: HeroCard
            item {
                AnimatedVisibility(
                    visible = isContentVisible,
                    enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 })
                ) {
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
            }

            // Quick Access Section
            item {
                AnimatedVisibility(
                    visible = isContentVisible,
                    enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + 
                            slideInVertically(initialOffsetY = { it / 3 })
                ) {
                    QuickAccessSection(navController)
                }
            }

            // 2: WeeklyStreakTracker
            item {
                AnimatedVisibility(
                    visible = isContentVisible,
                    enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + 
                            slideInVertically(initialOffsetY = { it / 3 })
                ) {
                    WeeklyStreakTracker(
                        streakCount = currentStreak,
                        weeklyCompletion = weeklyCompletion,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // Worship Tools Panel
            item {
                AnimatedVisibility(
                    visible = isContentVisible,
                    enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + 
                            slideInVertically(initialOffsetY = { it / 3 })
                ) {
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
            }

            // Prayer Clocks
            item {
                AnimatedVisibility(
                    visible = isContentVisible,
                    enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + 
                            slideInVertically(initialOffsetY = { it / 3 })
                ) {
                    PrayerClocksSection(
                        prayers = prayers,
                        onPrayerClick = { prayer ->
                            dashboardViewModel.togglePrayerState(prayer)
                        },
                        modifier = Modifier.padding(horizontal = 4.dp)
                    )
                }
            }

            // Daily Motivation
            item {
                AnimatedVisibility(
                    visible = isContentVisible,
                    enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + 
                            slideInVertically(initialOffsetY = { it / 3 })
                ) {
                    DailyMotivationSection(
                        hadithArabic = dashboardViewModel.hadithArabic.collectAsState().value,
                        hadithEnglish = dashboardViewModel.hadithEnglish.collectAsState().value,
                        hadithInfo = dashboardViewModel.hadithInfo.collectAsState().value
                    )
                }
            }

            // Dua of the Day
            item {
                AnimatedVisibility(
                    visible = isContentVisible,
                    enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + 
                            slideInVertically(initialOffsetY = { it / 3 })
                ) {
                    DuaOfTheDaySection(dua = dailyDua)
                }
            }

            // Progress Card
            item {
                AnimatedVisibility(
                    visible = isContentVisible,
                    enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + 
                            slideInVertically(initialOffsetY = { it / 3 })
                ) {
                    EnhancedProgressCard(
                        completionPercentage = completionRate,
                        completed = prayers.count { it.isPrayed },
                        total = prayers.size,
                        onAnalyticsClick = { navController.navigate(Screen.Analytics.route) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }

            // Goal of the Day
            item {
                AnimatedVisibility(
                    visible = isContentVisible,
                    enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + 
                            slideInVertically(initialOffsetY = { it / 3 })
                ) {
                    GoalOfTheDayCard(
                        habit = habits.firstOrNull(),
                        onActionClick = { habitId ->
                            dashboardViewModel.incrementHabit(habitId)
                        }
                    )
                }
            }

            // Quick Insights
            item {
                AnimatedVisibility(
                    visible = isContentVisible,
                    enter = fadeIn(animationSpec = spring(stiffness = Spring.StiffnessLow)) + 
                            slideInVertically(initialOffsetY = { it / 3 })
                ) {
                    QuickInsights(
                        insights = listOf(
                            InsightData(
                                title = "Performance",
                                description = if (completionRate > 0.8f) "Excellent consistency today!" else "Keep going to maintain your streak.",
                                icon = Icons.Default.TrendingUp,
                                backgroundColor = SuccessLight,
                                tintColor = SuccessColor
                            ),
                            InsightData(
                                title = "Ramadan",
                                description = "Don't forget to check your Ramadan tracker.",
                                icon = Icons.Default.Mosque,
                                backgroundColor = IslamicGoldLight,
                                tintColor = IslamicGold
                            )
                        )
                    )
                }
            }
            
            // Bottom Spacer
            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

@Composable
fun QuickAccessSection(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Quick Access",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            ),
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickAccessItem(
                icon = Icons.Default.MenuBook,
                label = "Quran",
                color = QuranColor,
                backgroundColor = QuranLight.copy(alpha = 0.2f),
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.QuranHadith.route) }
            )
            QuickAccessItem(
                icon = Icons.Default.AutoAwesome,
                label = "Dua",
                color = InfoColor,
                backgroundColor = InfoLight.copy(alpha = 0.2f),
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.Duas.route) }
            )
            QuickAccessItem(
                icon = Icons.Default.Mosque,
                label = "Ramadan",
                color = RamadanColor,
                backgroundColor = RamadanLight.copy(alpha = 0.2f),
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.Ramadan.route) }
            )
            QuickAccessItem(
                icon = Icons.Default.Favorite,
                label = "Charity",
                color = CharityColor,
                backgroundColor = CharityLight.copy(alpha = 0.2f),
                modifier = Modifier.weight(1f),
                onClick = { navController.navigate(Screen.Charity.route) }
            )
        }
    }
}

@Composable
fun QuickAccessItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    color: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "scale"
    )
    
    Surface(
        onClick = onClick,
        modifier = modifier.scale(scale),
        shape = RoundedCornerShape(16.dp),
        color = MidnightBlueCard,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(backgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    icon, 
                    contentDescription = null, 
                    tint = color, 
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                label, 
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.Medium,
                    color = TextPrimaryDark
                )
            )
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
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = ShadowDark
            ),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightBlueCard)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Quote icon with decorative background
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(IslamicGold.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.FormatQuote, 
                    contentDescription = null, 
                    tint = IslamicGold, 
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Arabic text
            if (hadithArabic.isNotBlank()) {
                Text(
                    text = hadithArabic,
                    style = ExtendedTypography.arabicMedium.copy(
                        color = TextPrimaryDark,
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
            
            // English translation
            Text(
                text = hadithEnglish,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = TextSecondaryDark,
                    lineHeight = 26.sp
                ),
                textAlign = TextAlign.Center
            )
            
            if (hadithInfo.isNotBlank()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "— $hadithInfo",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = IslamicGold,
                        fontWeight = FontWeight.SemiBold
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
