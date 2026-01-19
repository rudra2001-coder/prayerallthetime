package com.rudra.prayerallthetime.ui.screen.dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudra.prayerallthetime.data.Prayer
import com.rudra.prayerallthetime.ui.theme.PrayerAllTheTimeTheme
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun PrayerClocksSection(
    prayers: List<Prayer>,
    onPrayerClick: (Prayer) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Filter out Sunrise to show only the 5 daily prayers
    val dailyPrayers = prayers.filter { it.name != "Sunrise" }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
    ) {
        Text(
            text = "Prayer Times Status",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )
        
        // Using a LazyRow to ensure all items fit nicely and keep them all visible
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(end = 16.dp)
        ) {
            items(dailyPrayers) { prayer ->
                PrayerClockItem(
                    prayer = prayer,
                    onClick = { onPrayerClick(prayer) },
                    modifier = Modifier.width(90.dp) // Fixed width to keep consistency
                )
            }
        }
    }
}

@Composable
fun PrayerClockItem(
    prayer: Prayer,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (prayer.isPrayed) Color(0xFFE8F5E9) // Light Green
                             else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        border = if (!prayer.isPrayed) androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f)) else null
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 12.dp, horizontal = 4.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = prayer.name,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (prayer.isPrayed) Color(0xFF2E7D32) else MaterialTheme.colorScheme.primary
            )
            
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .padding(2.dp),
                contentAlignment = Alignment.Center
            ) {
                AnalogClockFace(time = prayer.time, isDone = prayer.isPrayed)
            }

            Text(
                text = prayer.time,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = if (prayer.isPrayed) Color(0xFF2E7D32).copy(alpha = 0.8f) else MaterialTheme.colorScheme.onSurfaceVariant
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = if (prayer.isPrayed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                    contentDescription = null,
                    tint = if (prayer.isPrayed) Color(0xFF4CAF50) else Color.Gray.copy(alpha = 0.3f),
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (prayer.isPrayed) "Done" else "Wait",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (prayer.isPrayed) Color(0xFF4CAF50) else Color.Gray
                )
            }
        }
    }
}

@Composable
fun AnalogClockFace(time: String, isDone: Boolean) {
    val (hour, minute) = try {
        val parts = time.split(":", " ")
        var h = parts[0].toInt()
        val m = parts[1].toInt()
        if (time.contains("PM", ignoreCase = true) && h != 12) h += 12
        if (time.contains("AM", ignoreCase = true) && h == 12) h = 0
        h to m
    } catch (e: Exception) {
        0 to 0
    }

    val primaryColor = if (isDone) Color(0xFF4CAF50) else MaterialTheme.colorScheme.primary
    val secondaryColor = if (isDone) Color(0xFF2E7D32).copy(alpha = 0.5f) else Color.Gray.copy(alpha = 0.3f)

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2

        // Outer ring
        drawCircle(
            color = secondaryColor,
            radius = radius,
            center = center,
            style = Stroke(width = 1.5.dp.toPx())
        )

        // Hour hand
        val hourAngle = (hour % 12 + minute / 60f) * 30f - 90f
        val hourLen = radius * 0.5f
        drawLine(
            color = primaryColor,
            start = center,
            end = Offset(
                center.x + hourLen * cos(Math.toRadians(hourAngle.toDouble())).toFloat(),
                center.y + hourLen * sin(Math.toRadians(hourAngle.toDouble())).toFloat()
            ),
            strokeWidth = 2.5.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Minute hand
        val minAngle = minute * 6f - 90f
        val minLen = radius * 0.8f
        drawLine(
            color = primaryColor.copy(alpha = 0.6f),
            start = center,
            end = Offset(
                center.x + minLen * cos(Math.toRadians(minAngle.toDouble())).toFloat(),
                center.y + minLen * sin(Math.toRadians(minAngle.toDouble())).toFloat()
            ),
            strokeWidth = 1.5.dp.toPx(),
            cap = StrokeCap.Round
        )
        
        drawCircle(
            color = primaryColor,
            radius = 1.5.dp.toPx(),
            center = center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PrayerClocksSectionPreview() {
    val samplePrayers = listOf(
        Prayer(name = "Fajr", time = "05:15 AM", isPrayed = true),
        Prayer(name = "Dhuhr", time = "01:00 PM", isPrayed = true),
        Prayer(name = "Asr", time = "03:45 PM", isPrayed = false),
        Prayer(name = "Maghrib", time = "06:20 PM", isPrayed = false),
        Prayer(name = "Isha", time = "08:00 PM", isPrayed = false)
    )
    PrayerAllTheTimeTheme {
        PrayerClocksSection(prayers = samplePrayers)
    }
}
