package com.rudra.prayerallthetime.ui.screen.ramadan

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.theme.GradientEnd
import com.rudra.prayerallthetime.ui.theme.GradientStart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RamadanScreen(
    navController: NavController,
    viewModel: RamadanViewModel
) {
    val isRamadan by viewModel.isRamadan.collectAsState()
    val ramadanDay by viewModel.ramadanDay.collectAsState()
    val suhoorTime by viewModel.suhoorTime.collectAsState()
    val iftarTime by viewModel.iftarTime.collectAsState()
    val fastingCountdown by viewModel.fastingCountdown.collectAsState()
    val taraweehCount by viewModel.taraweehCount.collectAsState()
    val fastingProgress by viewModel.fastingProgress.collectAsState()
    val isFasting by viewModel.isFasting.collectAsState()
    val remainingDays by viewModel.remainingDays.collectAsState()
    val daysFasted by viewModel.daysFasted.collectAsState()
    val waterIntake by viewModel.waterIntake.collectAsState()
    val dailyGoals by viewModel.dailyGoals.collectAsState()
    val charityAmount by viewModel.charityAmount.collectAsState()
    val quranProgress by viewModel.quranProgress.collectAsState()
    val fastingStreak by viewModel.fastingStreak.collectAsState()
    val achievements by viewModel.achievements.collectAsState()
    val nextRamadanCountdown by viewModel.nextRamadanCountdown.collectAsState()
    val prayerTimes by viewModel.prayerTimes.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Filled.Mosque,
                            contentDescription = "Ramadan",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ramadan Mubarak",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                actions = {
                    IconButton(onClick = { /* Open calendar view */ }) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calendar")
                    }
                    IconButton(onClick = { /* Share progress */ }) {
                        Icon(Icons.Filled.Share, contentDescription = "Share")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.logFast() },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Filled.CheckCircle, contentDescription = "Log Today's Fast")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f),
                            MaterialTheme.colorScheme.surface
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Header Section
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .background(
                            brush = Brush.horizontalGradient(
                                colors = listOf(
                                    GradientStart,
                                    GradientEnd
                                )
                            ),
                            shape = RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = viewModel.getHijriDate(),
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.9f))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = if (isRamadan) {
                                "Ramadan Day $ramadanDay • ${(viewModel.getRamadanProgress() * 100).toInt()}% Complete"
                            } else {
                                "Ramadan starts in $nextRamadanCountdown"
                            },
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isFasting) "Currently Fasting" else "Not Fasting",
                            style = MaterialTheme.typography.bodyLarge.copy(color = Color.White.copy(alpha = 0.9f))
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "$remainingDays days remaining • $daysFasted fasted",
                            style = MaterialTheme.typography.bodyMedium.copy(color = Color.White.copy(alpha = 0.8f))
                        )
                    }
                }

                // Main Content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    FastingTimerCard(
                        suhoorTime = suhoorTime,
                        iftarTime = iftarTime,
                        countdown = fastingCountdown,
                        progress = fastingProgress,
                        isFasting = isFasting,
                        fastingStreak = fastingStreak,
                        onLogFast = { viewModel.logFast() },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    QuickStatsRow(
                        waterIntake = waterIntake,
                        prayerCount = dailyGoals["prayers"] ?: 0,
                        charityAmount = charityAmount,
                        fastingStreak = fastingStreak,
                        onWaterAdd = { viewModel.addWater() },
                        onCharityAdd = { viewModel.addCharity(10.0) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    RamadanProgressCard(
                        daysFasted = daysFasted,
                        remainingDays = remainingDays,
                        totalDays = 30,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ProgressDashboardCard(
                        daysFasted = daysFasted,
                        quranProgress = quranProgress,
                        taraweehProgress = taraweehCount / 20f,
                        charityProgress = (charityAmount / 100f).toFloat(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    PrayerTimesFullCard(
                        prayerTimes = prayerTimes,
                        currentPrayer = viewModel.getCurrentPrayer(),
                        nextPrayer = viewModel.getNextPrayer(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isRamadan) {
                        TaraweehTrackerCard(
                            taraweehCount = taraweehCount,
                            onIncrement = { viewModel.incrementTaraweeh() },
                            onReset = { viewModel.resetTaraweeh() },
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    QuranTrackerCard(
                        progress = quranProgress,
                        juzCompleted = viewModel.getJuzCompleted(),
                        onProgressUpdate = { pages -> viewModel.updateQuranProgress(pages) },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (achievements.isNotEmpty()) {
                        AchievementsSection(
                            achievements = achievements,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    HealthTipsSection(modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}

@Composable
fun FastingTimerCard(
    suhoorTime: String,
    iftarTime: String,
    countdown: String,
    progress: Float,
    isFasting: Boolean,
    fastingStreak: Int,
    onLogFast: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.shadow(8.dp, RoundedCornerShape(24.dp)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Fasting Timer", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    if (fastingStreak > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.Star, null, tint = Color(0xFFFF9800), modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("$fastingStreak day streak", style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
                Button(onClick = onLogFast, shape = RoundedCornerShape(12.dp)) {
                    Text(if (isFasting) "Log Fast" else "Mark Complete")
                }
            }
            Spacer(Modifier.height(16.dp))
            Box(Modifier.size(200.dp), contentAlignment = Alignment.Center) {
                val primaryColor = MaterialTheme.colorScheme.primary
                Canvas(Modifier.size(200.dp)) {
                    drawCircle(primaryColor.copy(0.1f), radius = size.minDimension / 2 - 10)
                }
                val animatedProgress by animateFloatAsState(progress, label = "progress")
                Canvas(Modifier.size(200.dp)) {
                    drawArc(primaryColor, -90f, 360f * animatedProgress, false, style = Stroke(12.dp.toPx(), cap = StrokeCap.Round))
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(if (isFasting) "Iftar in" else "Suhoor in", style = MaterialTheme.typography.bodyMedium)
                    Text(countdown, style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold)
                }
            }
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                TimeItem(Icons.Filled.NightShelter, "Suhoor", suhoorTime, "Ends", MaterialTheme.colorScheme.primary)
                TimeItem(Icons.Filled.Brightness7, "Iftar", iftarTime, "Starts", MaterialTheme.colorScheme.secondary)
            }
        }
    }
}

@Composable
fun TimeItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, time: String, subtitle: String, iconColor: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(Modifier.size(60.dp).clip(CircleShape).background(iconColor.copy(0.1f)), contentAlignment = Alignment.Center) {
            Icon(icon, title, tint = iconColor, modifier = Modifier.size(30.dp))
        }
        Text(title, style = MaterialTheme.typography.bodyMedium)
        Text(time, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(subtitle, style = MaterialTheme.typography.labelSmall)
    }
}

@Composable
fun QuickStatsRow(waterIntake: Int, prayerCount: Int, charityAmount: Double, fastingStreak: Int, onWaterAdd: () -> Unit, onCharityAdd: () -> Unit, modifier: Modifier = Modifier) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        QuickStatCard("Water", "${waterIntake}L", Icons.Filled.LocalDrink, Color(0xFF2196F3), onWaterAdd, "Goal: 2L")
        QuickStatCard("Prayers", "$prayerCount/5", Icons.Filled.Mosque, Color(0xFF4CAF50), {}, "Today")
        QuickStatCard("Charity", "$${charityAmount.toInt()}", Icons.Filled.VolunteerActivism, Color(0xFFF44336), onCharityAdd, "Given")
        QuickStatCard("Streak", "$fastingStreak", Icons.Filled.Star, Color(0xFFFF9800), {}, "Days")
    }
}

@Composable
fun RowScope.QuickStatCard(title: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector, color: Color, onClick: () -> Unit, subtitle: String = "") {
    Card(modifier = Modifier.weight(1f).height(100.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = color.copy(0.1f)), onClick = onClick) {
        Column(Modifier.fillMaxSize().padding(8.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, title, tint = color, modifier = Modifier.size(16.dp))
                Spacer(Modifier.width(2.dp))
                Text(value, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = color)
            }
            Text(title, style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
            if (subtitle.isNotEmpty()) Text(subtitle, style = MaterialTheme.typography.labelSmall, fontSize = 8.sp)
        }
    }
}

@Composable
fun RamadanProgressCard(daysFasted: Int, remainingDays: Int, totalDays: Int, modifier: Modifier = Modifier) {
    val progress = daysFasted.toFloat() / totalDays
    Card(modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.fillMaxWidth().padding(20.dp)) {
            Text("Ramadan Progress", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text("${(progress * 100).toInt()}% complete • $remainingDays days left", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth().height(16.dp).clip(RoundedCornerShape(8.dp)))
            Spacer(Modifier.height(16.dp))
            RamadanDaysGrid(daysFasted, totalDays)
        }
    }
}

@Composable
fun RamadanDaysGrid(daysFasted: Int, totalDays: Int) {
    Column {
        val rows = (totalDays + 9) / 10
        repeat(rows) { row ->
            Row(Modifier.padding(vertical = 4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                for (day in (row * 10 + 1)..minOf((row + 1) * 10, totalDays)) {
                    Box(Modifier.weight(1f).aspectRatio(1f).clip(CircleShape).background(if (day <= daysFasted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(0.1f)), contentAlignment = Alignment.Center) {
                        Text("$day", fontSize = 10.sp, color = if (day <= daysFasted) Color.White else MaterialTheme.colorScheme.onSurface.copy(0.5f))
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressDashboardCard(daysFasted: Int, quranProgress: Float, taraweehProgress: Float, charityProgress: Float, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.fillMaxWidth().padding(20.dp)) {
            Text("Progress Dashboard", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            ProgressItem("Fasting", daysFasted, 30, daysFasted / 30f, MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(12.dp))
            ProgressItem("Quran", (quranProgress * 30).toInt(), 30, quranProgress, Color(0xFF4CAF50))
            Spacer(Modifier.height(12.dp))
            ProgressItem("Taraweeh", (taraweehProgress * 20).toInt(), 20, taraweehProgress, MaterialTheme.colorScheme.secondary)
        }
    }
}

@Composable
fun ProgressItem(label: String, current: Int, total: Int, progress: Float, color: Color) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, style = MaterialTheme.typography.bodyMedium)
            Text("$current/$total (${(progress * 100).toInt()}%)", color = color, fontWeight = FontWeight.Bold)
        }
        LinearProgressIndicator(progress = progress, modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp)), color = color)
    }
}

@Composable
fun PrayerTimesFullCard(prayerTimes: Map<String, String>, currentPrayer: String?, nextPrayer: Pair<String, String>?, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.fillMaxWidth().padding(20.dp)) {
            Text("Prayer Times", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            val prayers = listOf("Fajr", "Sunrise", "Dhuhr", "Asr", "Maghrib", "Isha")
            prayers.forEach { prayer ->
                val isCurrent = currentPrayer == prayer
                val isPast = prayers.indexOf(prayer) < (currentPrayer?.let { prayers.indexOf(it) } ?: -1)
                PrayerTimeRow(prayer, prayerTimes[prayer] ?: "--:--", isCurrent, isPast)
                if (prayer != prayers.last()) Spacer(Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun PrayerTimeRow(name: String, time: String, isCurrent: Boolean, isPast: Boolean) {
    Card(Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = if (isCurrent) MaterialTheme.colorScheme.primaryContainer else Color.Transparent)) {
        Row(Modifier.padding(16.dp, 12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Text(name, fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal, color = if (isPast) Color.Gray else Color.Unspecified)
            Text(time, fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal, color = if (isCurrent) MaterialTheme.colorScheme.primary else if (isPast) Color.Gray else Color.Unspecified)
        }
    }
}

@Composable
fun QuranTrackerCard(progress: Float, juzCompleted: Int, onProgressUpdate: (Int) -> Unit, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Quran Recitation", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Box(Modifier.size(100.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(progress = progress, modifier = Modifier.size(100.dp), strokeWidth = 8.dp, color = Color(0xFF4CAF50))
                Text("${(progress * 100).toInt()}%", fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { onProgressUpdate(1) }, Modifier.weight(1f)) { Text("+1 P") }
                Button(onClick = { onProgressUpdate(20) }, Modifier.weight(1f)) { Text("+1 J") }
            }
        }
    }
}

@Composable
fun TaraweehTrackerCard(taraweehCount: Int, onIncrement: () -> Unit, onReset: () -> Unit, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Taraweeh Tracker", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            CircularProgressIndicator(progress = taraweehCount / 20f, modifier = Modifier.size(120.dp), strokeWidth = 8.dp, color = MaterialTheme.colorScheme.secondary)
            Text("$taraweehCount / 20", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                Button(onClick = onIncrement, enabled = taraweehCount < 20) { Text("Mark 2") }
                Button(onClick = onReset, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.errorContainer)) { Text("Reset") }
            }
        }
    }
}

@Composable
fun AchievementsSection(achievements: List<Achievement>, modifier: Modifier = Modifier) {
    Card(modifier, colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface), shape = RoundedCornerShape(20.dp)) {
        Column(Modifier.padding(20.dp)) {
            Text("Achievements", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(achievements) { AchievementCard(it) }
            }
        }
    }
}

@Composable
fun AchievementCard(achievement: Achievement) {
    Card(Modifier.size(150.dp, 120.dp), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = if (achievement.unlocked) MaterialTheme.colorScheme.tertiaryContainer else Color.LightGray.copy(0.3f))) {
        Column(Modifier.fillMaxSize().padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
            Icon(achievement.icon, achievement.title, tint = if (achievement.unlocked) MaterialTheme.colorScheme.tertiary else Color.Gray)
            Text(achievement.title, fontWeight = FontWeight.Medium, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun HealthTipsSection(modifier: Modifier = Modifier) {
    val tips = listOf("Drink Water", "Healthy Meals", "Break Fast Slowly", "Avoid Fried Food", "Light Exercise")
    Column(modifier) {
        Text("Health Tips", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(tips) { HealthTipItem(it) }
        }
    }
}

@Composable
fun HealthTipItem(tip: String) {
    Text(
        text = tip,
        modifier = Modifier
            .padding(8.dp)
            .background(MaterialTheme.colorScheme.tertiaryContainer, RoundedCornerShape(8.dp))
            .padding(8.dp)
    )
}
