package com.rudra.prayerallthetime.ui.screen.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.rudra.prayerallthetime.data.Prayer
import com.rudra.prayerallthetime.ui.theme.PrayerAllTheTimeTheme

@Composable
fun PrayerTimeline(prayers: List<Prayer>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        Text(
            text = "Today's Timeline",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
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
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(48.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(if (prayer.isPrayed) Color(0xFF4CAF50) else Color.LightGray)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .weight(1f)
                        .background(Color.LightGray.copy(alpha = 0.5f))
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(bottom = 24.dp)
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
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (prayer.isPrayed) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                    Text(
                        text = prayer.time,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
                
                if (prayer.isPrayed) {
                    Badge(
                        containerColor = Color(0xFFE8F5E9),
                        contentColor = Color(0xFF4CAF50)
                    ) {
                        Text("Completed", modifier = Modifier.padding(4.dp))
                    }
                } else {
                    Text(
                        text = "Pending",
                        fontSize = 12.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(4.dp)
                    )
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
