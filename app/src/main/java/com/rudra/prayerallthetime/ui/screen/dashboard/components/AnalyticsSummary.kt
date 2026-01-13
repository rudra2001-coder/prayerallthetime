package com.rudra.prayerallthetime.ui.screen.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rudra.prayerallthetime.ui.theme.PrayerAllTheTimeTheme

@Composable
fun AnalyticsSummary(
    dailyAvg: Float,
    bestPrayer: String,
    consistencyScore: Int,
    monthlyProgress: List<Float> // 30 values for the month
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Analytics Summary",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryMetricItem(
                    label = "Daily Avg",
                    value = "${(dailyAvg * 100).toInt()}%",
                    icon = Icons.Default.TrendingUp,
                    color = Color(0xFF4ECDC4)
                )
                SummaryMetricItem(
                    label = "Best Prayer",
                    value = bestPrayer,
                    icon = Icons.Default.Star,
                    color = Color(0xFFFFD93D)
                )
                SummaryMetricItem(
                    label = "Consistency",
                    value = "$consistencyScore%",
                    icon = Icons.Default.ShowChart,
                    color = Color(0xFF45B7D1)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Monthly Progress",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Simple Sparkline/Bar chart for Monthly Progress
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(2.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    monthlyProgress.forEach { value ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(value.coerceAtLeast(0.1f))
                                .clip(RoundedCornerShape(2.dp))
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.6f))
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryMetricItem(label: String, value: String, icon: ImageVector, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        Text(text = label, fontSize = 10.sp, color = Color.Gray)
    }
}

@Preview(showBackground = true)
@Composable
fun AnalyticsSummaryPreview() {
    PrayerAllTheTimeTheme {
        AnalyticsSummary(
            dailyAvg = 0.85f,
            bestPrayer = "Fajr",
            consistencyScore = 92,
            monthlyProgress = List(30) { (0.5f + Math.random() * 0.5f).toFloat() }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SummaryMetricItemPreview() {
    PrayerAllTheTimeTheme {
        SummaryMetricItem(
            label = "Daily Avg",
            value = "85%",
            icon = Icons.Default.TrendingUp,
            color = Color(0xFF4ECDC4)
        )
    }
}
