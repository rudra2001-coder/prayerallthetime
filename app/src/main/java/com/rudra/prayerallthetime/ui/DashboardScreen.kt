package com.rudra.prayerallthetime.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.batoulapps.adhan.Coordinates
import com.rudra.prayerallthetime.data.Prayer
import com.rudra.prayerallthetime.ui.components.*
import com.rudra.prayerallthetime.ui.navigation.Screen
import com.rudra.prayerallthetime.ui.theme.IslamicGold
import com.rudra.prayerallthetime.ui.theme.PrayerAllTheTimeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PremiumDashboardScreen(
    prayerViewModel: PrayerViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val prayers by prayerViewModel.prayers.collectAsState()
    val nextPrayerName by prayerViewModel.nextPrayerName.collectAsState()
    val countdown by prayerViewModel.countdown.collectAsState()
    val sunriseTime by prayerViewModel.sunriseTime.collectAsState()
    val hijriDate by prayerViewModel.hijriDate.collectAsState()
    val ayatArabic by prayerViewModel.ayatArabic.collectAsState()
    val ayatEnglish by prayerViewModel.ayatEnglish.collectAsState()
    val surahInfo by prayerViewModel.surahInfo.collectAsState()
    val gregorianDate by prayerViewModel.gregorianDate.collectAsState()
    val cityName by prayerViewModel.cityName.collectAsState()
    val isRamadan by prayerViewModel.isRamadan.collectAsState()
    val tasbeehCount by prayerViewModel.tasbeehCount.collectAsState()
    val taraweehCount by prayerViewModel.taraweehCount.collectAsState()
    val currentStreak by prayerViewModel.currentStreak.collectAsState()
    val weeklyDayData by prayerViewModel.weeklyDayData.collectAsState()
    val completionRate by prayerViewModel.completionRate.collectAsState()
    val isAyatBookmarked by prayerViewModel.isAyatBookmarked.collectAsState()
    val isHadithBookmarked by prayerViewModel.isHadithBookmarked.collectAsState()

    val prayedCount = prayers.count { it.isPrayed }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
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
                            Text(
                                text = "🕌",
                                fontSize = 18.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Salaam",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.White
                            )
                            Text(
                                text = "Your Spiritual Companion",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2C3E50)
                ),
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Screen.Notifications.route) }
                    ) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Color(0xFFFF6B6B)
                                ) {
                                    Text("3")
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Notifications,
                                contentDescription = "Notifications",
                                tint = Color.White
                            )
                        }
                    }

                    IconButton(
                        onClick = { navController.navigate(Screen.Profile.route) }
                    ) {
                        Icon(
                            Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = Color.White
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF0F4FF),
                            Color(0xFFE8F0FF),
                            Color(0xFFF8F5F0)
                        )
                    )
                ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Hero Section with Next Prayer
            item {
                PremiumHeroCard(
                    nextPrayerName = nextPrayerName,
                    countdown = countdown,
                    sunriseTime = sunriseTime,
                    hijriDate = hijriDate,
                    gregorianDate = gregorianDate,
                    cityName = cityName,
                    prayerTime = prayerViewModel.getNextPrayerTime(),
                    isAlarmSet = prayerViewModel.isAlarmSet(),
                    qiblaDirection = prayerViewModel.getQiblaDirection().toInt().toString() + "°",
                    onDetectLocationClick = { prayerViewModel.updateLocation(23.6556256, 90.6257555) },
                    onAlarmClick = { prayerViewModel.toggleAlarm() },
                    onCalendarClick = { navController.navigate(Screen.Calendar.route) },
                    onLocationClick = { navController.navigate(Screen.Qibla.route) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Today's Progress Section
            item {
                PremiumTodayProgressCard(
                    prayers = prayers,
                    prayedCount = prayedCount,
                    totalPrayers = prayers.size,
                    onPrayerClicked = { prayer ->
                        prayerViewModel.togglePrayerState(prayer)
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Quick Stats Row
            item {
                LazyRow(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        QuickStatCard(
                            title = "Prayer Streak",
                            value = "$currentStreak days",
                            icon = "🔥",
                            color = Color(0xFFFF6B6B),
                            onClick = { navController.navigate(Screen.Analytics.route) }
                        )
                    }
                    item {
                        QuickStatCard(
                            title = "Completion",
                            value = "${(completionRate * 100).toInt()}%",
                            subValue = "overall",
                            icon = "⭐",
                            color = Color(0xFFD4AF37),
                            onClick = { navController.navigate(Screen.Analytics.route) }
                        )
                    }
                    item {
                        QuickStatCard(
                            title = "Tasbeeh",
                            value = "$tasbeehCount",
                            icon = "📿",
                            color = Color(0xFF8B4513),
                            onClick = { navController.navigate(Screen.Tasbeeh.route) }
                        )
                    }
                    item {
                        QuickStatCard(
                            title = "Taraweeh",
                            value = "$taraweehCount",
                            subValue = "/ 20",
                            icon = "🕌",
                            color = Color(0xFF4ECDC4),
                            onClick = { navController.navigate(Screen.Ramadan.route) }
                        )
                    }
                }
            }

            // Worship Tools Section
            item {
                PremiumWorshipToolsPanel(
                    tasbeehCount = tasbeehCount,
                    qiblaDistance = "Kaaba Direction",
                    currentSurah = "Al-Fatihah",
                    wuduStatus = true,
                    tahajjudTime = "03:45 AM",
                    onTasbeehClick = { navController.navigate(Screen.Tasbeeh.route) },
                    onQiblaClick = { navController.navigate(Screen.Qibla.route) },
                    onQuranClick = { navController.navigate(Screen.QuranHadith.route) },
                    onWuduClick = { navController.navigate(Screen.Wudu.route) },
                    onPrayerTimesClick = { navController.navigate(Screen.Prayers.route) },
                    onTahajjudClick = { navController.navigate(Screen.Tahajjud.route) }
                )
            }

            // Daily Inspiration Section
            item {
                PremiumAyatOfTheDayCard(
                    arabicText = ayatArabic,
                    englishText = ayatEnglish,
                    translation = "Verse information from Quran",
                    surahInfo = surahInfo,
                    isBookmarked = isAyatBookmarked,
                    onBookmarkClick = { prayerViewModel.toggleAyatBookmark() },
                    onShareClick = { prayerViewModel.shareContent("$ayatArabic\n\n$ayatEnglish\n($surahInfo)") },
                    onClick = { navController.navigate(Screen.QuranHadith.route) }
                )
            }

            // Analytics & Stats Section
            item {
                PremiumAnalyticsCard(
                    onClick = { navController.navigate(Screen.Analytics.route) },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            // Conditional Ramadan Section
            if (isRamadan) {
                item {
                    val fastingCountdown by prayerViewModel.fastingCountdown.collectAsState()
                    val suhoorTime by prayerViewModel.suhoorTime.collectAsState()
                    val iftarTime by prayerViewModel.iftarTime.collectAsState()
                    val ramadanDay by prayerViewModel.ramadanDay.collectAsState()

                    PremiumRamadanFastingCard(
                        dayNumber = ramadanDay,
                        totalDays = 30,
                        timeUntilIftar = fastingCountdown,
                        suhoorTime = suhoorTime,
                        iftarTime = iftarTime,
                        taraweehCompleted = taraweehCount,
                        taraweehTotal = 20,
                        fastingStreak = currentStreak,
                        moonPhase = MoonPhase.FIRST_QUARTER,
                        onViewDetails = { navController.navigate(Screen.Ramadan.route) },
                        onIftarTimer = { navController.navigate(Screen.RamadanTimer.route) },
                        onTaraweehClick = { navController.navigate(Screen.Taraweeh.route) }
                    )
                }
            }

            // Charts Section
            item {
                PrayerCharts(prayerViewModel = prayerViewModel)
            }

            // Streaks Section
            item {
                Streaks(prayerViewModel = prayerViewModel)
            }

            // Weekly Stats Section
            item {
                WeeklyStats(prayerViewModel = prayerViewModel)
            }

            // Hadith of the Day
            item {
                PremiumHadithOfTheDayCard(
                    isBookmarked = isHadithBookmarked,
                    onBookmarkClick = { prayerViewModel.toggleHadithBookmark() },
                    onShareClick = { prayerViewModel.shareContent("The best among you are those who learn the Quran and teach it. (Sahih al-Bukhari)") },
                    onAudioClick = { prayerViewModel.playAudio("خَيْرُكُمْ مَنْ تَعَلَّمَ الْقُرْآنَ وَعَلَّمَهُ") },
                    onClick = { navController.navigate(Screen.QuranHadith.route) }
                )
            }

            // Badges Section
            item {
                Badges(prayerViewModel = prayerViewModel)
            }
        }
    }
}

@Composable
fun PremiumTodayProgressCard(
    prayers: List<Prayer>,
    prayedCount: Int,
    totalPrayers: Int,
    onPrayerClicked: (Prayer) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xFFF8F5F0), Color(0xFFF0EDE8))
                    )
                )
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "📅 Today's Progress",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2C3E50)
                    )
                    Text(
                        text = "Complete your daily prayers",
                        fontSize = 12.sp,
                        color = Color(0xFF2C3E50).copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF8B4513).copy(alpha = 0.1f))
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "${if (totalPrayers > 0) (prayedCount.toFloat() / totalPrayers.toFloat() * 100).toInt() else 0}%",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B4513)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            LinearProgressIndicator(
                progress = if (totalPrayers > 0) prayedCount.toFloat() / totalPrayers.toFloat() else 0f,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(10.dp)
                    .clip(RoundedCornerShape(5.dp)),
                color = Color(0xFF8B4513),
                trackColor = Color(0xFF2C3E50).copy(alpha = 0.1f)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Completed",
                    fontSize = 12.sp,
                    color = Color(0xFF2C3E50).copy(alpha = 0.6f)
                )
                Text(
                    text = "$prayedCount/$totalPrayers prayers",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B4513)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(prayers) { prayer ->
                    PrayerChip(
                        prayer = prayer,
                        onClick = { onPrayerClicked(prayer) }
                    )
                }
            }
        }
    }
}

@Composable
fun PrayerChip(
    prayer: Prayer,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (prayer.isPrayed) Color(0xFF4ECDC4).copy(alpha = 0.2f)
            else Color(0xFF2C3E50).copy(alpha = 0.1f)
        ),
        border = BorderStroke(
            1.dp,
            if (prayer.isPrayed) Color(0xFF4ECDC4) else Color.Transparent
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        if (prayer.isPrayed) Color(0xFF4ECDC4)
                        else Color(0xFF2C3E50).copy(alpha = 0.2f)
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = prayer.emoji,
                    fontSize = 16.sp
                )
            }

            Text(
                text = prayer.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2C3E50)
            )

            Text(
                text = prayer.time,
                fontSize = 11.sp,
                color = Color(0xFF2C3E50).copy(alpha = 0.6f)
            )

            if (prayer.isPrayed) {
                Text(
                    text = "✓ Prayed",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4ECDC4)
                )
            }
        }
    }
}

@Composable
fun QuickStatCard(
    title: String,
    value: String,
    subValue: String = "",
    icon: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(120.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .background(color.copy(alpha = 0.1f))
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = icon,
                fontSize = 24.sp
            )

            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )

            Text(
                text = title,
                fontSize = 12.sp,
                color = Color(0xFF2C3E50).copy(alpha = 0.8f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            if (subValue.isNotEmpty()) {
                Text(
                    text = subValue,
                    fontSize = 10.sp,
                    color = Color(0xFF2C3E50).copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun DashboardScreen(
    prayerViewModel: PrayerViewModel,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    PremiumDashboardScreen(
        prayerViewModel = prayerViewModel,
        navController = navController,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    PrayerAllTheTimeTheme {
        DashboardScreen(
            prayerViewModel = viewModel(),
            navController = rememberNavController()
        )
    }
}
