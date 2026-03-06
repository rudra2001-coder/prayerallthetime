package com.rudra.prayerallthetime.ui.screen.dashboard.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudra.prayerallthetime.ui.theme.*

@Composable
fun WeeklyStreakTracker(
    modifier: Modifier = Modifier,
    streakCount: Int,
    weeklyCompletion: List<Boolean>
) {
    val days = listOf("M", "T", "W", "T", "F", "S", "S")
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MidnightBlueCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextPrimaryDark
                        )
                    )
                    Text(
                        text = "Keep your momentum going!",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextSecondaryDark
                        )
                    )
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(StreakColor.copy(alpha = 0.15f))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = null,
                        tint = StreakColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$streakCount",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = StreakColor
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
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
    val animatedScale by animateFloatAsState(
        targetValue = if (isCompleted) 1.1f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "scale"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isCompleted) StreakColor else MidnightBlueLight,
        label = "background"
    )
    
    val contentColor by animateColorAsState(
        targetValue = if (isCompleted) Color.White else TextTertiaryDark,
        label = "content"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .scale(animatedScale)
                .clip(CircleShape)
                .background(backgroundColor),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            } else {
                Text(
                    text = day,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = contentColor
                )
            }
        }
        Text(
            text = day,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium,
            color = if (isCompleted) StreakColor else TextTertiaryDark
        )
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
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DayStreakItem(day = "M", isCompleted = true)
            DayStreakItem(day = "T", isCompleted = false)
        }
    }
}
