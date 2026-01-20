package com.rudra.prayerallthetime.ui.screen.dashboard.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun PremiumStreaksCard(
    currentStreakData: StreakData,
    completedPrayers: Set<String> = emptySet(),
    onTogglePrayer: (String) -> Unit = {},
    onAddDay: () -> Unit = {},
    otherStreaks: List<StreakData> = getOtherStreaks(),
    longestStreak: Int = 45,
    totalPerfectDays: Int = 78,
    onViewAll: () -> Unit = {},
    onStreakClick: (StreakData) -> Unit = {}
) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = BorderStroke(
            1.dp,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
    ) {
        Column(
            modifier = Modifier
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8F5F0),
                            Color(0xFFF0EDE8)
                        )
                    )
                )
                .padding(24.dp)
        ) {
            // Header with fire animation effect
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFFF6B6B).copy(alpha = 0.2f))
                                .padding(6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fireplace,
                                contentDescription = "Streak",
                                tint = Color(0xFFFF6B6B),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "🔥 Prayer Streaks",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C3E50)
                        )
                    }
                    Text(
                        text = "Keep the fire of ibadah burning",
                        fontSize = 12.sp,
                        color = Color(0xFF2C3E50).copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                // View All Button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF8B4513).copy(alpha = 0.1f))
                        .clickable { onViewAll() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "View All",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF8B4513)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Main Current Streak
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFFF6B6B).copy(alpha = 0.1f),
                                Color(0xFFFFD93D).copy(alpha = 0.1f)
                            )
                        )
                    )
                    .clickable { onStreakClick(currentStreakData) }
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Flame Animation Area
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .drawBehind {
                                // Draw multiple flame layers for animation effect
                                for (i in 0..2) {
                                    val sizeMultiplier = 1f - (i * 0.2f)
                                    drawCircle(
                                        color = when (i) {
                                            0 -> Color(0xFFFFD93D)
                                            1 -> Color(0xFFFFB347)
                                            else -> Color(0xFFFF6B6B)
                                        }.copy(alpha = 0.3f),
                                        radius = size.minDimension * 0.4f * sizeMultiplier,
                                        center = Offset(size.width / 2, size.height / 2)
                                    )
                                }
                            }
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color(0xFFFF6B6B).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${currentStreakData.days}",
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFFF6B6B)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    // Streak Details
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentStreakData.name,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2C3E50)
                        )
                        Text(
                            text = currentStreakData.description,
                            fontSize = 13.sp,
                            color = Color(0xFF2C3E50).copy(alpha = 0.8f),
                            lineHeight = 18.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Next Milestone
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Flag,
                                contentDescription = "Milestone",
                                tint = Color(0xFFD4AF37),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Next: ${currentStreakData.nextMilestone} days",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF8B4513)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        LinearProgressIndicator(
                            progress = (currentStreakData.days.toFloat() / currentStreakData.nextMilestone.toFloat()).coerceIn(0f, 1f),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(CircleShape),
                            color = Color(0xFFFF6B6B),
                            trackColor = Color(0xFF2C3E50).copy(alpha = 0.1f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Daily Prayer Check-in (The 5 boxes)
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Daily Prayer Check-in",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2C3E50),
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    listOf("Fajr", "Dhuhr", "Asr", "Maghrib", "Isha").forEach { prayer ->
                        PrayerCheckItem(
                            name = prayer,
                            isCompleted = completedPrayers.contains(prayer),
                            onClick = { onTogglePrayer(prayer) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Button to Add Streak
                Button(
                    onClick = onAddDay,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (completedPrayers.size == 5) Color(0xFFFF6B6B) else Color(0xFF2C3E50).copy(alpha = 0.1f),
                        contentColor = if (completedPrayers.size == 5) Color.White else Color(0xFF2C3E50).copy(alpha = 0.4f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = if (completedPrayers.size == 5) 4.dp else 0.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Bolt,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (completedPrayers.size == 5) "Add Today to Streak" else "Complete All 5 Prayers",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF2C3E50).copy(alpha = 0.05f))
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StreakStat(
                    title = "Longest Streak",
                    value = "$longestStreak days",
                    icon = Icons.Default.TrendingUp,
                    color = Color(0xFF4ECDC4)
                )
                StreakStat(
                    title = "Perfect Days",
                    value = totalPerfectDays.toString(),
                    icon = Icons.Default.Star,
                    color = Color(0xFFD4AF37)
                )
                StreakStat(
                    title = "Current Level",
                    value = if (currentStreakData.days >= 30) "Platinum" else if (currentStreakData.days >= 15) "Gold" else "Silver",
                    icon = Icons.Default.AutoAwesome,
                    color = Color(0xFFFFD93D)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Other Streaks Section
            Text(
                text = "Other Streaks",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2C3E50),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                otherStreaks.forEach { streak ->
                    StreakRow(
                        streak = streak,
                        onClick = { onStreakClick(streak) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Motivational Section
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF8B4513).copy(alpha = 0.1f))
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFD4AF37).copy(alpha = 0.2f))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Tip",
                            tint = Color(0xFFD4AF37),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Consistency Tip",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF8B4513)
                        )
                        Text(
                            text = "Pray Fajr on time to maintain your streak. The early morning prayer sets the tone for the day.",
                            fontSize = 12.sp,
                            color = Color(0xFF2C3E50).copy(alpha = 0.8f),
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PrayerCheckItem(
    name: String,
    isCompleted: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(
                    if (isCompleted) {
                        Brush.linearGradient(
                            colors = listOf(Color(0xFFFF6B6B), Color(0xFFFFD93D))
                        )
                    } else {
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF2C3E50).copy(alpha = 0.05f),
                                Color(0xFF2C3E50).copy(alpha = 0.08f)
                            )
                        )
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(26.dp)
                )
            } else {
                Text(
                    text = name.take(1),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFF2C3E50).copy(alpha = 0.3f)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (isCompleted) Color(0xFFFF6B6B) else Color(0xFF2C3E50).copy(alpha = 0.5f)
        )
    }
}

@Composable
fun StreakRow(
    streak: StreakData,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.3f))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(streak.color.copy(alpha = 0.2f))
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            streak.icon?.let {
                Icon(
                    imageVector = it,
                    contentDescription = streak.name,
                    tint = streak.color,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = streak.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF2C3E50)
                )
                Text(
                    text = "${streak.days} days",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = streak.color
                )
            }
            Text(
                text = streak.description,
                fontSize = 12.sp,
                color = Color(0xFF2C3E50).copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
fun StreakStat(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color.copy(alpha = 0.2f))
                .padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF2C3E50)
        )
        Text(
            text = title,
            fontSize = 11.sp,
            color = Color(0xFF2C3E50).copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

data class StreakData(
    val id: Int,
    val name: String,
    val description: String,
    val days: Int,
    val color: Color,
    val icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    val nextMilestone: Int = 30
)

@Composable
fun getMainStreak(): StreakData {
    return StreakData(
        id = 1,
        name = "Daily Prayer Streak",
        description = "Consecutive days of completing all 5 prayers",
        days = 0,
        color = Color(0xFFFF6B6B),
        icon = Icons.Default.Fireplace,
        nextMilestone = 30
    )
}

@Composable
fun getOtherStreaks(): List<StreakData> {
    return listOf(
        StreakData(
            id = 2,
            name = "Fajr On Time",
            description = "Waking up for morning prayer",
            days = 0,
            color = Color(0xFF4ECDC4),
            icon = Icons.Default.Schedule,
            nextMilestone = 40
        ),
        StreakData(
            id = 3,
            name = "Quran Reading",
            description = "Daily Quran recitation",
            days = 0,
            color = Color(0xFF8B4513),
            icon = Icons.Default.Bolt,
            nextMilestone = 30
        )
    )
}

@Preview(showBackground = true)
@Composable
fun PremiumStreaksCardPreview() {
    var completedPrayers by remember { mutableStateOf(setOf("Fajr", "Dhuhr")) }
    var streakDays by remember { mutableStateOf(12) }

    MaterialTheme {
        PremiumStreaksCard(
            currentStreakData = StreakData(
                id = 1,
                name = "Daily Prayer Streak",
                description = "Consecutive days of completing all 5 prayers",
                days = streakDays,
                color = Color(0xFFFF6B6B),
                icon = Icons.Default.Fireplace,
                nextMilestone = 30
            ),
            completedPrayers = completedPrayers,
            onTogglePrayer = { prayer ->
                completedPrayers = if (completedPrayers.contains(prayer)) {
                    completedPrayers - prayer
                } else {
                    completedPrayers + prayer
                }
            },
            onAddDay = {
                if (completedPrayers.size == 5) {
                    streakDays += 1
                    completedPrayers = emptySet()
                }
            },
            otherStreaks = listOf(
                StreakData(
                    id = 2,
                    name = "Fajr On Time",
                    description = "Waking up for morning prayer",
                    days = 8,
                    color = Color(0xFF4ECDC4),
                    icon = Icons.Default.Schedule,
                    nextMilestone = 40
                ),
                StreakData(
                    id = 3,
                    name = "Quran Reading",
                    description = "Daily Quran recitation",
                    days = 5,
                    color = Color(0xFF8B4513),
                    icon = Icons.Default.Bolt,
                    nextMilestone = 30
                )
            ),
            longestStreak = 45,
            totalPerfectDays = 78
        )
    }
}
