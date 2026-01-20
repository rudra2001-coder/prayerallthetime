package com.rudra.prayerallthetime.ui.screen.analytics

import androidx.compose.animation.core.*
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
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rudra.prayerallthetime.ui.navigation.Screen
import com.rudra.prayerallthetime.ui.screen.prayer.Badge
import com.rudra.prayerallthetime.ui.screen.prayer.DayData
import com.rudra.prayerallthetime.ui.screen.prayer.PrayerViewModel
import com.rudra.prayerallthetime.ui.screen.prayer.StreakData

@Composable
fun AnalyticsScreen(
    navController: NavController,
    prayerViewModel: PrayerViewModel,
    analyticsViewModel: AnalyticsViewModel = hiltViewModel()
) {
    val currentStreak by prayerViewModel.currentStreak.collectAsState()
    val prayers by prayerViewModel.prayers.collectAsState()
    val prayerStats by prayerViewModel.prayerStats.collectAsState()
    val weeklyDayData by prayerViewModel.weeklyDayData.collectAsState()
    val earnedBadges by prayerViewModel.earnedBadges.collectAsState()
    
    val mainStreak = getMainStreak().copy(days = currentStreak)
    val completedPrayers = prayers.filter { it.isPrayed }.map { it.name }.toSet()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFBFBFB)),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            AnalyticsHeader()
        }

        item {
            StreakCard(
                streakData = mainStreak,
                onStreakClick = { navController.navigate(Screen.Streaks.route) }
            )
        }

        item {
            WeeklyProgressSection(weeklyDayData)
        }

        item {
            DailyCheckInSection(
                completedPrayers = completedPrayers,
                onTogglePrayer = { prayerName ->
                    prayers.find { it.name == prayerName }?.let {
                        prayerViewModel.togglePrayerState(it)
                    }
                }
            )
        }

        item {
            PrayerBreakdownSection(prayerStats)
        }

        item {
            BadgesSection(
                badges = earnedBadges,
                onViewAll = { navController.navigate(Screen.Achievements.route) }
            )
        }

        item {
            ConsistencyInsights()
        }
    }
}

@Composable
fun AnalyticsHeader() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = "Spiritual Analytics",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2C3E50)
            )
        )
        Text(
            text = "Track your journey of ibadah",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun StreakCard(streakData: StreakData, onStreakClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth()
            .clickable { onStreakClick() }
            .shadow(12.dp, RoundedCornerShape(24.dp), spotColor = Color(0xFFFF6B6B).copy(alpha = 0.5f)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(
            modifier = Modifier
                .background(
                    Brush.linearGradient(
                        colors = listOf(Color(0xFFFF6B6B), Color(0xFFFF9E7D))
                    )
                )
                .padding(24.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Current Streak",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${streakData.days} Days",
                        color = Color.White,
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { (streakData.days.toFloat() / streakData.nextMilestone).coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape),
                        color = Color.White,
                        trackColor = Color.White.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Next Milestone: ${streakData.nextMilestone} days",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 12.sp
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Animated Flame Icon
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Whatshot,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WeeklyProgressSection(weeklyData: List<DayData>) {
    Column(modifier = Modifier.padding(24.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Weekly Activity",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF2C3E50)
            )
            Text(
                text = "Last 7 Days",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color.White, RoundedCornerShape(20.dp))
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            val displayData = if (weeklyData.isEmpty()) {
                listOf(
                    DayData("Mon", "M", 3, 0.6f),
                    DayData("Tue", "T", 5, 1.0f),
                    DayData("Wed", "W", 4, 0.8f),
                    DayData("Thu", "T", 5, 1.0f),
                    DayData("Fri", "F", 5, 1.0f),
                    DayData("Sat", "S", 2, 0.4f),
                    DayData("Sun", "S", 0, 0.0f, true)
                )
            } else weeklyData

            displayData.forEach { day ->
                BarChartItem(day)
            }
        }
    }
}

@Composable
fun BarChartItem(day: DayData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxHeight()
    ) {
        val barHeight = (day.completionRate * 100).dp
        
        Box(
            modifier = Modifier
                .width(32.dp)
                .height(barHeight)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(
                    if (day.isToday) Brush.verticalGradient(listOf(Color(0xFF4ECDC4), Color(0xFF45B7D1)))
                    else Brush.verticalGradient(listOf(Color(0xFF2C3E50).copy(alpha = 0.1f), Color(0xFF2C3E50).copy(alpha = 0.2f)))
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = day.dayAbbr,
            fontSize = 12.sp,
            fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Normal,
            color = if (day.isToday) Color(0xFF4ECDC4) else Color.Gray
        )
    }
}

@Composable
fun DailyCheckInSection(completedPrayers: Set<String>, onTogglePrayer: (String) -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Today's Prayers",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFF2C3E50)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha").forEach { prayer ->
                    PrayerCircleItem(
                        name = prayer,
                        isCompleted = completedPrayers.contains(prayer),
                        onClick = { onTogglePrayer(prayer) }
                    )
                }
            }
        }
    }
}

@Composable
fun PrayerCircleItem(name: String, isCompleted: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(if (isCompleted) Color(0xFF4ECDC4) else Color(0xFFF0F0F0))
                .clickable { onClick() },
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(Icons.Default.Check, null, tint = Color.White, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = name.take(1),
                    fontWeight = FontWeight.Bold,
                    color = Color.LightGray
                )
            }
        }
        Text(
            text = name,
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 6.dp),
            color = if (isCompleted) Color(0xFF2C3E50) else Color.Gray
        )
    }
}

@Composable
fun PrayerBreakdownSection(stats: Map<String, Float>) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "Prayer Consistency",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF2C3E50)
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                stats.forEach { (prayer, rate) ->
                    PrayerStatRow(prayer, rate)
                    if (prayer != stats.keys.last()) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF0F0F0))
                    }
                }
            }
        }
    }
}

@Composable
fun PrayerStatRow(name: String, rate: Float) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = name,
            modifier = Modifier.width(70.dp),
            fontWeight = FontWeight.Medium,
            color = Color(0xFF2C3E50)
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .height(10.dp)
                .clip(CircleShape)
                .background(Color(0xFFF0F0F0))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(rate)
                    .fillMaxHeight()
                    .clip(CircleShape)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color(0xFF4ECDC4), Color(0xFF45B7D1))
                        )
                    )
            )
        }
        Text(
            text = "${(rate * 100).toInt()}%",
            modifier = Modifier.width(45.dp),
            textAlign = TextAlign.End,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            color = Color(0xFF4ECDC4)
        )
    }
}

@Composable
fun BadgesSection(badges: List<Badge>, onViewAll: () -> Unit) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Achievements",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color(0xFF2C3E50)
            )
            TextButton(onClick = onViewAll) {
                Text("View All", color = Color(0xFF45B7D1))
            }
        }
        
        LazyRow(
            contentPadding = PaddingValues(horizontal = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val displayBadges = if (badges.isEmpty()) {
                listOf(
                    Badge(1, "Early Bird", "Pray Fajr 7 days", Color(0xFFFFD93D), iconImage = Icons.Default.WbSunny),
                    Badge(2, "Consistent", "30 day streak", Color(0xFFFF6B6B), iconImage = Icons.Default.AutoAwesome),
                    Badge(3, "Community", "Joined a group", Color(0xFF4ECDC4), iconImage = Icons.Default.Groups)
                )
            } else badges

            items(displayBadges) { badge ->
                BadgeItem(badge)
            }
        }
    }
}

@Composable
fun BadgeItem(badge: Badge) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.width(100.dp)
    ) {
        Box(
            modifier = Modifier
                .size(80.dp)
                .clip(CircleShape)
                .background(badge.color.copy(alpha = 0.1f))
                .drawBehind {
                    drawCircle(
                        color = badge.color.copy(alpha = 0.2f),
                        radius = size.minDimension / 2,
                        style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.dp.toPx())
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            badge.iconImage?.let {
                Icon(it, null, tint = badge.color, modifier = Modifier.size(32.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = badge.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ConsistencyInsights() {
    Card(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2C3E50))
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.Lightbulb,
                null,
                tint = Color(0xFFFFD93D),
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = "Smart Insight",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "You're most consistent with Maghrib prayer. Try applying the same routine to your Fajr prayer for better results!",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp,
                    lineHeight = 18.sp
                )
            }
        }
    }
}

@Composable
fun getMainStreak() = StreakData(1, "Prayer Streak", "Days in a row", 7, Color(0xFFFF6B6B), nextMilestone = 10)
fun getOtherStreaks() = listOf<StreakData>()

@Preview(showBackground = true)
@Composable
fun AnalyticsPreview() {
    // Preview with mock data
}
