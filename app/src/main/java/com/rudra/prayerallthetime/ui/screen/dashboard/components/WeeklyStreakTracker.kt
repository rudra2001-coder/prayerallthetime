package com.rudra.prayerallthetime.ui.screen.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudra.prayerallthetime.ui.theme.PrayerAllTheTimeTheme

@Composable
fun WeeklyStreakTracker(
    modifier: Modifier = Modifier,
    streakCount: Int,
    weeklyCompletion: List<Boolean> // 7 booleans for Mon-Sun or similar
) {
    val days = listOf("M", "T", "W", "T", "F", "S", "S")
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF7E6))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Weekly Streak",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8B4513)
                    )
                    Text(
                        text = "Don't break the chain!",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF8B4513).copy(alpha = 0.7f)
                    )
                }
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$streakCount",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFF4500)
                    )
                    Text(
                        text = " 🔥",
                        fontSize = 24.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                days.forEachIndexed { index, day ->
                    val isCompleted = if (index < weeklyCompletion.size) weeklyCompletion[index] else false
                    DayStreakItem(day, isCompleted)
                }
            }
        }
    }
}

@Composable
fun DayStreakItem(day: String, isCompleted: Boolean) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(
                    if (isCompleted) Color(0xFFFF4500)
                    else Color(0xFF8B4513).copy(alpha = 0.1f)
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Text("🔥", fontSize = 16.sp)
            } else {
                Text(
                    text = day,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF8B4513).copy(alpha = 0.5f)
                )
            }
        }
        if (isCompleted) {
            Text(
                text = day,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF8B4513)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WeeklyStreakTrackerPreview() {
    PrayerAllTheTimeTheme {
        WeeklyStreakTracker(
            streakCount = 5,
            weeklyCompletion = listOf(true, true, true, true, true, false, false)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DayStreakItemPreview() {
    PrayerAllTheTimeTheme {
        DayStreakItem(day = "F", isCompleted = true)
    }
}
