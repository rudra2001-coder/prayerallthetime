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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.theme.*

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
                            tint = IslamicGold,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Ramadan Mubarak",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MidnightBlue
                ),
                actions = {
                    IconButton(
                        onClick = { /* Open calendar view */ },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                            .padding(4.dp)
                    ) {
                        Icon(Icons.Filled.CalendarMonth, contentDescription = "Calendar", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = { /* Share progress */ },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                            .padding(4.dp)
                    ) {
                        Icon(Icons.Filled.Share, contentDescription = "Share", tint = Color.White)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.logFast() },
                containerColor = IslamicGold,
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.CheckCircle, contentDescription = "Log Today's Fast")
            }
        },
        containerColor = MidnightBlue
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MidnightBlue)
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
                        .height(200.dp)
                        .background(
                            brush = AppGradients.ramadanGradient,
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
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = if (isRamadan) {
                                "Ramadan Day $ramadanDay • ${(viewModel.getRamadanProgress() * 100).toInt()}% Complete"
                            } else {
                                "Ramadan starts in $nextRamadanCountdown"
                            },
                            style = MaterialTheme.typography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = if (isFasting) "Currently Fasting" else "Not Fasting",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    color = Color.White
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "$remainingDays days remaining • $daysFasted fasted",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color.White.copy(alpha = 0.8f)
                            )
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
                        currentPrayer = viewModel.getCurrentPrayer() ?: "",
                        nextPrayer = viewModel.getNextPrayer()?.first ?: "",
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
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = ShadowDark
            ),
        colors = CardDefaults.cardColors(containerColor = MidnightBlueCard),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                    Text(
                        "Fasting Timer",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimaryDark
                        )
                    )
                    if (fastingStreak > 0) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Filled.Star,
                                null,
                                tint = StreakColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(Modifier.width(4.dp))
                            Text(
                                "$fastingStreak day streak",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondaryDark
                                )
                            )
                        }
                    }
                }
                Button(
                    onClick = onLogFast,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = IslamicGold.copy(alpha = 0.2f),
                        contentColor = IslamicGold
                    )
                ) {
                    Text(if (isFasting) "Log Fast" else "Mark Complete")
                }
            }
            
            Spacer(Modifier.height(20.dp))
            
            Box(
                Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                val primaryColor = RamadanPurple
                Canvas(Modifier.size(200.dp)) {
                    drawCircle(primaryColor.copy(0.1f), radius = size.minDimension / 2 - 10)
                }
                val animatedProgress by animateFloatAsState(progress, label = "progress")
                Canvas(Modifier.size(200.dp)) {
                    drawArc(
                        primaryColor,
                        -90f,
                        360f * animatedProgress,
                        false,
                        style = Stroke(12.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        if (isFasting) "Iftar in" else "Suhoor in",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondaryDark
                        )
                    )
                    Text(
                        countdown,
                        style = ExtendedTypography.countdownSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextPrimaryDark
                        )
                    )
                }
            }
            
            Spacer(Modifier.height(20.dp))
            
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                TimeItem(
                    Icons.Filled.NightShelter,
                    "Suhoor",
                    suhoorTime,
                    "Ends",
                    InfoColor
                )
                TimeItem(
                    Icons.Filled.Brightness7,
                    "Iftar",
                    iftarTime,
                    "Starts",
                    IslamicGold
                )
            }
        }
    }
}

@Composable
fun TimeItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    time: String,
    subtitle: String,
    iconColor: Color
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            Modifier
                .size(60.dp)
                .clip(CircleShape)
                .background(iconColor.copy(0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, title, tint = iconColor, modifier = Modifier.size(28.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text(
            title,
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextSecondaryDark
            )
        )
        Text(
            time,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = TextPrimaryDark
            )
        )
        Text(
            subtitle,
            style = MaterialTheme.typography.labelSmall.copy(
                color = TextTertiaryDark
            )
        )
    }
}

@Composable
fun QuickStatsRow(
    waterIntake: Int,
    prayerCount: Int,
    charityAmount: Double,
    fastingStreak: Int,
    onWaterAdd: () -> Unit,
    onCharityAdd: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        QuickStatCard(
            "Water",
            "${waterIntake}L",
            Icons.Filled.LocalDrink,
            InfoColor,
            onWaterAdd,
            "Goal: 2L"
        )
        QuickStatCard(
            "Prayers",
            "$prayerCount/5",
            Icons.Filled.Mosque,
            SuccessColor,
            {},
            "Today"
        )
        QuickStatCard(
            "Charity",
            "$${charityAmount.toInt()}",
            Icons.Filled.VolunteerActivism,
            IslamicGold,
            onCharityAdd,
            "Given"
        )
        QuickStatCard(
            "Streak",
            "$fastingStreak",
            Icons.Filled.LocalFireDepartment,
            StreakColor,
            {},
            "Days"
        )
    }
}

@Composable
fun RowScope.QuickStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    onClick: () -> Unit,
    subtitle: String = ""
) {
    Card(
        modifier = Modifier
            .weight(1f)
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = color.copy(0.1f)),
        onClick = onClick,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, title, tint = color, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(4.dp))
                Text(
                    value,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = color
                    )
                )
            }
            Text(
                title,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextSecondaryDark,
                    fontSize = 10.sp
                )
            )
            if (subtitle.isNotEmpty()) {
                Text(
                    subtitle,
                    style = MaterialTheme.typography.labelSmall.copy(
                        color = TextTertiaryDark,
                        fontSize = 8.sp
                    )
                )
            }
        }
    }
}

@Composable
fun RamadanProgressCard(
    daysFasted: Int,
    remainingDays: Int,
    totalDays: Int,
    modifier: Modifier = Modifier
) {
    val progress = daysFasted.toFloat() / totalDays
    Card(
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = ShadowDark
            ),
        colors = CardDefaults.cardColors(containerColor = MidnightBlueCard),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "Ramadan Progress",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimaryDark
                )
            )
            Text(
                "${(progress * 100).toInt()}% complete • $remainingDays days left",
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = TextSecondaryDark
                )
            )
            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .clip(RoundedCornerShape(8.dp)),
                color = RamadanPurple,
                trackColor = MidnightBlueLight
            )
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
            Row(
                Modifier.padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                for (day in (row * 10 + 1)..minOf((row + 1) * 10, totalDays)) {
                    Box(
                        Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .clip(CircleShape)
                            .background(
                                if (day <= daysFasted) RamadanPurple
                                else MidnightBlueLight
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "$day",
                            fontSize = 10.sp,
                            color = if (day <= daysFasted) Color.White else TextTertiaryDark
                        )
                    }
                }
            }
        }
    }
}

// Remaining composables would follow the same pattern with updated theme
@Composable
fun ProgressDashboardCard(
    daysFasted: Int,
    quranProgress: Float,
    taraweehProgress: Float,
    charityProgress: Float,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = ShadowDark
            ),
        colors = CardDefaults.cardColors(containerColor = MidnightBlueCard),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "Your Progress Dashboard",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimaryDark
                )
            )
            Spacer(Modifier.height(16.dp))

            ProgressItem("Fasting", daysFasted / 30f, "$daysFasted/30 days", SuccessColor)
            Spacer(Modifier.height(12.dp))
            ProgressItem("Quran Reading", quranProgress, "${(quranProgress * 100).toInt()}%", QuranColor)
            Spacer(Modifier.height(12.dp))
            ProgressItem("Taraweeh", taraweehProgress, "${(taraweehProgress * 20).toInt()}/20 nights", TaraweehColor)
            Spacer(Modifier.height(12.dp))
            ProgressItem("Charity", charityProgress, "$${(charityProgress * 100).toInt()}", IslamicGold)
        }
    }
}

@Composable
fun ProgressItem(label: String, progress: Float, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            label,
            modifier = Modifier.width(100.dp),
            style = MaterialTheme.typography.bodyMedium.copy(
                color = TextSecondaryDark
            )
        )
        LinearProgressIndicator(
            progress = { progress.coerceIn(0f, 1f) },
            modifier = Modifier
                .weight(1f)
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = MidnightBlueLight
        )
        Spacer(Modifier.width(12.dp))
        Text(
            value,
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.SemiBold,
                color = color
            )
        )
    }
}

@Composable
fun PrayerTimesFullCard(
    prayerTimes: Map<String, String>,
    currentPrayer: String,
    nextPrayer: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = ShadowDark
            ),
        colors = CardDefaults.cardColors(containerColor = MidnightBlueCard),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "Prayer Times",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimaryDark
                )
            )
            Spacer(Modifier.height(16.dp))

            prayerTimes.forEach { (prayer, time) ->
                val isNext = prayer == nextPrayer
                val isCurrent = prayer == currentPrayer
                val prayerColor = getPrayerColor(prayer)

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(
                                    when {
                                        isCurrent -> SuccessColor
                                        isNext -> prayerColor
                                        else -> TextTertiaryDark.copy(alpha = 0.3f)
                                    }
                                )
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            prayer,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = if (isCurrent || isNext) TextPrimaryDark else TextSecondaryDark,
                                fontWeight = if (isCurrent || isNext) FontWeight.SemiBold else FontWeight.Normal
                            )
                        )
                    }
                    Text(
                        time,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = prayerColor,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun TaraweehTrackerCard(
    taraweehCount: Int,
    onIncrement: () -> Unit,
    onReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = ShadowDark
            ),
        colors = CardDefaults.cardColors(containerColor = MidnightBlueCard),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "Taraweeh Prayer",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimaryDark
                    )
                )
                Text(
                    "$taraweehCount of 20 nights",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextSecondaryDark
                    )
                )
            }
            Row {
                IconButton(
                    onClick = onReset,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(ErrorColor.copy(alpha = 0.15f))
                ) {
                    Icon(
                        Icons.Default.Refresh,
                        contentDescription = "Reset",
                        tint = ErrorColor
                    )
                }
                Spacer(Modifier.width(8.dp))
                IconButton(
                    onClick = onIncrement,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(TaraweehColor.copy(alpha = 0.15f))
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Add",
                        tint = TaraweehColor
                    )
                }
            }
        }
    }
}

@Composable
fun QuranTrackerCard(
    progress: Float,
    juzCompleted: Int,
    onProgressUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = ShadowDark
            ),
        colors = CardDefaults.cardColors(containerColor = MidnightBlueCard),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        "Quran Reading",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimaryDark
                        )
                    )
                    Text(
                        "$juzCompleted Juz completed",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondaryDark
                        )
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = QuranColor
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    IconButton(
                        onClick = { onProgressUpdate(1) },
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(QuranColor.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Page",
                            tint = QuranColor,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            Spacer(Modifier.height(16.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp)
                    .clip(RoundedCornerShape(6.dp)),
                color = QuranColor,
                trackColor = MidnightBlueLight
            )
        }
    }
}

@Composable
fun AchievementsSection(
    achievements: List<Achievement>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = ShadowDark
            ),
        colors = CardDefaults.cardColors(containerColor = MidnightBlueCard),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "Achievements",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimaryDark
                )
            )
            Spacer(Modifier.height(12.dp))
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(achievements) { achievement ->
                    AchievementBadge(achievement)
                }
            }
        }
    }
}

@Composable
fun AchievementBadge(achievement: Achievement) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = IslamicGold.copy(alpha = 0.15f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                achievement.icon,
                contentDescription = null,
                tint = IslamicGold,
                modifier = Modifier.size(18.dp)
            )
            Spacer(Modifier.width(6.dp))
            Text(
                achievement.title,
                style = MaterialTheme.typography.labelMedium.copy(
                    color = IslamicGold,
                    fontWeight = FontWeight.Medium
                )
            )
        }
    }
}

@Composable
fun HealthTipsSection(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 6.dp,
                shape = RoundedCornerShape(20.dp),
                spotColor = ShadowDark
            ),
        colors = CardDefaults.cardColors(containerColor = MidnightBlueCard),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                "Health Tips",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimaryDark
                )
            )
            Spacer(Modifier.height(12.dp))

            val tips = listOf(
                "Stay hydrated between Iftar and Suhoor",
                "Eat balanced meals with protein and fiber",
                "Avoid excessive caffeine and sugary drinks",
                "Get adequate sleep during Ramadan",
                "Light exercise after Iftar is beneficial"
            )

            tips.forEachIndexed { index, tip ->
                Row(
                    modifier = Modifier.padding(vertical = 6.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        "${index + 1}.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = SuccessColor,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        tip,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextSecondaryDark
                        )
                    )
                }
            }
        }
    }
}
