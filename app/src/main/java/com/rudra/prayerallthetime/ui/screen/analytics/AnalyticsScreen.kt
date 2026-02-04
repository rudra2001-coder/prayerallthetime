package com.rudra.prayerallthetime.ui.screen.analytics

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.rudra.prayerallthetime.data.local.HabitEntity
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
    val prayerConsistency by analyticsViewModel.prayerConsistency.collectAsState()
    val weeklyProgress by analyticsViewModel.weeklyProgress.collectAsState()
    val habitStats by analyticsViewModel.habitStats.collectAsState()
    val totalCompleted by analyticsViewModel.totalCompletedPrayers.collectAsState()
    
    val mainStreak = StreakData(
        id = 1,
        name = "Prayer Streak",
        description = "Consecutive days of all 5 prayers",
        days = currentStreak,
        color = Color(0xFFFF6B6B),
        icon = Icons.Default.Whatshot,
        nextMilestone = if (currentStreak < 10) 10 else if (currentStreak < 30) 30 else currentStreak + 10
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            AnalyticsHeader(totalCompleted)
        }

        item {
            StreakCard(
                streakData = mainStreak,
                onStreakClick = { /* Navigate to detailed streaks if needed */ }
            )
        }

        item {
            WeeklyActivitySection(weeklyProgress)
        }

        item {
            ConsistencyBreakdownSection(prayerConsistency)
        }

        item {
            HabitsPerformanceSection(habitStats)
        }

        item {
            ConsistencyInsights(completionRate = prayerViewModel.completionRate.collectAsState().value)
        }
    }
}

@Composable
fun AnalyticsHeader(totalCompleted: Int) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = "Spiritual Analytics",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF0F1B4C)
        )
        Text(
            text = "You have performed $totalCompleted prayers so far. MashAllah!",
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
            .shadow(12.dp, RoundedCornerShape(32.dp), spotColor = Color(0xFFFF6B6B).copy(alpha = 0.5f)),
        shape = RoundedCornerShape(32.dp),
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
                        text = "CURRENT STREAK",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${streakData.days} Days",
                        color = Color.White,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { (streakData.days.toFloat() / streakData.nextMilestone.toFloat()).coerceIn(0f, 1f) },
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
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    streakData.icon?.let {
                        Icon(it, null, tint = Color.White, modifier = Modifier.size(36.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun WeeklyActivitySection(weeklyData: List<DayData>) {
    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "Weekly Activity",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF2C3E50)
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                weeklyData.forEach { day ->
                    BarItem(day)
                }
            }
        }
    }
}

@Composable
fun BarItem(day: DayData) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.fillMaxHeight()
    ) {
        val barHeight = (day.completionRate * 120).dp
        
        Box(
            modifier = Modifier
                .width(28.dp)
                .height(barHeight.coerceAtLeast(4.dp))
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (day.isToday) Brush.verticalGradient(listOf(Color(0xFF4ECDC4), Color(0xFF45B7D1)))
                    else Brush.verticalGradient(listOf(Color(0xFFE0E0E0), Color(0xFFBDBDBD)))
                )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = day.dayAbbr,
            fontSize = 12.sp,
            fontWeight = if (day.isToday) FontWeight.Bold else FontWeight.Medium,
            color = if (day.isToday) Color(0xFF4ECDC4) else Color.Gray
        )
    }
}

@Composable
fun ConsistencyBreakdownSection(stats: Map<String, Float>) {
    Column(modifier = Modifier.padding(horizontal = 24.dp)) {
        Text(
            text = "Prayer Consistency",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF2C3E50)
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(1.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                stats.forEach { (prayer, rate) ->
                    ConsistencyRow(prayer, rate)
                }
            }
        }
    }
}

@Composable
fun ConsistencyRow(name: String, rate: Float) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(name, fontWeight = FontWeight.Bold, color = Color(0xFF2C3E50))
            Text("${(rate * 100).toInt()}%", fontWeight = FontWeight.Black, color = Color(0xFF45B7D1))
        }
        Spacer(modifier = Modifier.height(8.dp))
        LinearProgressIndicator(
            progress = { rate },
            modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
            color = Color(0xFF4ECDC4),
            trackColor = Color(0xFFF0F0F0)
        )
    }
}

@Composable
fun HabitsPerformanceSection(habits: List<HabitEntity>) {
    if (habits.isEmpty()) return
    
    Column(modifier = Modifier.padding(24.dp)) {
        Text(
            text = "Habit Streaks",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFF2C3E50)
        )
        Spacer(modifier = Modifier.height(16.dp))
        
        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(habits) { habit ->
                HabitStreakItem(habit)
            }
        }
    }
}

@Composable
fun HabitStreakItem(habit: HabitEntity) {
    Card(
        modifier = Modifier.size(width = 140.dp, height = 120.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(habit.iconEmoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = habit.title, fontSize = 12.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 1)
            Text(text = "${habit.streak} Day Streak", fontSize = 11.sp, color = Color(0xFFFF5722), fontWeight = FontWeight.Black)
        }
    }
}

@Composable
fun ConsistencyInsights(completionRate: Float) {
    Card(
        modifier = Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1B4C))
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(48.dp).clip(CircleShape).background(Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Lightbulb, null, tint = Color(0xFFFFD93D))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Coach Insight", color = Color.White, fontWeight = FontWeight.Bold)
                Text(
                    text = if (completionRate > 0.8f) 
                        "Excellent work! Your consistency is helping you build a strong spiritual routine."
                    else 
                        "Try to focus on small daily wins. Every prayer counts towards your goal.",
                    color = Color.White.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
            }
        }
    }
}
