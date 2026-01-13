package com.rudra.prayerallthetime.ui.screen.dashboard.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.navigation.NavController
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
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = "Prayer Clocks",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            prayers.forEach { prayer ->
                PrayerClockItem(
                    prayer = prayer,
                    onClick = { onPrayerClick(prayer) },
                    modifier = Modifier.weight(1f)
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
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (prayer.isPrayed) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f) 
                             else MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = prayer.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            // Visual Clock Representation
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .padding(4.dp),
                contentAlignment = Alignment.Center
            ) {
                AnalogClockFace(time = prayer.time, isDone = prayer.isPrayed)
            }

            Text(
                text = prayer.time,
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Icon(
                imageVector = if (prayer.isPrayed) Icons.Default.CheckCircle else Icons.Default.RadioButtonUnchecked,
                contentDescription = if (prayer.isPrayed) "Done" else "Pending",
                tint = if (prayer.isPrayed) Color(0xFF4CAF50) else Color.Gray.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
            
            Text(
                text = if (prayer.isPrayed) "Done" else "Pending",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = if (prayer.isPrayed) Color(0xFF4CAF50) else Color.Gray
            )
        }
    }
}

@Composable
fun AnalogClockFace(time: String, isDone: Boolean) {
    val (hour, minute) = try {
        val parts = time.split(":", " ")
        var h = parts[0].toInt()
        val m = parts[1].toInt()
        if (time.contains("PM") && h != 12) h += 12
        if (time.contains("AM") && h == 12) h = 0
        h to m
    } catch (e: Exception) {
        0 to 0
    }

    val secondaryColor = if (isDone) Color(0xFF4CAF50) else Color.Gray

    Canvas(modifier = Modifier.fillMaxSize()) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2

        // Outer circle
        drawCircle(
            color = secondaryColor.copy(alpha = 0.2f),
            radius = radius,
            center = center,
            style = Stroke(width = 2.dp.toPx())
        )

        // Hour hand
        val hourAngle = (hour % 12 + minute / 60f) * 30f - 90f
        val hourLen = radius * 0.5f
        drawLine(
            color = secondaryColor,
            start = center,
            end = Offset(
                center.x + hourLen * cos(Math.toRadians(hourAngle.toDouble())).toFloat(),
                center.y + hourLen * sin(Math.toRadians(hourAngle.toDouble())).toFloat()
            ),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Minute hand
        val minAngle = minute * 6f - 90f
        val minLen = radius * 0.8f
        drawLine(
            color = secondaryColor.copy(alpha = 0.7f),
            start = center,
            end = Offset(
                center.x + minLen * cos(Math.toRadians(minAngle.toDouble())).toFloat(),
                center.y + minLen * sin(Math.toRadians(minAngle.toDouble())).toFloat()
            ),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )
        
        // Center dot
        drawCircle(
            color = secondaryColor,
            radius = 2.dp.toPx(),
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

@Preview(showBackground = true)
@Composable
fun PrayerClockItemPreview() {
    PrayerAllTheTimeTheme {
        PrayerClockItem(
            prayer = Prayer(name = "Fajr", time = "05:15 AM", isPrayed = true),
            onClick = {}
        )
    }
}
