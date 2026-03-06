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
import androidx.compose.material.icons.filled.Check
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
import com.rudra.prayerallthetime.data.Prayer
import com.rudra.prayerallthetime.ui.theme.*

@Composable
fun PrayerTimeline(prayers: List<Prayer>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Today's Prayer Timeline",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.SemiBold,
                color = TextPrimaryDark
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        prayers.forEachIndexed { index, prayer ->
            TimelineItem(
                prayer = prayer,
                isLast = index == prayers.size - 1
            )
        }
    }
}

@Composable
fun TimelineItem(prayer: Prayer, isLast: Boolean) {
    val prayerColor = getPrayerColor(prayer.name)
    
    val nodeColor by animateColorAsState(
        targetValue = if (prayer.isPrayed) SuccessColor else MidnightBlueLight,
        label = "nodeColor"
    )
    
    val scale by animateFloatAsState(
        targetValue = if (prayer.isPrayed) 1.1f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMedium),
        label = "scale"
    )
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        // Timeline node and connector
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(40.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .scale(scale)
                    .clip(CircleShape)
                    .background(nodeColor),
                contentAlignment = Alignment.Center
            ) {
                if (prayer.isPrayed) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(10.dp)
                    )
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(
                            if (prayer.isPrayed) SuccessColor.copy(alpha = 0.3f) 
                            else MidnightBlueLight
                        )
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Prayer details
        Column(
            modifier = Modifier
                .padding(bottom = if (isLast) 0.dp else 20.dp)
                .weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = prayer.name,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (prayer.isPrayed) TextPrimaryDark else TextSecondaryDark
                        )
                    )
                    Text(
                        text = prayer.time,
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = prayerColor,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
                
                if (prayer.isPrayed) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SuccessColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "Completed",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = SuccessColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MidnightBlueLight
                    ) {
                        Text(
                            text = "Pending",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = TextTertiaryDark,
                                fontWeight = FontWeight.Medium
                            )
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PrayerTimelinePreview() {
    val samplePrayers = listOf(
        Prayer(name = "Fajr", time = "05:15 AM", isPrayed = true),
        Prayer(name = "Dhuhr", time = "01:00 PM", isPrayed = true),
        Prayer(name = "Asr", time = "03:45 PM", isPrayed = false),
        Prayer(name = "Maghrib", time = "05:20 PM", isPrayed = false),
        Prayer(name = "Isha", time = "07:00 PM", isPrayed = false)
    )
    PrayerAllTheTimeTheme {
        PrayerTimeline(prayers = samplePrayers)
    }
}

@Preview(showBackground = true)
@Composable
fun TimelineItemPreview() {
    PrayerAllTheTimeTheme {
        TimelineItem(
            prayer = Prayer(name = "Fajr", time = "05:15 AM", isPrayed = true),
            isLast = false
        )
    }
}
